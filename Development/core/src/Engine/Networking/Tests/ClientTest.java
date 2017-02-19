////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: ClientTest
////////////////////////////////////////////////

package Engine.Networking.Tests;

import Engine.Networking.Networking;
import Engine.Networking.NetworkingEventListener;

import java.util.Scanner;

public class ClientTest {
   public static void main(String[] args) throws Exception {
      System.out.println("Networking - Client (Controller) Test.");
      
      System.out.print("Please enter Server ID:");
      
      Scanner in = new Scanner(System.in);
      
      System.out.print("Connecting to server...");

      Networking.startClient(in.nextLine());
      Networking.clientSetTCPCallback(new ClientTestListener());

      while (Networking.clientRunning){
         String input = in.nextLine();
         Networking.clientSendTCP(input);
      }
      
   }

   private static class ClientTestListener implements NetworkingEventListener {
      @Override
      public void get(Object msg) {
         System.out.println(msg.toString());
      }
   }
   
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////