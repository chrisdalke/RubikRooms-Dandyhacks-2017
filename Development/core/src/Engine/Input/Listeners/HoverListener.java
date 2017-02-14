////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: HoverListener
////////////////////////////////////////////////

package Engine.Input.Listeners;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public abstract class HoverListener extends ClickListener {

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        super.enter(event, x, y, pointer, fromActor);
        enter();
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        super.exit(event, x, y, pointer, toActor);
        exit();
    }


    public abstract void enter();
    public abstract void exit();
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////