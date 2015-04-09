import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
   
   private ServerSocket serverSocket;
   /* HashTable created for storing the entries for clients */
   Map clientList = new HashMap();

   public Server(int serverPort) throws IOException {
      waitForConnection(serverPort);
   }

   public void waitForConnection(int serverPort) throws IOException {
      serverSocket = new ServerSocket(serverPort);       //Attempts to create a server socket bound to the specified port
      serverSocket.setSoTimeout(1000000);                //sets the server waiting time-out value

      while (true) {                                     //loop to accept connections as they come

         /* Set up incoming connection in new socket */
         Socket s = serverSocket.accept();      //waits for an incoming client
         //System.out.println( getRemoteSocketAddress() + " is now online!" );
         System.out.println( s + " is now connected." );
         
         DataOutputStream out = new DataOutputStream( s.getOutputStream() );
         
         clientList.put(s, out);

         /* create a new thread to handle the new client */
         new ServerThread(this,s);
      }
   }

   void sendToAll( String message ) {

      Iterator it = clientList.entrySet().iterator();

      /* Sending messages iteratively to all clients */
      while (it.hasNext()) {
         Map.Entry entry = (Map.Entry) it.next();
         DataOutputStream out = (DataOutputStream)entry.getValue();
         try {
            out.writeUTF( message );
         } catch( IOException e ) {
            System.out.println(e); 
         }
      }
   }

   void removeConnection( Socket s ) {  

      System.out.println( "Removing connection to " + s );
      // Remove it from clientList Hashmap
      clientList.remove(s);

      try {
         s.close();
      } catch( IOException e ) {
         System.out.println( "Error closing connection for " + s );
         e.printStackTrace();
      }
   }

   public static void main(String [] args) throws Exception {
         int serverPort = Integer.parseInt(args[0]);
         new Server(serverPort);
   }

}