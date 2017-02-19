////////////////////////////////////////////////
// Vaultland
// Chris Dalke
////////////////////////////////////////////////
// Module: Text
////////////////////////////////////////////////

package Engine.Renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Text {


    public static BitmapFont font;
    public static BitmapFont font2;
    private static BitmapFont font3;
    private static BitmapFont currentFont;

    public static void init(){

        //Generate the font from Freetype files
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Assets/Fonts/geo_sans_light/GeosansLight.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        parameter.size = 22;
        font3 = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(Gdx.files.internal("Assets/Fonts/courier_new/courier_new.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 16;
        parameter2.color = Color.WHITE;
        font2 = generator2.generateFont(parameter2);
        generator2.dispose(); // don't forget to dispose to avoid memory leaks!

        /*
        FreeTypeFontGenerator generator3 = new FreeTypeFontGenerator(Gdx.files.internal("Assets/Fonts/moist/Moist.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter3 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter3.size = 48;
        parameter3.color = Color.WHITE;
        font3 = generator3.generateFont(parameter3);
        generator3.dispose(); // don't forget to dispose to avoid memory leaks!
        */


        currentFont = font;
    }

    public static void setFont(int fontId){
        switch (fontId){
            case 1:
                currentFont = font;
                break;
            case 2:
                currentFont = font2;
                break;
            case 3:
                currentFont = font3;
                break;
        }
    }

    public static void draw(int x, int y, String text){

        currentFont.draw(Renderer.getBatch(), text,x,y);

    }

    public static void setColor(Color color){
        currentFont.setColor(color);
    }

    public static void draw(float x, float y, String text){

        currentFont.draw(Renderer.getBatch(), text,x,y);

    }
    public static void centerDraw(double x, double y, String text) {
        GlyphLayout layout = new GlyphLayout(currentFont, text);

        double cx = x - (layout.width) / 2;
        double cy = y + (layout.height) / 2;
        currentFont.draw(Renderer.getBatch(), text, (int) cx, (int) cy);
    }


    public static float getTextWidth(String text){
        GlyphLayout layout = new GlyphLayout(currentFont, text);
        return layout.width;
    }
    public static float getTextHeight(String text){
        GlyphLayout layout = new GlyphLayout(currentFont, text);
        return layout.height;
    }

    public static void dispose(){
        currentFont = null;
        font.dispose();
        font2.dispose();
        font3.dispose();
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////