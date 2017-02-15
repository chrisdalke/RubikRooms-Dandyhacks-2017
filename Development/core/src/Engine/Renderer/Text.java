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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Text {


    private static BitmapFont font;

    public static void init(){

        //Generate the font from Freetype files
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Assets/Fonts/geo_sans_light/GeosansLight.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 28;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

    }

    public static void draw(int x, int y, String text){

        font.draw(Renderer.getBatch(), text,x,y);
    }

    public static void draw(double x, double y, String text){

        font.draw(Renderer.getBatch(), text,(int)x,(int)y);
    }

    public static void setColor(Color color){
        font.setColor(color);
    }


    public static void centerDraw(double x, double y, String text){

        double cx = x - (font.getSpaceWidth() * (text.length()/2));
        //System.out.println(cx+","+x);
        double cy = (y - (font.getLineHeight()/2));

        font.draw(Renderer.getBatch(), text,(int)cx,(int)cy);

    }

    public static void dispose(){

        font.dispose();
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////