////////////////////////////////////////////////
// ORBITAL SPACE
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: UIModalWindow
////////////////////////////////////////////////

package Engine.UI.Elements;

import Engine.Display.Display;
import Engine.System.Multithreading.ParameterRunnable;
import Engine.UI.Stages.UIStageManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.*;

import static Engine.UI.UISkinLoader.skin;

public class UIModalWindow {
    //Creates some modal dialogs

    public static void clearModals(){
        modalVisible = false;
    }

    public static void createYesNoDialog(Stage stage, String title, String description, final Runnable yesAction, int style){
        if (!modalVisible) {
            modalVisible = true;
            final Window modalWindow = new Window(title,skin);
            modalWindow.setModal(true);
            modalWindow.setMovable(true);
            modalWindow.setPosition((float) Display.getWidth()/2,(float)Display.getHeight()/2);


            final TextButton yesButton;
            final TextButton noButton;

            if (style == 2){
                yesButton = new TextButton("Ok",skin);
                noButton = new TextButton("Cancel",skin);
            } else {
                yesButton = new TextButton("Yes",skin);
                noButton = new TextButton("No",skin);
            }

            yesButton.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    yesAction.run();
                    modalVisible = false;
                    modalWindow.remove();
                }
            });

            noButton.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    modalVisible = false;
                    modalWindow.remove();
                }
            });


            Table modalTable = new Table();
            Table yesNoTable = new Table();

            Label modalLabel = new Label(description,skin);
            modalTable.add(modalLabel).expandX().pad(10);
            modalTable.row();

            yesNoTable.add(yesButton).pad(10);
            yesNoTable.add(noButton).pad(10);
            modalTable.add(yesNoTable).expand();
            modalWindow.add(modalTable);
            modalTable.pad(20.0f);
            modalTable.layout();
            modalWindow.pack();

            stage.addActor(modalWindow);
        }
    }

    private static boolean modalVisible = false;

    public static void createAlert(String title, String description){
        if (!modalVisible) {
            Stage stage = UIStageManager.getCurrentStage();
            modalVisible = true;
            final VisWindow modalWindow = new VisWindow(title,"dialog");
            modalWindow.setModal(true);
            modalWindow.setCenterOnAdd(true);

            final VisTextButton closeButton = new VisTextButton("X");
            closeButton.setHeight(modalWindow.getPadTop() - 1);
            //modalWindow.getTitleTable().add(closeButton);
            modalWindow.getTitleTable().pad(10,0,0,0);
            modalWindow.padTop(30);

            final VisTextButton noButton = new VisTextButton("Close");

            noButton.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    modalVisible = false;
                    modalWindow.remove();
                }
            });
            closeButton.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    modalVisible = false;
                    modalWindow.remove();
                }
            });

            VisTable modalTable = new VisTable();
            VisLabel modalLabel = new VisLabel(description);
            modalTable.add(modalLabel).pad(10);
            modalTable.row();
            modalTable.add(noButton).pad(10);
            modalWindow.add(modalTable);
            modalTable.pad(20.0f);
            modalWindow.pack();

            stage.addActor(modalWindow);

        }
    }

    //Prompts the user to enter a certain string
    public static void createStringPrompt(String title, String description, ParameterRunnable runnable){

        if (!modalVisible) {
            Stage stage = UIStageManager.getCurrentStage();
            modalVisible = true;
            final VisWindow modalWindow = new VisWindow(title,"dialog");
            modalWindow.setModal(true);
            modalWindow.setCenterOnAdd(true);

            final VisTextButton closeButton = new VisTextButton("X");
            closeButton.setHeight(modalWindow.getPadTop() - 1);
            modalWindow.getTitleTable().add(closeButton);
            modalWindow.getTitleTable().pad(10,0,0,0);
            modalWindow.padTop(30);

            final VisTextField textField = new VisTextField();
            final VisTextButton acceptButton = new VisTextButton("Accept");

            acceptButton.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {

                    modalVisible = false;
                    modalWindow.remove();

                    //Run the runnable which will return the result of our modal window
                    runnable.setParam(textField.getText());
                    runnable.run();

                }
            });

            VisTable modalTable = new VisTable();
            VisLabel modalLabel = new VisLabel(description);
            modalTable.add(modalLabel).pad(10);
            modalTable.row();
            modalTable.add(textField).pad(10).fillX();
            modalTable.add(acceptButton).pad(10);
            modalWindow.add(modalTable);
            modalTable.pad(20.0f);
            modalWindow.pack();

            stage.addActor(modalWindow);

        }
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////