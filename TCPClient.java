package CS4504Project;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class TCPClient {
       public static void main(String[] args) throws IOException {

			// Variables for setting up connection and communication
         Socket s = null; // socket to connect with ServerRouter
         PrintWriter out = null; // for writing to ServerRouter
         BufferedReader in = null; // for reading form ServerRouter
			InetAddress addr = InetAddress.getLocalHost();
			String host = addr.getHostAddress(); // Client machine's IP
           String routerName = "192.168.1.128";
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
         List<int[][]> matrices = readMatricesFromFile("C:\\IntelliJ IDEA Community Edition 2021.2\\src\\CS4504Project\\file.txt");
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
           for(int [][] matrix : matrices){
               // convert the matrix to String format for sending
               StringBuilder matrixString = new StringBuilder();
               for(int[] row : matrix){
                   for(int value : row){
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

           // closing connections
         out.close();
         in.close();
         s.close();
      }

      public static List<int[][]> readMatricesFromFile(String fileName){
           List<int[][]> matrices = new ArrayList<>();
           try (BufferedReader br = new BufferedReader((new FileReader(fileName)))){
               String line;
               while((line = br.readLine()) != null){
                   if(line.startsWith("#")){
                       // read the matrix size
                       line = br.readLine();
                       int size = Integer.parseInt(line.trim());
                       int [][] matrix = new int[size][size];

                       //read the matrix values
                       for(int i = 0; i < size; i++){
                           line = br.readLine();
                           String [] values = line.trim().split("");
                           for(int j = 0; j< size; j++){
                               matrix[i][j] = Integer.parseInt(values[j]);
                           }
                       }
                       matrices.add(matrix);
                   }
               }
           } catch (IOException e){
               e.printStackTrace();
           }
           return matrices;
      }
   }
