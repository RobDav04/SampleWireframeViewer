import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WireframeDrawer {

    public static void draw(Graphics2D g2, Wireframe wired, boolean antiAlias) {

        // Checking for antiAlias
        if(antiAlias) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        // Array used later to sort the polygons for painters algorithm
        List<PolygonWithDepth> polygonList = new ArrayList<>();

        // We first want to loop over all the triangles
        for(int i = 0; i < wired.numTriangles; i++) {
            // Get the vertex indices
            int v1 = wired.vtx1[i];
            int v2 = wired.vtx2[i];
            int v3 = wired.vtx3[i];

            // 2D coordinates
            int x1 = (int) wired.xv[v1], y1 = (int) wired.yv[v1];
            int x2 = (int) wired.xv[v2], y2 = (int) wired.yv[v2];
            int x3 = (int) wired.xv[v3], y3 = (int) wired.yv[v3];

            // This is a method for finding the triangle edges
            int px1 = x2 - x1;
            int py1 = y2 - y1;
            int px2 = x3 - x1;
            int py2 = y3 - y1;

            // Depth is how far the polygon/triangle is from the viewpoint
            // we need to use the z coordinate for this. We can get an average
            double depth = (wired.zv[v1] + wired.zv[v2] + wired.zv[v3]) / 3;

            // In order to find out if a polygon is backwards facing it depends
            // on the direction of the triangle. The equation below calculates
            // the direction that the triangle is going.
            // If the triangle is clockwise (negative number) then the triangle
            // is back facing, so we do not draw it. If the triangle is anti-clockwise
            // (equal to 0 or positive number) then it is forward facing, so we draw it
            int direction = px1 * py2 - py1 * px2;

            if(direction < 0) {
                continue;
            }

            // We make a polygon we also are going to need the xPoints and the yPoints and nPoints.
            // nPoints will always be 3 because our Polygon will always be a triangle. As for xPoints and yPoints
            // we need to get the Points from the vertices. From our file we read in the data from the .tri and store
            // them in an array that holds the 2D coordinates.

            int[] xPoints = {x1, x2, x3};
            int[] yPoints = {y1, y2, y3};

            // Create polygon (triangle) add to array
            Polygon polygon = new Polygon(xPoints, yPoints, 3);
            PolygonWithDepth polygonDepth = new PolygonWithDepth(polygon, depth);
            polygonList.add(polygonDepth);

        }

        // We are sorting our array list to draw the triangle furthest away as per
        // painters algorithm
        polygonList.sort(Comparator.comparingDouble(PolygonWithDepth::getDepth));

        // The loop to draw the polygons starting from furthers away
        for (PolygonWithDepth dep : polygonList) {
            g2.setColor(Color.black);
            g2.drawPolygon(dep.getPolygon());
            g2.setColor(Color.gray);
            g2.fill(dep.getPolygon());
        }
    }

    // This is a simple class I made to keep track of the depth of each
    // triangle.
    public static class PolygonWithDepth {
        private Polygon polygon;
        private double depth;

        public PolygonWithDepth(Polygon polygon, double depth) {
            this.polygon = polygon;
            this.depth = depth;
        }

        public Polygon getPolygon() {
            return polygon;
        }

        public double getDepth() {
            return depth;
        }
    }
}