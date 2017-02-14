////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: ClientTest
////////////////////////////////////////////////

package Engine.Networking.Tests;

import Engine.Networking.URLEncoder;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.Scanner;

public class ClientTest {
   public static void main(String[] args) throws Exception {
      System.out.println("Networking - Client (Controller) Test.");
      
      System.out.print("Please enter Server ID:");
      
      Scanner in = new Scanner(System.in);
      String ipPort = URLEncoder.decodeURL(in.nextLine());
      String ip = (ipPort.split(":"))[0];
      String port = (ipPort.split(":"))[1];
      
      System.out.print("Connecting to server...");
      
      Client client = new Client();
      client.start();
      client.connect(5000, ip, Integer.parseInt(port), Integer.parseInt(port)+1);
   
      client.addListener(new Listener() {
         public void received (Connection connection, Object object) {
            if (object instanceof String) {
               String response = (String)object;
               System.out.println(response);
            }
         }
      });
      
      while (true){
         String input = in.nextLine();
         client.sendTCP(input);
      }
      
   }
   
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////