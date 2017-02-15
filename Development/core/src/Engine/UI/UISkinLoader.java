////////////////////////////////////////////////
// ORBITAL SPACE
// A game by Chris Dalke
////////////////////////////////////////////////
// Loads UI Skin and other vector graphics
////////////////////////////////////////////////

//Part of the Vector package
package Engine.UI;

import Engine.System.Logging.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;

////////////////////////////////////////////////

public class UISkinLoader {

    ////////////////////////////////////////////////
    // Text alignment constants
    ////////////////////////////////////////////////

    public static final int TEXT_ALIGN_LEFT = 1;
    public static final int TEXT_ALIGN_RIGHT = 2;
    public static final int TEXT_ALIGN_CENTER = 3;

    ////////////////////////////////////////////////
    // Internal rendering variables
    ////////////////////////////////////////////////

    private static boolean isReady;
    public static int scaling = 1;
    private static ShapeRenderer shapeRenderer;
    public static Skin skin;

    public static void init(){
        try {

            //VisUI.load(new FileHandle("Resources/ui/skin/custom/x1/uiskin.json"));
            //VisUI.load(new FileHandle("Resources/ui/skin/tinted-custom/x1/tinted.json"));
            VisUI.setDefaultTitleAlign(Align.center);

            //Add custom textures for skin
            //TODO: Merge these together with other textures
            //Texture windowTex = TextureLoader.load("Resources/Textures/windowPatch.png");
            //VisUI.getSkin().add("window-custom", new NinePatch(windowTex.getRegion(),10,10,10,10));

            skin = new Skin(Gdx.files.internal("Assets/Skins/flat/skin/skin.json"));

            Logger.log("Loaded UI Skinning system!");
            isReady = true;
        } catch (Exception e){
            Logger.log("Unable to load UI Skinning system!");
            e.printStackTrace();
            isReady = false;
        }
    }

    public static void dispose(){
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////