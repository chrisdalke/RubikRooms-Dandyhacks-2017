////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: ServerTest
////////////////////////////////////////////////

package Engine.Networking.Tests;

import Engine.Networking.Networking;
import Engine.Networking.URLEncoder;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ServerTest {
   public static void main(String[] args) throws Exception {
      System.out.println("Networking - Server Test.");
      
      String localIp = Networking.getLocalIP();
      System.out.println("Local IP: "+localIp);
   
      Server server = new Server();
      server.start();
      server.bind(54555, 54556);
      String tcpPort = "54555";
      
      String encodedIp = URLEncoder.encodeURL(localIp+":"+tcpPort);
      
      System.out.println("Server ID: "+encodedIp);
      
      System.out.print("Starting server...");
   
      server.addListener(new Listener() {
         public void received (Connection connection, Object object) {
            if (object instanceof String) {
               String request = (String)object;
               System.out.println(request);
            
               String response = "Hello World!";
               connection.sendTCP(request);
            }
         }
      });
      
      while (true){
         //Keep the program running until we manually quit it.
      }
      
      //server.stop();
      
   }
   
   
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////