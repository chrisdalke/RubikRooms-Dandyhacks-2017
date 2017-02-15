////////////////////////////////////////////////
// Vaultland
// Chris Dalke
////////////////////////////////////////////////
// Module: Shapes
////////////////////////////////////////////////

package Engine.Renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Shapes {

    private static ShapeRenderer shapeRenderer;
    
    public static void init(){
        shapeRenderer = new ShapeRenderer();
    }
    
    public static void setColor(int r, int g, int b, int a){
        shapeRenderer.setColor(r/255.0f,g/255.0f,b/255.0f,a/255.0f);
    }
    
    public static void drawBox(float x, float y, float width, float height){
        shapeRenderer.rect(x, y, width, height);
    }
    
    public static void drawSphere(float x, float y, float radius){
        
        shapeRenderer.circle(x,y,radius,32);
    }
    
    //p1 p2 p3
    //p4 .  p5
    //p6 p7 p8
    public static void gradientShadow(float x, float y, float width, float height, boolean p1,boolean p2,boolean p3,boolean p4){
        Color color1 = p1 ? Color.BLACK : Color.CLEAR;
        Color color2 = p2 ? Color.BLACK : Color.CLEAR;
        Color color3 = p3 ? Color.BLACK : Color.CLEAR;
        Color color4 = p4 ? Color.BLACK : Color.CLEAR;
        shapeRenderer.rect(x,y,width,height,color1,color2,color3,color4);
    }
    
    public static void begin(OrthographicCamera target){
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(target.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }
    
    
    public static void beginOutline(OrthographicCamera target){
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(target.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    }
    
    
    public static void end(){
        shapeRenderer.end();
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////

