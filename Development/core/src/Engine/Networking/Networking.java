////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: Networking
////////////////////////////////////////////////

package Engine.Networking;

import Engine.System.Config.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Networking {
   public static void init(Configuration config){
   }
   
   public static void update(){
      
   }
   
   public static void dispose(){
      
   }
   
   public static String getLocalIP(){
      try {
         InetAddress ipAddr = InetAddress.getLocalHost();
         return (ipAddr.getHostAddress());
      } catch (UnknownHostException ex) {
         ex.printStackTrace();
      }
      return null;
   }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////