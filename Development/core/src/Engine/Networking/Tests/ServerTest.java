////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: ServerTest
////////////////////////////////////////////////

package Engine.Networking.Tests;

import Engine.Networking.Networking;
import Engine.Networking.NetworkingEventListener;
import Engine.Networking.URLEncoder;

public class ServerTest {
   public static void main(String[] args) throws Exception {
      System.out.println("Networking - Server Test.");
      
      String localIp = Networking.getLocalIP();
      System.out.println("Local IP: "+localIp);

      Networking.startServer();
      
      String encodedIp = URLEncoder.encodeIP(localIp);
      
      System.out.println("Server ID: "+encodedIp);
      System.out.println("Started server.");

      Networking.serverSetTCPCallback(new ServerTestListener());
      
      while (true){
         //Keep the program running until we manually quit it.
      }
      
      //server.stop();
      
   }

   private static class ServerTestListener implements NetworkingEventListener {
      @Override
      public void get(Object msg) {
         System.out.println(msg.toString());
      }
   }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////