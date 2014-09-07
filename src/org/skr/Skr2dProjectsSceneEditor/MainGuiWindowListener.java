package org.skr.Skr2dProjectsSceneEditor;

import com.badlogic.gdx.utils.Array;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by rat on 02.08.14.
 */
public class MainGuiWindowListener implements WindowListener {

    Array<Timer> timers = new Array<Timer>();
    MainGui gui;

    public MainGuiWindowListener(MainGui gui) {
        this.gui = gui;
    }

    public void addTimer( Timer timer ) {
        timers.add( timer );
    }

    public void removeTimer( Timer timer ) {
        timers.removeValue( timer, true );
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

        int n = JOptionPane.showOptionDialog(null, "Do you really want to quit?", "Close Application",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null );
        if ( n!= 0 )
            return;

        if ( !gui.confirmClosure() )
            return;

        e.getWindow().dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        ApplicationSettings.save();

        for ( Timer timer :  timers )
            timer.stop();
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
