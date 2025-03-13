public class Wireframe {

    public int numTriangles = 0;
    public int numVertices = 0;

    // Vertex coordinates in the world scene for each vertex of the triangle
    public double[] xv, yv, zv;

    // Triangle mapping
    public int[] vtx1, vtx2, vtx3;

    public double[] origxv, origyv, origzv;

    // Add any data, properties or methods as appropriate

    // Here we apply the rotations and scaling before drawing the display
    public void toView(double[][] tmx, double scale) {
        for (int i = 0; i < numVertices; i++) {
            // Apply rotation matrix first
            double x = xv[i];
            double y = yv[i];
            double z = zv[i];

            double newX = x * tmx[0][0] + y * tmx[0][1] + z * tmx[0][2];
            double newY = x * tmx[1][0] + y * tmx[1][1] + z * tmx[1][2];
            double newZ = x * tmx[2][0] + y * tmx[2][1] + z * tmx[2][2];

            xv[i] = newX * scale;
            yv[i] = newY * scale;
            zv[i] = newZ * scale;
        }
    }


    // Here we set both our numVertices and numTriangles while also defining the size of
    // the array for the values that need to be stored.
    public void setNumVertices(int numVertices) {
        this.numVertices = numVertices;
        this.xv = new double[numVertices];
        this.yv = new double[numVertices];
        this.zv = new double[numVertices];

        this.origxv = new double[numVertices];
        this.origyv = new double[numVertices];
        this.origzv = new double[numVertices];
    }

    public void setNumTriangles(int numTriangles) {
        this.numTriangles = numTriangles;
        this.vtx1 = new int[numTriangles];
        this.vtx2 = new int[numTriangles];
        this.vtx3 = new int[numTriangles];
    }

    public void setOriginal() {
        for (int i = 0; i < numVertices; i++) {
            xv[i] = origxv[i];
            yv[i] = origyv[i];
            zv[i] = origzv[i];
        }
    }

    public void resetOriginal(double scale) {
        for (int i = 0; i < numVertices; i++) {
            xv[i] = origxv[i] * scale;
            yv[i] = origyv[i] * scale;
            zv[i] = origzv[i] * scale;
        }
    }

}
