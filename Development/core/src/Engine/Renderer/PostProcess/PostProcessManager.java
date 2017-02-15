////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: PostProcessManager
////////////////////////////////////////////////

package Engine.Renderer.PostProcess;

import Engine.System.Logging.Logger;
import Engine.System.System;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.*;
import com.bitfire.postprocessing.filters.Combine;
import com.bitfire.postprocessing.filters.CrtScreen;
import com.bitfire.postprocessing.filters.RadialBlur;
import com.bitfire.utils.ShaderLoader;

public class PostProcessManager {

    private static PostProcessor postProcessor;
    private static boolean uiMode;

    public static void setUIMode(boolean on){
        uiMode = on;
    }

    public static boolean getUIMode(){
        return uiMode;
    }


    public static void init() {
        ShaderLoader.BasePath = "Assets/Shaders/post/shaders/";
        postProcessor = new PostProcessor( false, false, System.isDesktop());
        /*

        Bloom bloom = new Bloom( (int)(Display.getWidth() * 0.25f), (int)(Display.getHeight() * 0.25f) );
        postProcessor.addEffect( bloom );
        //postProcessor.addEffect( bloom );
        //postProcessor.addEffect(vignette);
        LensFlare2 lensFlare2 = new LensFlare2((int)(Renderer.getWidth() * 0.25f), (int)(Renderer.getHeight() * 0.25f));
        lensFlare2.setLensColorTexture(new Texture(new FileHandle("shaders/post/lenscolor.png")));
        postProcessor.addEffect(lensFlare2);
*/

        int vpW = Gdx.graphics.getWidth();
        int vpH = Gdx.graphics.getHeight();

        Bloom bloom = new Bloom( (int)(vpW * 0.25f), (int)(vpH * 0.25f) );
        bloom.setBloomIntesity(0.7f);
        bloom.setThreshold(0.4f);
        bloom.setBlurPasses(4);



        Curvature curvature = new Curvature();
        Zoomer zoomer = new Zoomer( vpW, vpH, System.isDesktop() ? RadialBlur.Quality.VeryHigh : RadialBlur.Quality.Low );
        int effects = CrtScreen.Effect.TweakContrast.v | CrtScreen.Effect.PhosphorVibrance.v | CrtScreen.Effect.Scanlines.v | CrtScreen.Effect.Tint.v;
        CrtMonitor crt = new CrtMonitor( vpW, vpH, false, false, CrtScreen.RgbMode.ChromaticAberrations, effects );
        Combine combine = crt.getCombinePass();

        combine.setSource1Intensity( 0f );
        combine.setSource2Intensity( 1f );
        combine.setSource1Saturation( 0f );
        combine.setSource2Saturation( 1f );

        Vignette vignette = new Vignette( vpW, vpH, false );
        vignette.setIntensity(0.25f);

        LensFlare2 lensFlare2 = new LensFlare2(vpW,vpH);
        lensFlare2.setLensColorTexture(new Texture(new FileHandle("Assets/Shaders/post/lenscolor.png")));


        // add them to the postprocessor
        //postProcessor.addEffect( curvature );
        //postProcessor.addEffect( zoomer );
        postProcessor.addEffect( vignette );
        //postProcessor.addEffect( crt );
        postProcessor.addEffect(new Fxaa(vpW,vpH));
        postProcessor.addEffect( bloom );
        //postProcessor.addEffect(lensFlare2);

        MotionBlur motionBlur = new MotionBlur();
        //postProcessor.addEffect(motionBlur);

        setUIMode(true);

        Logger.log("Initialized Post Processor...");

    }

    public static void start(){
        postProcessor.capture();
    }

    public static void end(){
        postProcessor.render();
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////