////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: IOS
////////////////////////////////////////////////

package Engine.System.Platforms.IOS;

import Engine.System.Main;
import Engine.System.Platforms.PlatformManager;
import com.badlogic.gdx.ApplicationAdapter;

public class IOS extends ApplicationAdapter {
    
    @Override
    public void create () {
        PlatformManager.setPlatform(PlatformManager.IOS);
        Main.init();
    }
    
    @Override
    public void render () {
        Main.render();
    }
    
    @Override
    public void dispose() {
        Main.dispose();
    }
    
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////