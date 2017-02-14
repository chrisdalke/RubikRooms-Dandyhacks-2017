////////////////////////////////////////////////
// Vaultland
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: Desktop
////////////////////////////////////////////////

package Engine.System.Platforms.Desktop;

import Engine.System.Main;
import com.badlogic.gdx.ApplicationAdapter;


public class Desktop extends ApplicationAdapter {

	@Override
	public void create () {
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