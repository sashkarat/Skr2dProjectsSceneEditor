package org.skr.Skr2dProjectsSceneEditor;

import java.awt.*;

/**
 * Created by rat on 08.08.14.
 */
public class SkrSwingUtils {
    public static void setGuiElementEnable(Container c, boolean state) {

        Component [] cl = c.getComponents();

        for ( int i = 0; i < cl.length; i++) {

            if ( cl[i] instanceof Container) {
                setGuiElementEnable((Container) cl[i], state);
            } else {
                cl[i].setEnabled( state );
            }

        }

        c.setEnabled( state );
    }
}
