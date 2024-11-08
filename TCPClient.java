package CS4504Project;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;

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
           ArrayList<Long> times = new ArrayList<>();
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
         List<BigInteger[][]> matrices = readMatricesFromFile("C:\\Users\\katar\\OneDrive\\Documents\\CS4504Project\\file.txt");
         String fromServer; // messages received from ServerRouter
         String fromUser; // messages sent to ServerRouter
			String address ="192.168.1.70"; // destination IP (Server)
			long t0, t1 = 0, t;
			// Communication process (initial sends/receives
			out.println(address);// initial send (IP of the destination Server)
			fromServer = in.readLine();//initial receive from router (verification of connection)
			System.out.println("ServerRouter: " + fromServer);
			out.println(host); // Client sends the IP of its machine as initial send
			t0 = System.currentTimeMillis();
			// Communication while loop
           for(BigInteger [][] matrix : matrices){
               // convert the matrix to String format for sending
               StringBuilder matrixString = new StringBuilder();
               for(BigInteger[] row : matrix){
                   for(BigInteger value : row){
                       matrixString.append(value).append(" ");
                   }
                   matrixString.append("\n");
               }
               t0 = System.currentTimeMillis();
               out.println(matrixString.toString().trim());
               fromServer = in.readLine();
               System.out.println("Server: " + fromServer);
               t1 = System.currentTimeMillis();
           }

           // Calculate and log the cycle time
           t = t1 - t0;
           times.add(t);
           System.out.println("Server: " + fromServer + " | Cycle time: " + t + " ms");

           for(Long l : times){
               totalTime += l;
           }
           System.out.println("Total time:" + totalTime + " ms");

           out.println("send metrics");
           String speedup = in.readLine();
           String efficiency = in.readLine();

           System.out.println("Speedup: "+ speedup);
           System.out.println("Efficiency: "+ efficiency);

           // closing connections
         out.close();
         in.close();
         s.close();
      }

      public static List<BigInteger[][]> readMatricesFromFile(String fileName){
          List<BigInteger[][]> matrices = new ArrayList<>();
          try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
              String line;
              while ((line = br.readLine()) != null) {
                  if (line.startsWith("#")) {
                      br.readLine();  // Read and skip the line containing the matrix size
                      line = br.readLine().trim(); // Read the matrix values as one single line
                      String[] values = line.split("\\s+"); // Split by whitespace
                      int size = values.length;

                      BigInteger[][] matrix = new BigInteger[size][size];
                      for (int i = 0; i < size; i++) {
                          for (int j = 0; j < size; j++) {
                              matrix[i][j] = new BigInteger(values[j]);
                          }
                      }

                      matrices.add(matrix); // Add the matrix to the list
                  }
              }
          } catch (IOException e) {
              e.printStackTrace();
          }
          return matrices;
      }
   }
