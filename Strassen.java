package CS4504Project;

public class Strassen {

    // Method to multiply two matrices using Strassen's algorithm
    public static long[][] multiply(long[][] A, long[][] B) {
        int n = A.length;
        long[][] R = new long[n][n];
        /** base case **/
        if (n == 1)
            R[0][0] = A[0][0] * B[0][0];
        else
        {
            long[][] A11 = new long[n/2][n/2];
            long[][] A12 = new long[n/2][n/2];
            long[][] A21 = new long[n/2][n/2];
            long[][] A22 = new long[n/2][n/2];
            long[][] B11 = new long[n/2][n/2];
            long[][] B12 = new long[n/2][n/2];
            long[][] B21 = new long[n/2][n/2];
            long[][] B22 = new long[n/2][n/2];

            /** Dividing matrix A into 4 halves **/
            split(A, A11, 0 , 0);
            split(A, A12, 0 , n/2);
            split(A, A21, n/2, 0);
            split(A, A22, n/2, n/2);
            /** Dividing matrix B into 4 halves **/
            split(B, B11, 0 , 0);
            split(B, B12, 0 , n/2);
            split(B, B21, n/2, 0);
            split(B, B22, n/2, n/2);

            /**
             M1 = (A11 + A22)(B11 + B22)
             M2 = (A21 + A22) B11
             M3 = A11 (B12 - B22)
             M4 = A22 (B21 - B11)
             M5 = (A11 + A12) B22
             M6 = (A21 - A11) (B11 + B12)
             M7 = (A12 - A22) (B21 + B22)
             **/

            long [][] M1 = multiply(add(A11, A22), add(B11, B22));
            long [][] M2 = multiply(add(A21, A22), B11);
            long [][] M3 = multiply(A11, sub(B12, B22));
            long [][] M4 = multiply(A22, sub(B21, B11));
            long [][] M5 = multiply(add(A11, A12), B22);
            long [][] M6 = multiply(sub(A21, A11), add(B11, B12));
            long [][] M7 = multiply(sub(A12, A22), add(B21, B22));

            /**
             C11 = M1 + M4 - M5 + M7
             C12 = M3 + M5
             C21 = M2 + M4
             C22 = M1 - M2 + M3 + M6
             **/
            long [][] C11 = add(sub(add(M1, M4), M5), M7);
            long [][] C12 = add(M3, M5);
            long [][] C21 = add(M2, M4);
            long [][] C22 = add(sub(add(M1, M3), M2), M6);

            /** join 4 halves into one result matrix **/
            join(C11, R, 0 , 0);
            join(C12, R, 0 , n/2);
            join(C21, R, n/2, 0);
            join(C22, R, n/2, n/2);
        }
        /** return result **/
        return R;
    }

    /** Function to add two matrices **/
    private static long[][] add(long[][] A, long[][] B) {
        int n = A.length;
        long[][] C = new long[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] + B[i][j];
        return C;
    }

    /** Function to sub two matrices **/
    private static long[][] sub(long[][] A, long[][] B) {
        int n = A.length;
        long[][] C = new long[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        return C;
    }

    /** Function to split parent matrix into child matrices **/
    private static void split(long[][] P, long[][] C, int iB, int jB) {
        for(int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
            for(int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
                C[i1][j1] = P[i2][j2];
    }

    /** Function to join child matrices into parent matrix **/
    private static void join(long[][] C, long[][] P, int iB, int jB) {
        for(int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
            for(int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
                P[i2][j2] = C[i1][j1];
    }

    //create an integer matrix
    public static long[][] splitMatrices(String combined, int size, boolean firstHalf) {
        long[][] matrix = new long[size][size];
        String line;

        if (firstHalf) {line = combined.substring(0, combined.length()/2);}
        else {line = combined.substring(combined.length()/2);}

        int i = 0;
        for (int j = 0; j < size; j++) {
            for (int k = 0; k < size; k++) {
                matrix[j][k] = Integer.parseInt(String.valueOf(line.charAt(i++)));
            }
        }
        return matrix;
    }
}
