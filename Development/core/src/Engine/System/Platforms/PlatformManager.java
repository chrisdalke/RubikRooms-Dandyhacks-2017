////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: PlatformManager
////////////////////////////////////////////////

package Engine.System.Platforms;

public class PlatformManager {
   
   public static int DESKTOP = 0;
   public static int IOS = 1;
   public static int platform = DESKTOP;
   
   public static void setPlatform(int Platform){
      platform = Platform;
   }
   
   public static int getPlatform(){
      return platform;
   }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////