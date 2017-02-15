////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: OutlineCube
////////////////////////////////////////////////

package Game;

import Engine.Game.Entity.GameObject3d;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class OutlineCube extends GameObject3d {
   
   public OutlineCube() {
      super();
      
      ModelBuilder modelBuilder = new ModelBuilder();
      modelBuilder.begin();
      MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
      builder.setColor(Color.RED);
      builder.line(0.0f, 0.0f, -5.0f, 0.0f, 0.0f, 5.0f);
      builder.line(-5.0f, 0.0f, 0.0f, 5.0f, 0.0f, 0.0f);
      builder.line(0.0f, -5.0f, 0.0f, 0.0f, 5.0f, 0.0f);
      Model lineModel = modelBuilder.end();
      setModel(new ModelInstance(lineModel));
      
   }
   
   @Override
   public void init() {
      
   }
   
   @Override
   public void update() {
   }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////