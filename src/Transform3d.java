public class Transform3d {
    private static final int SIZE = 3;

    // Transformation matrices
    private final double[][] tXY = new double[SIZE][SIZE];
    private final double[][] tYZ = new double[SIZE][SIZE];
    private final double[][] tXZ = new double[SIZE][SIZE];
    private final double[][] temp = new double[SIZE][SIZE];

    // Transformation matrix that is visible to users of the class
    public double[][] tmx = new double[SIZE][SIZE];

    private static void setIdentity(double[][] mx) {
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                mx[i][j] = 0.0;
            }
            mx[i][i] = 1.0;
        }
    }

    private static void multiply(double[][] mx1, double[][] mx2, double[][] result) {
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                result[i][j] = 0;
                for(int k = 0; k < SIZE; k++) {
                    result[i][j] += mx1[i][k] * mx2[k][j];
                }
            }
        }
    }

    public Transform3d() {
        setIdentity(tXY);
        setIdentity(tXZ);
        setIdentity(tYZ);
        calculate();
    }

    // The formula I am using to rotate the xy plane is:
    //       [ cos ϕ -sin ϕ 0 ]
    // Rxy = | sin ϕ cos ϕ  0 |
    //       [ 0     0      1 ]

    void setXY(double angle) {
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);

        tXY[0][0] = cosAngle;
        tXY[0][1] = -sinAngle;
        tXY[0][2] = 0;

        tXY[1][0] = sinAngle;
        tXY[1][1] = cosAngle;
        tXY[1][2] = 0;

        tXY[2][0] = 0;
        tXY[2][1] = 0;
        tXY[2][2] = 1;
    }

    // The formula I am using to rotate the xy plane is:
    //       [ cos θ 0  sin θ ]
    // Rxz = | 0     1      0 |
    //       [-sin θ 0  cos θ ]

    void setXZ(double angle) {
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);

        tXZ[0][0] = cosAngle;
        tXZ[0][1] = 0;
        tXZ[0][2] = sinAngle;

        tXZ[1][0] = 0;
        tXZ[1][1] = 1;
        tXZ[1][2] = 0;

        tXZ[2][0] = -sinAngle;
        tXZ[2][1] = 0;
        tXZ[2][2] = cosAngle;
    }

    // The formula I am using to rotate the xy plane is:
    //       [ 1     0      0      ]
    // Ryz = | 0     cos ψ  -sin ψ |
    //       [ 0     sin ψ  cos ψ  ]

    void setYZ(double angle) {
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);

        tYZ[0][0] = 1;
        tYZ[0][1] = 0;
        tYZ[0][2] = 0;

        tYZ[1][0] = 0;
        tYZ[1][1] = cosAngle;
        tYZ[1][2] = -sinAngle;

        tYZ[2][0] = 0;
        tYZ[2][1] = sinAngle;
        tYZ[2][2] = cosAngle;
    }

    // Compute full transformation matrix from the 3 rotation matrices
    void calculate() {
        multiply(tXY, tXZ, temp);
        multiply(temp, tYZ, tmx);
    }

}
