package org.skr.Skr2dProjectsSceneEditor.PropertiesTableModel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import org.skr.Skr2dProjectsSceneEditor.gdx.controllers.ModelsController;
import org.skr.gdx.editor.Controller;

import javax.swing.*;

/**
 * Created by rat on 06.09.14.
 */
public class ModelsControllerPropertiesTableModel extends PropertiesBaseTableModel {

    private enum Property_ {

        BasePointPos_X(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        BasePointPos_Y(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        ModelsNumber(PropertyType.NUMBER, false);

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

    Property_ getProperty( int rowIndex ) {
        return Property_.values[ rowIndex ];
    }


    ModelsController modelsController;

    public void setModelsController(ModelsController modelsController) {
        this.modelsController = modelsController;
    }

    public ModelsControllerPropertiesTableModel(JTree modelJTree) {
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
        return getProperty(rowIndex).propertyType;
    }

    @Override
    public DataRole getDataRole(int rowIndex) {
        return getProperty(rowIndex).dataRole;
    }

    @Override
    public int getPropertiesCount() {
        return Property_.values.length;
    }

    @Override
    public boolean isPropertyEditable(int rowIndex) {
        return getProperty(rowIndex).editable;
    }

    @Override
    public Object getPropertyName(int rowIndex) {
        return getProperty(rowIndex).name();
    }

    @Override
    public Object getPropertyValue(int rowIndex) {
        switch ( getProperty(rowIndex) ) {
            case BasePointPos_X:
                return modelsController.getPosControlPoint().getX();
            case BasePointPos_Y:
                return modelsController.getPosControlPoint().getY();
            case ModelsNumber:
                return modelsController.getModelItems().size;
        }
        return null;
    }

    @Override
    public void setProperty(Object aValue, int rowIndex) {
        switch ( getProperty(rowIndex) ) {
            case BasePointPos_X:
                modelsController.translateModelsToViewX((Float) aValue);
                break;
            case BasePointPos_Y:
                modelsController.translateModelsToViewY((Float) aValue);
                break;
            case ModelsNumber:
                break;
        }
        Gdx.app.postRunnable( new Runnable() {
            @Override
            public void run() {
                fireTableDataChanged();
            }
        });
    }
}
