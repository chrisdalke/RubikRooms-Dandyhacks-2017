////////////////////////////////////////////////
// ORBITAL SPACE
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: UIElement
////////////////////////////////////////////////

package Engine.UI.Elements;

import Engine.UI.UISkinLoader;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class UIElement {

    protected Actor actor;

    public UIElement() {
    }

    //Is called on all elements when a stage is switched to
    public void reset(){

    }

    public abstract void addChild(UIElement child);

    public Actor getActor(){
        return actor;
    }

    public void setSize(int w, int h){
        actor.setSize(w* UISkinLoader.scaling,h* UISkinLoader.scaling);
    }

    public void setPos(int x, int y){
        actor.setPosition(x* UISkinLoader.scaling,y* UISkinLoader.scaling);
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////