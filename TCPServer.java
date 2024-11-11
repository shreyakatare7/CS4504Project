package CS4504Project;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class TCPServer {
public static void main(String[] args) throws IOException {

    // Variables for setting up connection and communication
    Socket s = null; // socket to connect with ServerRouter
    PrintWriter out = null; // for writing to ServerRouter
    BufferedReader in = null; // for reading form ServerRouter
    InetAddress addr = InetAddress.getLocalHost();
    String host = addr.getHostAddress(); // Server machine's IP
    String routerName = "192.168.1.128";
    int SockNum = 5555; // port number

    // Tries to connect to the ServerRouter
    try {
        s = new Socket(routerName, SockNum);
        out = new PrintWriter(s.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));

    } catch (UnknownHostException e) {
        System.err.println("Don't know about router: " + routerName);
        System.exit(1);
    } catch (IOException e) {
        System.err.println("Couldn't get I/O for the connection to: " + routerName);
        System.exit(1);
    }

    // Variables for message passing
    String fromServer; // messages sent to ServerRouter
    String fromClient; // messages received from ServerRouter
    List<int[][]> matrices = new ArrayList<>();
    int numMatrices = 2;
    int numThreads = 3;
    ExecutorService singleThreadExecutor = Executors.newFixedThreadPool(1);
    ExecutorService multiThreadExecutor = Executors.newFixedThreadPool(numThreads);
    String address = "192.168.1.128"; // destination IP (Client)

    // Communication process (initial sends/receives)
    out.println(address);// initial send (IP of the destination Client)
    fromClient = in.readLine();// initial receive from router (verification of connection)
    System.out.println("ServerRouter: " + fromClient);
    out.println(host); // Server sends the IP of its machine as initial send

    // Communication while loop
    while (matrices.size() < numMatrices) {
        fromClient = in.readLine();
        int[][] matrix = parseMatrix(fromClient);
        matrices.add(matrix);
    }

    long singleStartTime = System.currentTimeMillis();
    int[][] singleThreadResult = multiplyBinaryTree(matrices, singleThreadExecutor);
    long singleEndTime = System.currentTimeMillis();
    long singleThreadTime = singleEndTime - singleStartTime;

    long parallelStartTime = System.currentTimeMillis();
    int[][] parallelResult = multiplyBinaryTree(matrices, multiThreadExecutor);
    long parallelEndTime = System.currentTimeMillis();
    long parallelTime = parallelEndTime - parallelStartTime;

    // Calculate speedup and efficiency
    double speedup = (double) singleThreadTime / parallelTime;
    double efficiency = speedup / numThreads;

    // Send the results and performance metrics to the client
    out.println(Arrays.deepToString(parallelResult));
    out.println("Single-thread time: " + singleThreadTime + " ms");
    out.println("Parallel time: " + parallelTime + " ms");
    out.println("Speedup: " + speedup);
    out.println("Efficiency: " + efficiency);

    //closing connections
    singleThreadExecutor.shutdown();
    multiThreadExecutor.shutdown();
    out.close();
    in.close();
    s.close();
}
    private static int[][] multiplyBinaryTree (List < int[][]>matrices, ExecutorService executor){
        while (matrices.size() > 1) {
            List<Future<int[][]>> results = new ArrayList<>();
            List<int[][]> nextLevelMatrices = new ArrayList<>();

            for (int i = 0; i < matrices.size(); i += 2) {
                final int[][] A = matrices.get(i);
                final int[][] B;
                if(i+1 < matrices.size()){
                    B = matrices.get(i+1);
                }
                else{
                    B = null;
                }
                if (B != null) {
                    results.add(executor.submit(() -> Strassen.multiply(A, B)));
                } else {
                    nextLevelMatrices.add(A);
                }
            }

            for (Future<int[][]> result : results) {
                try {
                    nextLevelMatrices.add(result.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            matrices = nextLevelMatrices;
        }

        return matrices.get(0);
    }

    private static int[][] parseMatrix (String matrixData){
        String[] rows = matrixData.trim().split("\n");
        int size = rows.length;
        int[][] matrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            String[] values = rows[i].trim().split("\\s+");
            for (int j = 0; j < size; j++) {
                matrix[i][j] = Integer.parseInt(values[j]);
            }
        }
        return matrix;
    }
}
