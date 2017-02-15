////////////////////////////////////////////////
// Frame Engine
// Chris Dalke
////////////////////////////////////////////////
// Module: FrameBuffer
////////////////////////////////////////////////

package Engine.Renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FrameBuffer {

    private com.badlogic.gdx.graphics.glutils.FrameBuffer frameBuffer;
    private int width;
    private int height;
    public FrameBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        frameBuffer = new com.badlogic.gdx.graphics.glutils.FrameBuffer(Pixmap.Format.RGB888,width,height,true);
    }

    public void start() {

        frameBuffer.begin();

        Gdx.gl.glViewport(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
        Gdx.gl.glClearColor(0.5f,0.5f,0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glDepthFunc(GL20.GL_GREATER);

    }

    public void end(){

        frameBuffer.end();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public TextureRegion getRegion(){

        Texture texture = frameBuffer.getColorBufferTexture();
        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.flip(false, true);
        return textureRegion;
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////