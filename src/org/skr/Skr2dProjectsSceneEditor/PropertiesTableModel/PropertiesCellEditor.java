package org.skr.Skr2dProjectsSceneEditor.PropertiesTableModel;

import com.badlogic.gdx.utils.Array;
import com.sun.javafx.binding.StringFormatter;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

/**
 * Created by rat on 01.06.14.
 */
public class PropertiesCellEditor extends AbstractCellEditor implements TableCellEditor {

    JFormattedTextField stringTextField = new JFormattedTextField( );
    JFormattedTextField numberTextField = new JFormattedTextField( NumberFormat.getNumberInstance() );
    JComboBox<Object> combo = new JComboBox<Object>();
    JCheckBox checkBox = new JCheckBox();
    Object editorValue = null;

    public PropertiesCellEditor() {

        stringTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorValue = stringTextField.getText();
            }
        });

        stringTextField.addKeyListener( new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
                    editorValue = stringTextField.getText();
                    fireEditingStopped();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });


        combo.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorValue = combo.getSelectedIndex();
                fireEditingStopped();
            }
        });

        numberTextField.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorValue = Float.valueOf( numberTextField.getText() );
                fireEditingStopped();
            }
        });

        checkBox.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorValue = checkBox.isSelected();
                fireEditingStopped();
            }
        });

    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        PropertiesBaseTableModel bmodel = (PropertiesBaseTableModel) table.getModel();

        PropertiesBaseTableModel.PropertyType ptype = bmodel.getPropertyType( row );

        if ( ptype == null )
            return null;

        switch ( ptype ) {

            case STRING:
                stringTextField.setText((String) bmodel.getValueAt(row, column));
                editorValue = stringTextField.getText();
                return stringTextField;
            case NUMBER:
                numberTextField.setText( bmodel.getValueAt( row, column).toString() );
                editorValue = Float.valueOf( numberTextField.getText() );
                return numberTextField;
            case BOOLEAN:
                editorValue = bmodel.getValueAt( row, column);
                checkBox.setSelected( (Boolean) editorValue  );
                return  checkBox;
            case SELECTOR:
                combo.removeAllItems();
                Array<Object> items = bmodel.getSelectorArray( row );
                if ( items == null )
                    return null;
                for (Object o : items )
                    combo.addItem( o );
                combo.setSelectedItem( bmodel.getComboSelectedObject( row ) );
                editorValue = combo.getSelectedIndex();
                return combo;

        }

        return null;
    }

    @Override
    public Object getCellEditorValue() {
        return editorValue;
    }

    public void cancelEditing() {
        fireEditingCanceled();
    }
}
