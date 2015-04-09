import java.net.*;
import java.io.*;
import java.util.*;

public class ServerThread extends Thread {
   private Server server;
   private Socket socket;

   public ServerThread(Server server, Socket socket) {
      this.server = server;
      this.socket = socket;
      /* Starts the new thread of this object by calling the run method */
      start();
   }

   /* overriding the run method of the thread class */
   public void run() { 
      try {
         int flag=0;
         String uname;
         DataInputStream in = new DataInputStream(socket.getInputStream());

         while (true) {
            if (flag == 0) {                          //first reply from client(the username)
               uname = (in.readUTF());
               flag++;
            }
            else {
               String message = (in.readUTF());
               server.sendToAll(message);
            }
         }
      } catch(SocketTimeoutException s) {
         System.out.println("Connection timed out!");
      } catch(IOException e) {
         e.printStackTrace();
      } finally {
         server.removeConnection(socket);
      }
   }
}