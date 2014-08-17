package org.skr.Skr2dProjectsSceneEditor.PropertiesTableModel;

import com.badlogic.gdx.utils.Array;
import org.skr.gdx.scene.PhysModelItem;

import javax.swing.*;

/**
 * Created by rat on 16.08.14.
 */
public class PhysModelItemPropertiesTableModel extends PropertiesBaseTableModel {

    PhysModelItem modelItem;

    private enum Property_ {

        Name(PropertyType.STRING),
        BasePointViewX(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        BasePointViewY(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        BasePointPhysX(PropertyType.NUMBER, DataRole.PHYS_COORDINATES),
        BasePointPhysY(PropertyType.NUMBER, DataRole.PHYS_COORDINATES);

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

    private static Property_ getProperty( int index ) {
        return Property_.values[index];
    }

    public PhysModelItemPropertiesTableModel(JTree modelJTree) {
        super(modelJTree);
    }

    public PhysModelItem getModelItem() {
        return modelItem;
    }

    public void setModelItem(PhysModelItem modelItem) {
        this.modelItem = modelItem;
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
        return getProperty( rowIndex ).propertyType;
    }

    @Override
    public DataRole getDataRole(int rowIndex) {
        return getProperty( rowIndex ).dataRole;
    }

    @Override
    public int getPropertiesCount() {
        return Property_.values.length;
    }

    @Override
    public boolean isPropertyEditable(int rowIndex) {
        return getProperty( rowIndex ).editable;
    }

    @Override
    public Object getPropertyName(int rowIndex) {
        return getProperty( rowIndex ).name();
    }

    @Override
    public Object getPropertyValue(int rowIndex) {
        switch ( getProperty(rowIndex ) ) {

            case Name:
                return modelItem.getName();
            case BasePointViewX:
                return modelItem.getBasePointViewX();
            case BasePointViewY:
                return modelItem.getBasePointViewY();
            case BasePointPhysX:
                return modelItem.getBasePointPhysX();
            case BasePointPhysY:
                return modelItem.getBasePointPhysY();
        }
        return null;
    }

    @Override
    public void setProperty(Object aValue, int rowIndex) {
        switch ( getProperty(rowIndex) ) {

            case Name:
                modelItem.setName((String) aValue );
                break;
            case BasePointViewX:
                modelItem.translateBasePointXTo_view((Float) aValue );
                break;
            case BasePointViewY:
                modelItem.translateBasePointYTo_view( (Float) aValue );
                break;
            case BasePointPhysX:
                modelItem.translateBasePointXTo_phys( (Float) aValue );
                break;
            case BasePointPhysY:
                modelItem.translateBasePointYTo_phys((Float) aValue );
                break;
        }
        fireTableDataChanged();
    }
}
