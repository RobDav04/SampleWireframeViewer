/*
The Main() class is the "controller". This controls the other components according settings
on the sliders and buttons.

 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Main extends JFrame implements ActionListener, ChangeListener {

    // Dimensions of JFrame
    static private final int FRAME_WIDTH = 900;
    static private final int FRAME_HEIGHT = 800;
    static private final int PANEL_HEIGHT = 600;

    // Menu items
    private JMenuItem openItem, quitItem, helpItem;

    // Buttons to change the size of the figure
    private final JButton biggerButton, smallerButton;

    // Sliders for rotation angles
    private final JSlider sliderXY, sliderXZ, sliderYZ;
    static private final int SLIDER_MIN = 0;
    static private final int SLIDER_MAX = 360;
    static private final int SLIDER_INIT = 0;

    // Anti-aliasing buttons
    private final JRadioButton aaOff, aaOn;
    private boolean antiAlias = false;

    private Wireframe wired = null;
    private final Transform3d transform;

    private double scale = 2;

    private final double factor = 1.2;

    // JPanel to display the image
    private final DisplayPanel displayP;

    // Need this for the meu items and buttons
    public void actionPerformed(ActionEvent event) {
        repaint();

        JComponent source = (JComponent) event.getSource();

        if (source == openItem) {
            JFileChooser chooser = new JFileChooser("./");
            int retVal = chooser.showOpenDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File myFile = chooser.getSelectedFile();
                wired = WireframeDataIO.read(myFile);
                wired.setOriginal();
                wired.toView(transform.tmx, scale);
                repaint();
            }
        } else if (source == quitItem) {
            System.out.println("Quitting ...");
            System.exit(0);
        } else if (source == helpItem) {
            System.out.println("Help me!");
        } else if (source == biggerButton) {
            // Increase scale
            scale *= factor;
            if (wired != null) {
                // The reason why I am resetting the wireframe to original is if we
                // don't do this step I am applying the scale on top of scaled wireframe
                // potentially. Without this step we are applying scaling to the already scaled
                // wireframe caning weird scaling issues and the buttons not working
                // as intended. We want to apply scaling to its original position.
                // For example if scale is 6 but the wireframe is already scaled at 5
                // I would then be applying 6 on top of that 5.
                // Comment below line out to test.
                wired.resetOriginal(scale);
                wired.toView(transform.tmx, scale);
            }
        } else if (source == smallerButton) {
            // Decrease scale
            scale /= factor;
            if (wired != null) {
                // Similar to the biggerButton
                wired.resetOriginal(scale);
                wired.toView(transform.tmx, scale);
            }
        } else if (source == aaOff) {
            antiAlias = false;
        } else if (source == aaOn) {
            antiAlias = true;
        }
        // Redraw display after any change
        displayP.repaint();
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        double angle;
        repaint();

        if (source.getValueIsAdjusting()) {
            angle = Math.toRadians(source.getValue());

            // Reset to original vertices before applying new transformation
            // Similar to the scale buttons if we continue to apply transformations
            // on top of transformations this can cause inaccuracies.
            if (wired != null) {
                wired.resetOriginal(scale);
            }

            // Apply the appropriate rotation based on the slider
            if (source == sliderXY) {
                transform.setXY(angle);  // Set XY plane rotation
            } else if (source == sliderXZ) {
                transform.setXZ(angle);  // Set XZ plane rotation
            } else if (source == sliderYZ) {
                transform.setYZ(angle);  // Set YZ plane rotation
            }

            // Recalculate the full transformation matrix
            transform.calculate();
            wired.toView(transform.tmx, scale);  // Apply rotation
            displayP.repaint();  // Repaint the display panel
        }
    }


    // Helper function to set up menu bar, menus, and menu-items
    private void makeMenu() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        openItem = new JMenuItem("Open");
        quitItem = new JMenuItem("Quit");
        fileMenu.add(openItem);
        fileMenu.add(quitItem);

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        helpItem = new JMenuItem("Help");
        helpMenu.add(helpItem);
    }

    // Helper function to create and add a slider to the panel
    private static JSlider makeSlider(JPanel panel, String title) {
        JSlider slider = new JSlider(SLIDER_MIN, SLIDER_MAX, SLIDER_INIT);
        slider.setBorder(BorderFactory.createTitledBorder(title));
        slider.setMajorTickSpacing(90);
        slider.setMinorTickSpacing(30);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        panel.add(slider);
        return slider;
    }

    public Main() {
        super("159235 Assignment 2");

        transform = new Transform3d();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        // Set up menu bar, menus, and menu items
        makeMenu();
        openItem.addActionListener(this);
        quitItem.addActionListener(this);
        helpItem.addActionListener(this);

        // Get a reference to the JFrames content pane to which
        // JPanels will be added
        Container content = this.getContentPane();
        //content.setLayout(null);

        // Will use 3 "control panels"
        JPanel controlP1 = new JPanel();   // for the sliders
        JPanel controlP2 = new JPanel();   // for the rescale buttons
        JPanel controlP3 = new JPanel();   // for the antialiasing toggle
        content.add(controlP1);
        content.add(controlP2);
        content.add(controlP3);

        // Arrange panels on the frame by setting appropriate positions and sizes
        controlP1.setBounds(new Rectangle(0, 0, 620, 100));
        controlP2.setBounds(new Rectangle(620, 0, 250, 50));
        controlP3.setBounds(new Rectangle(620, 50, 250, 50));

        // Set up the sliders
        sliderXY = makeSlider(controlP1, "XY Plane");
        sliderXZ = makeSlider(controlP1, "XZ Plane");
        sliderYZ = makeSlider(controlP1, "YZ Plane");
        sliderXY.addChangeListener(this);
        sliderYZ.addChangeListener(this);
        sliderXZ.addChangeListener(this);

        // Set up the rescale buttons
        controlP2.add(new JLabel("Scaling"));
        biggerButton = new JButton("+");
        smallerButton = new JButton("-");
        controlP2.add(biggerButton);
        controlP2.add(smallerButton);
        biggerButton.addActionListener(this);
        smallerButton.addActionListener(this);

        // Set up anti-aliasing toggle buttons
        controlP3.add(new JLabel("Anti-aliasing"));
        aaOff = new JRadioButton("Off");
        aaOn = new JRadioButton("On");
        aaOff.setSelected(true);
        controlP3.add(aaOff);
        controlP3.add(aaOn);
        ButtonGroup group = new ButtonGroup();
        group.add(aaOff);
        group.add(aaOn);
        aaOff.addActionListener(this);
        aaOn.addActionListener(this);

        // JPanel to display the drawn figure - implemented as inner class below
        displayP = new DisplayPanel();
        displayP.setBounds(new Rectangle(0, 200, FRAME_WIDTH, PANEL_HEIGHT));
        content.add(displayP);

        this.setVisible(true);

    }

    // An inner class to handle the final rendering of the figure
    class DisplayPanel extends JPanel {
        public void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            g2.clearRect(0, 0, FRAME_WIDTH, PANEL_HEIGHT); // Must do this first

            // Place the origin at the centre of the panel and have the y-axis pointing up the screen
            g2.translate(0.5 * FRAME_WIDTH, 0.5 * PANEL_HEIGHT);
            g2.scale(1, -1);

            if (wired != null) {
                WireframeDrawer.draw(g2, wired, antiAlias);
            }
        }
    }

    // Program entry point
    public static void main(String[] args) {
        new Main();
    }
}
