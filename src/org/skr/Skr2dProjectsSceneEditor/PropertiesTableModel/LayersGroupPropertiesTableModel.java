package org.skr.Skr2dProjectsSceneEditor.PropertiesTableModel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

import javax.swing.*;

/**
 * Created by rat on 03.09.14.
 */
public class LayersGroupPropertiesTableModel extends PropertiesBaseTableModel {


    private enum Property_ {

        Visible(PropertyType.BOOLEAN);

        private PropertyType propertyType;
        private DataRole dataRole = DataRole.DEFAULT;
        private boolean editable = true;

        Property_(PropertyType propertyType) {
            this.propertyType = propertyType;
        }

        Property_(PropertyType propertyType, DataRole dataRole) {
            this.propertyType = propertyType;
            this.dataRole = dataRole;
        }

        Property_(PropertyType propertyType, boolean editable) {
            this.propertyType = propertyType;
            this.editable = editable;
        }

        Property_(PropertyType propertyType, DataRole dataRole, boolean editable) {
            this.propertyType = propertyType;
            this.dataRole = dataRole;
            this.editable = editable;
        }

        static Property_[] values = Property_.values();
    }


    Group group = null;

    public void setGroup( Group group ) {
        this.group = group;
    }

    public LayersGroupPropertiesTableModel(JTree modelJTree) {
        super(modelJTree);
    }

    @Override
    public Object getComboSelectedObject(int rowIndex) {
        return null;
    }

    @Override
    public Array<Object> getSelectorArray(int rowIndex) {
        return null;
    }

    @Override
    public PropertyType getPropertyType(int rowIndex) {
        Property_ p = Property_.values()[ rowIndex ];
        return p.propertyType;
    }

    @Override
    public DataRole getDataRole(int rowIndex) {
        Property_ p = Property_.values()[ rowIndex ];
        return p.dataRole;
    }

    @Override
    public int getPropertiesCount() {
        return Property_.values.length ;
    }

    @Override
    public boolean isPropertyEditable(int rowIndex) {
        Property_ p = Property_.values()[ rowIndex ];
        return p.editable;
    }

    @Override
    public Object getPropertyName(int rowIndex) {
        Property_ p = Property_.values()[ rowIndex ];
        return p.name();
    }

    @Override
    public Object getPropertyValue(int rowIndex) {
        if ( group == null )
            return null;
        Property_ p = Property_.values()[ rowIndex ];

        switch ( p ) {

            case Visible:
                return isVisible();
        }

        return null;
    }

    @Override
    public void setProperty(Object aValue, int rowIndex) {
        if ( group == null )
            return ;
        Property_ p = Property_.values()[ rowIndex ];

        switch ( p ) {

            case Visible:
                setVisible((Boolean) aValue);
                break;
        }
    }

    private void setVisible( boolean state ) {
        for (Actor a : group.getChildren() )
            a.setVisible( state) ;
        group.setVisible( state );
    }

    private boolean isVisible() {
        return group.isVisible();
    }

}
