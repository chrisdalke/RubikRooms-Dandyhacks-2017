////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: Base64Encoder
////////////////////////////////////////////////

package Engine.Networking;

import org.hashids.Hashids;

public class URLEncoder {
   
   private static Hashids hashids = new Hashids("RubikRoom");
   
   public static String encodeURL(String input){
      //Split the input into an integer array. Must be in the format ip:port
      String[] splitInput = input.split(":");
      String[] splitIp = splitInput[0].split("\\.");
      long port = Long.parseLong(splitInput[1]);
      
      long[] ipParts = new long[splitIp.length+1];
      for (int i = 0; i < splitIp.length; i++){
         ipParts[i]=Long.parseLong(splitIp[i]);
      }
      ipParts[splitIp.length] = port;
      
      String id = hashids.encode(ipParts);
      return id;
   }
   
   public static String decodeURL(String input){
      long[] numbers = hashids.decode(input);
      String output="";
      for (int i = 0; i < numbers.length; i++){
         if (i == numbers.length-1){
            output += Long.toString(numbers[i]);
         } else if (i == numbers.length-2){
            output += Long.toString(numbers[i]) + ":";
         } else {
            output += Long.toString(numbers[i]) + ".";
         }
      }
      return output;
   }
   
   public static void main(String[] args){
      //Test the base64 encoder
      System.out.println("Testing URL Encoder:");
      
      String URL = "127.0.0.1:65525";
      
      System.out.println("Unencoded URL: "+URL);
      
      String encodedURL =encodeURL(URL);
      
      System.out.println("Encoded URL: "+encodedURL);
      
      String decodedURL = decodeURL(encodedURL);
      
      System.out.println("Decoded URL: "+decodedURL);
      
      System.out.println("Done test.");
      
   }
   
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////