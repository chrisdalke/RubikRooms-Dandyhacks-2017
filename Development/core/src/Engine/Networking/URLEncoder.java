////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: Base64Encoder
////////////////////////////////////////////////

package Engine.Networking;

import org.hashids.Hashids;

import java.util.regex.Pattern;

public class URLEncoder {
   
   private static Hashids hashids = new Hashids("RubikRoom");

   private static final Pattern PATTERN = Pattern.compile(
           "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

   public static boolean validate(final String ip) {
      return PATTERN.matcher(ip).matches();
   }

   public static String encodeIP(String input){
      String[] splitIp = input.split("\\.");
      long[] ipParts = new long[splitIp.length];
      for (int i = 0; i < splitIp.length; i++){
         ipParts[i]=Long.parseLong(splitIp[i]);
      }
      String id = hashids.encode(ipParts);
      return id;
   }

   public static String decodeIP(String input){
      long[] numbers = hashids.decode(input);
      String output="";
      for (int i = 0; i < numbers.length; i++){
         if (i == numbers.length-1){
            output += Long.toString(numbers[i]);
         } else {
            output += Long.toString(numbers[i]) + ".";
         }
      }
      return output;
   }

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

      System.out.println("Testing IP Encoder:");

      String IP = "127.0.0.1";

      System.out.println("Unencoded IP: "+IP);

      String encodedIP =encodeIP(IP);

      System.out.println("Encoded IP: "+encodedIP);

      String decodedIP = decodeIP(encodedIP);

      System.out.println("Decoded IP: "+decodedIP);

      System.out.println("Done test.");
   }
   
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////