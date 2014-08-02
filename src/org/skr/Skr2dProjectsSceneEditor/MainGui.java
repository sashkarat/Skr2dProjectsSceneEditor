package org.skr.Skr2dProjectsSceneEditor;

import javax.swing.*;

/**
 * Created by rat on 02.08.14.
 */
public class MainGui extends JFrame {


    MainGui() {

        pack();
        setSize(1280, 800);

    }

    //======================= main ================================

    public static void main(String [] args) {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                MainGui instance = new MainGui();
                instance.setVisible(true);
            }
        });
    }
}
