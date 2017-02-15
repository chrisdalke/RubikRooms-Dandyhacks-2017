////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: GameObject2d
////////////////////////////////////////////////

package Engine.Game.Entity;

import Engine.Renderer.Renderer;
import Engine.Renderer.Textures.Texture;

public class GameObject2d {
   public float x;
   public float y;
   public float width;
   public float height;
   public float angle;
   public Texture tex;
   public int frame;
   public float vx;
   public float vy;
   public boolean deleteFlag;
   public int layer;
   
   public GameObject2d(Texture tex) {
      this.tex = tex;
      
   }
   
   public void init(){
      
   }
   
   public void update(){
      x += vx;
      y += vy;
   }
   
   public void delete(){
      deleteFlag = true;
   }
   
   public void render(){
      if (tex != null) {
         Renderer.draw(tex.getRegion(frame), x - (width / 2), y - (height / 2), width, height, angle);
      }
   }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////