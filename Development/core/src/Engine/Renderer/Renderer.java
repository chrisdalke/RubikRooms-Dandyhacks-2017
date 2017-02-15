////////////////////////////////////////////////
// Vaultland
// Chris Dalke
////////////////////////////////////////////////
// Module: Renderer
////////////////////////////////////////////////

package Engine.Renderer;

import Engine.Display.Display;
import Engine.System.Config.Configuration;
import Engine.System.Logging.Logger;
import Engine.UI.UISkinLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
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

    private static FrameBuffer frameBuffer;

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
    
        frameBuffer = new FrameBuffer(Pixmap.Format.RGB888,320,240,false);
    
        matrixStack = new ArrayList<Matrix4>();

        Logger.log("Renderer successfully initialized!");
    }

    public static void displayDebug(){

    }
    
    public static void startFrameBuffer(){
        
        frameBuffer.begin();
    
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glDepthFunc(GL20.GL_GREATER);
    }
    
    public static void endFrameBuffer(){
        frameBuffer.end();
    }
    
    public static TextureRegion getFrameBufferTexture(){
        Texture texture = frameBuffer.getColorBufferTexture();
        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.flip(false, true);
        return textureRegion;
    }

    public static void pushMatrix(Matrix4 newMatrix){
        matrixStack.add(newMatrix);

        //apply the matrix stack
        applyMatrixStack();
    }

    public static void popMatrix(){
        if (matrixStack.size() > 1) {
            matrixStack.remove(matrixStack.size() - 1);
        } //we can't remove the matrix if its the last one

        applyMatrixStack();
    }

    public static Matrix4 getMatrix(){
        Matrix4 combined = new Matrix4().idt();
        for (int i = 0; i < matrixStack.size(); i++){
            combined.mul(matrixStack.get(i));
        }
        return combined;
    }

    public static void resetMatrixStack(){
        matrixStack.clear();
    }

    public static void applyMatrixStack(){
        //multiplies all the matrixes together and applies them
        Matrix4 combined = new Matrix4().idt();
        for (int i = 0; i < matrixStack.size(); i++){
            combined.mul(matrixStack.get(i));
        }
        batch.setProjectionMatrix(combined);
    }
    

    public static void startUI(){
        cameraUI.update();
        resetMatrixStack();
        pushMatrix(cameraUI.combined);
        batch.begin();

    }

    public static void setClipping(float x, float y, float width, float height){
        Rectangle scissor = new Rectangle();
        Rectangle clipBounds = new Rectangle(x,y,width,height);
        ScissorStack.calculateScissors(cameraUI, 0, 0, (float)Display.getWidth(), (float)Display.getHeight(), batch.getTransformMatrix(), clipBounds, scissor);
        ScissorStack.pushScissors(scissor);

    }

    public static void resetClipping(){
        ScissorStack.popScissors();

    }

    //Offsets the batch rendering by an x and y position
    public static void setOffset(float x, float y){
        batch.setTransformMatrix(batch.getTransformMatrix().translate(x,y,0));

    }

    public static void endUI(){
        batch.end();
    }


    public static void setColor(Color tint){
        Matrix4 temp = batch.getTransformMatrix();
        batch.end();
        batch.setTransformMatrix(temp);
        batch.begin();
        batch.setColor(tint);
    }
    public static void resetColor(){
        setColor(new Color(1,1,1,1));
    }

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