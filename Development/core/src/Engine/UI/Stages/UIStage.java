////////////////////////////////////////////////
// ORBITAL SPACE
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: UIStage
////////////////////////////////////////////////

package Engine.UI.Stages;

import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class UIStage extends Stage {

    public UIStage() {
        super();
        init();
    }

    //Three stage-specific commands
    protected abstract void init();

    //Update at a fixed time step (separate from rendering)
    protected abstract void update();

    public void updateAll(){
        act();
        update();
    }

    protected abstract void render();

    public void renderAll(){
        render();
        draw();
    }

    public void dispose(){
        clear();
    }

    public void reset(){
        clear();
        init();
    }

    public void hide(){
        clear();
    }

    public void show(){
        reset();
    }

    /*
    public void addChild(UIElement child){
        addActor(child.getActor());
    }

    public void removeChild(UIElement child){
        child.getActor().remove();
    }

     */
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////