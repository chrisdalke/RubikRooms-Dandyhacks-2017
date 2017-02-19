////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: Networking
////////////////////////////////////////////////

package Engine.Networking;

import Engine.System.Config.Configuration;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

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

   ////////////////////////////////////////////////
   // Server Code
   ////////////////////////////////////////////////

   public static final int TCP_PORT = 54555;
   public static final int UDP_PORT = 54556;

   public static boolean serverRunning;
   public static Server serverSession;
   public static NetworkingEventListener serverListener;
   public static String serverEncodedUrl;

   public static String getGameCode(){
      if (serverEncodedUrl == null) {
         serverEncodedUrl = URLEncoder.encodeIP(Networking.getLocalIP());
      }
      return serverEncodedUrl;
   }

   public static void startServer(){
      try {
         serverSession = new Server();
         serverSession.start();
         serverSession.bind(TCP_PORT, UDP_PORT);

         String localIp = Networking.getLocalIP();

         serverSession.addListener(new Listener() {
            public void received (Connection connection, Object object) {
               if (serverListener != null) {
                  serverListener.get(object);
               }
            }
         });

         serverRunning = true;
      } catch (Exception e){
         e.printStackTrace();
         stopServer();
      }

   }

   public static void updateServer(){

   }

   public static void stopServer(){
      serverRunning = false;
      try {
         if (serverSession != null) {
            serverSession.dispose();
         }
      } catch (IOException e){
         e.printStackTrace();
      }
   }

   public static void serverSendTCP(Object message){
      if (serverRunning) {
         serverSession.sendToAllTCP(message);
      }
   }

   public static void serverSetTCPCallback(NetworkingEventListener listener){
      serverListener = listener;
   }


   ////////////////////////////////////////////////
   // Client Code
   ////////////////////////////////////////////////

   public static boolean clientRunning;
   public static Client clientSession;
   public static NetworkingEventListener clientListener;

   public static boolean getClientRunning(){
      if (clientRunning){
         if (!clientSession.isConnected()){
            stopClient();
            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public static void startClient(String encodedUrl){
      try {

         //Decode the URL and get ip and port
         String ipPort = URLEncoder.decodeIP(encodedUrl);
         if (!URLEncoder.validate(ipPort)){
            throw new IOException("Couldn't connect to server (Invalid IP)");
         }
         //String ip = (ipPort.split(":"))[0];
         //String port = (ipPort.split(":"))[1];
         //int portTCP = Integer.parseInt(port);
         //int portUDP = Integer.parseInt(port)+1;

         //UDP port is always one port above tcp port
         //try to connect

         clientSession = new Client();
         clientSession.start();
         clientSession.connect(5000, ipPort, TCP_PORT, UDP_PORT);

         //Wait and see if the client is connecting
         await().atMost(5, SECONDS).until(() -> clientSession.isConnected());

         if (!clientSession.isConnected()){
            throw new IOException("Couldn't connect to server (timeout)");
         }

         clientSession.addListener(new Listener() {
            public void received (Connection connection, Object object) {
               if (clientListener != null){
                  clientListener.get(object);
               }
            }
         });

         clientRunning = true;
      } catch (IOException e) {
         e.printStackTrace();
         stopClient();
      }

   }

   public static void updateClient(){

   }

   public static void stopClient(){
      clientRunning = false;
      try {
         if (clientSession != null) {
            clientSession.dispose();
         }
      } catch (IOException e){
         e.printStackTrace();
      }
   }

   public static void clientSendTCP(Object message){
      if (clientRunning) {
         clientSession.sendTCP(message);
      }
   }

   public static void clientSetTCPCallback(NetworkingEventListener listener){
      clientListener = listener;
   }

   public static Server getServerSession() {
      return serverSession;
   }

   public static Client getClientSession() {
      return clientSession;
   }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////