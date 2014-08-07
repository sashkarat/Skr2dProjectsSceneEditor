package org.skr.Skr2dProjectsSceneEditor;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by rat on 02.08.14.
 */
public class MainGuiWindowListener implements WindowListener {
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        ApplicationSettings.save();
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
