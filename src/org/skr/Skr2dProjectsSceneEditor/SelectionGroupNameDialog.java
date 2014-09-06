package org.skr.Skr2dProjectsSceneEditor;

import javax.swing.*;
import java.awt.event.*;

public class SelectionGroupNameDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tfSelectionGroupName;
    boolean accepted = false;

    public SelectionGroupNameDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        accepted = true;
        dispose();
    }

    private void onCancel() {
        accepted = false;
        dispose();
    }

    public String execute( String selectionGroupName ) {
        tfSelectionGroupName.setText( selectionGroupName );
        pack();
        setVisible( true );
        if ( !accepted )
            return null;
        return tfSelectionGroupName.getText();
    }

}
