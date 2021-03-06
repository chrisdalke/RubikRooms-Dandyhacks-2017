////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: MenuInstance
////////////////////////////////////////////////

package Game.Instances;

import Engine.Display.Display;
import Engine.Game.Instance.AbstractGameInstance;
import Engine.Renderer.Renderer;
import Engine.Renderer.Textures.Texture;
import Engine.Renderer.Textures.TextureLoader;
import Engine.System.Timer.TimerManager;

public class MenuInstance extends AbstractGameInstance {

    Texture bg1;
    Texture bg2;
    Texture title;

    Texture[] cubeTextures;


    @Override
    public void init() {
        super.init();

        bg1 = TextureLoader.load("Assets/Textures/bg_layer1.png");
        bg2 = TextureLoader.load("Assets/Textures/bg_layer2.png");
        title = TextureLoader.load("Assets/Textures/title.png");

        cubeTextures = new Texture[8];
        for (int i = 1; i <= 8; i++){
            cubeTextures[i-1] = TextureLoader.load("Assets/Textures/bg_cube"+i+".png");
        }
    }

    @Override
    public void render() {
        Renderer.startUI();
        Renderer.draw(bg2.getRegion(),0,0,(float) Display.getWidth(),(float)Display.getHeight());

        for (int i = 1; i <= 8; i++){
            Renderer.draw(cubeTextures[i-1].getRegion(),0,-40+(float)Math.sin((TimerManager.getTime() + (i*2000))/(2000.0f))*40.0f,(float) Display.getWidth(),(float)Display.getHeight());
        }
        //Renderer.draw(title.getRegion(),0,0,(float) Display.getWidth(),(float)Display.getHeight());

        Renderer.endUI();

    }

    @Override
    public void dispose() {

    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////