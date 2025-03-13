import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;


public class WireframeDataIO {

    // This reads in our data from the .tri file
    public static Wireframe read(File myFile) {

        Wireframe wired = new Wireframe();

        try {

            Scanner scanner = new Scanner(myFile);
            String line = scanner.nextLine();
            int numVertices = Integer.parseInt(line);
            int j = 0, k = 0;
            wired.setNumVertices(numVertices);

            while(j < numVertices) {
                line = scanner.nextLine();
                StringTokenizer strtok = new StringTokenizer(line, " \t");

                int i = Integer.parseInt(strtok.nextToken());
                double x = Double.parseDouble(strtok.nextToken());
                double y = Double.parseDouble(strtok.nextToken());
                double z = Double.parseDouble(strtok.nextToken());

                wired.xv[j] = x;
                wired.yv[j] = y;
                wired.zv[j] = z;

                wired.origxv[j] = x;
                wired.origyv[j] = y;
                wired.origzv[j] = z;

                j++;
            }

            line = scanner.nextLine();
            int numTriangles = Integer.parseInt(line);
            wired.setNumTriangles(numTriangles);

            while(k < numTriangles) {
                line = scanner.nextLine();
                StringTokenizer strtok = new StringTokenizer(line, " \t");

                int i = Integer.parseInt(strtok.nextToken());
                int v1 = Integer.parseInt(strtok.nextToken());
                int v2 = Integer.parseInt(strtok.nextToken());
                int v3 = Integer.parseInt(strtok.nextToken());

                wired.vtx1[k] = v1;
                wired.vtx2[k] = v2;
                wired.vtx3[k] = v3;

                k++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return wired;
    }
}
