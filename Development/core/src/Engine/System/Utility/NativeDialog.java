////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: NativeDialog
////////////////////////////////////////////////

package Engine.System.Utility;

import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;

public class NativeDialog {
    public static GDXDialogs dialogs;

    public static void init(){
        dialogs = GDXDialogsSystem.install();
    }

    //Displays a native modal dialog
    public static void displayModal(String title, String description){
        GDXButtonDialog bDialog = dialogs.newDialog(GDXButtonDialog.class);
        bDialog.setTitle(title);
        bDialog.setMessage(description);

        bDialog.setClickListener(new ButtonClickListener() {
            @Override
            public void click(int button) {
                // handle button click here
            }
        });

        bDialog.addButton("Okay");
        bDialog.build().show();
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////