package CS4504Project;

import java.io.*;
   import java.net.*;

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
         String fromServer; // messages sent to ServerRouter
         String fromClient; // messages received from ServerRouter
 			String address ="192.168.1.128"; // destination IP (Client)

			// Communication process (initial sends/receives)
			out.println(address);// initial send (IP of the destination Client)
			fromClient = in.readLine();// initial receive from router (verification of connection)
			System.out.println("ServerRouter: " + fromClient);
           out.println(host); // Server sends the IP of its machine as initial send
           // Communication while loop

           while ((fromClient = in.readLine()) != null) {
            System.out.println("Client said: " + fromClient);
            if (fromClient.equals("Bye.")) // exit statement
					break;
            fromServer = fromClient.toUpperCase(); // converting received message to upper case
            System.out.println("Server said: " + fromServer);
            out.println(fromServer); // sending the converted message back to the Client via ServerRouter
         }

			// closing connections
         out.close();
         in.close();
         s.close();
      }


   }
