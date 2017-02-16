////////////////////////////////////////////////
// Vaultland
// Chris Dalke
////////////////////////////////////////////////
// Module: Renderer
////////////////////////////////////////////////

package Engine.Renderer;

import Engine.Display.Display;
import Engine.Renderer.PostProcess.PostProcessManager;
import Engine.System.Config.Configuration;
import Engine.System.Logging.Logger;
import Engine.UI.UISkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;

public class Renderer {

    //Helper class for the Game to render stuff
    private static SpriteBatch batch;
    private static StretchViewport viewport;
    public static OrthographicCamera cameraUI;

    private static ArrayList<Matrix4> matrixStack;
    private static Matrix4 currentMatrix;


    public static SpriteBatch getBatch(){
        return batch;
    }

    public static void init(Configuration config){
        batch = new SpriteBatch();
        cameraUI = new OrthographicCamera((float)Display.getWidth(),(float)Display.getHeight());
        cameraUI.translate((float)Display.getWidth()/2,(float)Display.getHeight()/2);
        Text.init();
        Shapes.init();
        UISkinLoader.init();
        PostProcessManager.init();

        Logger.log("Renderer successfully initialized!");
    }

    public static void startUI(){
        cameraUI.update();
        batch.setProjectionMatrix(cameraUI.combined);
        batch.begin();
    }

    public static void endUI(){
        batch.end();
    }

    ////////////////////////////////////////////////
    // 2d Rendering: Clipping
    ////////////////////////////////////////////////


    public static void setClipping(float x, float y, float width, float height){
        Rectangle scissor = new Rectangle();
        Rectangle clipBounds = new Rectangle(x,y,width,height);
        ScissorStack.calculateScissors(cameraUI, 0, 0, (float)Display.getWidth(), (float)Display.getHeight(), batch.getTransformMatrix(), clipBounds, scissor);
        ScissorStack.pushScissors(scissor);
    }

    public static void resetClipping(){
        ScissorStack.popScissors();
    }

    ////////////////////////////////////////////////
    // 2d Rendering: Coloring
    ////////////////////////////////////////////////

    public static void setColor(Color tint){
        batch.setColor(tint);
    }

    public static void resetColor(){
        batch.end();
        batch.begin();
        batch.setColor(Color.WHITE);
    }

    ////////////////////////////////////////////////
    // 2d Rendering: Blending
    ////////////////////////////////////////////////

    static int srcFunc;
    static int dstFunc;

    public static void setAdditive(){

        srcFunc = batch.getBlendSrcFunc();
        dstFunc = batch.getBlendDstFunc();
        batch.setBlendFunction(GL20.GL_ONE,GL20.GL_ONE);
    }

    public static void resetAdditive(){
        batch.setBlendFunction(srcFunc,dstFunc);

    }

    public static void setSubtractive(){

        batch.setBlendFunction(GL20.GL_ONE,GL20.GL_ONE);
    }

    public static void setInvert(){
        batch.setBlendFunction(GL20.GL_ONE_MINUS_DST_COLOR,GL20.GL_ONE_MINUS_SRC_COLOR);
    }


    public static void resetBlending(){
        batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void flush(){
        batch.flush();
    }


    public static void draw(TextureRegion tex, float x, float y, float width, float height, float angle){
        batch.draw(tex,x,y,width/2,height/2,width,height,1.0f,1.0f,angle);

    }

    public static void draw(TextureRegion tex, float x, float y, float width, float height){
        batch.draw(tex,x,y,width,height);
    }

    public static void dispose(){
        batch.dispose();
        Text.dispose();
        UISkinLoader.dispose();
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////