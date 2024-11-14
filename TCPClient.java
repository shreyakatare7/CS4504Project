package CS4504Project;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class TCPClient {
       public static void main(String[] args) throws IOException {

           // Variables for setting up connection and communication
         Socket s = null; // socket to connect with ServerRouter
         PrintWriter out = null; // for writing to ServerRouter
         BufferedReader in = null; // for reading form ServerRouter
			InetAddress addr = InetAddress.getLocalHost();
			String host = addr.getHostAddress(); // Client machine's IP
           String routerName = "192.168.1.110";
			int SockNum = 5555; // port number
           long totalTime = 0;

			// Tries to connect to the ServerRouter
         try {
            s = new Socket(routerName, SockNum);
            out = new PrintWriter(s.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
         }
             catch (UnknownHostException e) {
               System.err.println("Don't know about router: " + routerName);
               System.exit(1);
            }
             catch (IOException e) {
               System.err.println("Couldn't get I/O for the connection to: " + routerName);
               System.exit(1);
            }


      	// Variables for message passing
         long[][] matrix = readMatricesFromFile("C:\\Users\\katar\\IntelliJ IDEA Community Edition 2024.2.3\\CS4504Project\\file.txt");
         String fromServer; // messages received from ServerRouter
         String fromUser; // messages sent to ServerRouter
			String address ="192.168.1.70"; // destination IP (Server)
			long t0, t1 = 0, t;
           ArrayList<Long> times = new ArrayList<>();

           // Communication process (initial sends/receives
           t0 = System.currentTimeMillis();
			out.println(address);// initial send (IP of the destination Server)
			fromServer = in.readLine();//initial receive from router (verification of connection)
			System.out.println("ServerRouter: " + fromServer);
			out.println(host); // Client sends the IP of its machine as initial send
            fromServer = in.readLine();
            t1 = System.currentTimeMillis();
            System.out.println("Server: "+ fromServer);
            System.out.println("Handshake cycle time: " + (t1-t0)+" ms");

           // Communication of matrix
           StringBuilder matrixString = new StringBuilder();
           for (long[] row : matrix) {
               for (long value : row) {
                   matrixString.append(value).append(" ");
               }
               matrixString.append(";");
           }

           t0 = System.currentTimeMillis();
           out.println(matrixString.toString().trim());
           fromServer = in.readLine();
           String resultMatrixString = fromServer;
           System.out.println("Server: " + fromServer);
           t1 = System.currentTimeMillis();

           // Calculate and log the cycle time
           t = t1 - t0;
           times.add(t);
           System.out.println("Cycle time: " + t + " ms");

           String[] rows = resultMatrixString.substring(2, resultMatrixString.length() - 2).split("\\], \\[");
           int rowCount = rows.length;
           int colCount = rows[0].split(",").length;
           long[][] resultMatrix = new long[rowCount][colCount];

           // Fill result matrix
           for (int i = 0; i < rowCount; i++) {
               String[] values = rows[i].split(",");
               for (int j = 0; j < colCount; j++) {
                   resultMatrix[i][j] = Long.parseLong(values[j].trim());
               }
           }

           // Print resulting matrix
           System.out.println("Resulting Matrix:");
           for (int i = 0; i < rowCount; i++) {
               for (int j = 0; j < colCount; j++) {
                   System.out.print(resultMatrix[i][j] + " ");
               }
               System.out.println();
           }

           System.out.println("\nPerformance Metrics:");
           fromServer = in.readLine();  // Single-thread time
           System.out.println(fromServer);

           fromServer = in.readLine();  // Parallel time
           System.out.println(fromServer);

           fromServer = in.readLine();  // Speedup
           System.out.println(fromServer);

           fromServer = in.readLine();  // Efficiency
           System.out.println(fromServer);

           for(Long l : times){
               totalTime += l;
           }
           System.out.println("Total time:" + totalTime + " ms");

           // closing connections
         out.close();
         in.close();
         s.close();
      }

      public static long[][] readMatricesFromFile(String fileName){
          long[][] matrix = null;
          try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
              String line;
              while ((line = br.readLine()) != null) {
                  if (line.startsWith("#")) {
                      String[] dimensions = line.split("\\s+");
                      int rows = Integer.parseInt(dimensions[1]);
                      int cols = Integer.parseInt(dimensions[3]);

                      matrix = new long[rows][cols];
                      for (int i = 0; i < rows; i++) {
                          line = br.readLine();
                          String [] values = line.trim().split("\\s+");

                          for (int j = 0; j < cols; j++) {
                              matrix[i][j] = Long.parseLong((values[j]));
                          }
                      }
                  }
              }
          } catch (IOException e) {
              e.printStackTrace();
          }
          return matrix;
      }
   }
