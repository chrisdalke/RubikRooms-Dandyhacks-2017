////////////////////////////////////////////////
// Vaultland
// Chris Dalke
////////////////////////////////////////////////
// Module: Shapes
////////////////////////////////////////////////

package Engine.Renderer;

import Engine.Renderer.Textures.Texture;
import Engine.Renderer.Textures.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Shapes {

    private static ShapeRenderer shapeRenderer;
    private static Texture pixelTex;

    public static void init(){
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        pixelTex = TextureLoader.load("Resources/Textures/pixel.png");
    }

    public static void begin(OrthographicCamera target){
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(target.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

    }
    public static void end(){
        shapeRenderer.end();
    }

    public static void drawBox(float x, float y, float width, float height, Color color){
        Renderer.setColor(color);
        Renderer.getBatch().draw(pixelTex.getRegion(), x, y, width, height);
        Renderer.resetColor();
    }

    public static void drawLine(float x1, float y1, float x2, float y2, float lineWidth) {
        float xdif = x2-x1;
        float ydif = y2-y1;
        float l2 = xdif*xdif+ydif*ydif;
        float invl = (float)(1/Math.sqrt(l2));
        //dif is vector with length linewidth from first to second vertex
        xdif*=invl*lineWidth;
        ydif*=invl*lineWidth;

        TextureRegion lineTexture = pixelTex.getRegion();
        float floatBits = Renderer.getBatch().getColor().toFloatBits();
        //draw quad with width of linewidth*2 through (x1, y1) and (x2, y2)
        float[] verts = new float[]{x1+ydif, y1-xdif, floatBits, lineTexture.getU(), lineTexture.getV(),
                x1-ydif, y1+xdif, floatBits, lineTexture.getU2(), lineTexture.getV(),
                x2-ydif, y2+xdif, floatBits, lineTexture.getU2(), lineTexture.getV2(),
                x2+ydif, y2-xdif, floatBits, lineTexture.getU(), lineTexture.getV2()};
        Renderer.getBatch().draw(lineTexture.getTexture(), verts, 0, 20);
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////