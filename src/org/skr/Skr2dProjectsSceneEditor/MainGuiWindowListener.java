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
