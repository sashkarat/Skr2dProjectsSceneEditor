package org.skr.Skr2dProjectsSceneEditor.PropertiesTableModel;

import com.badlogic.gdx.utils.Array;
import org.skr.gdx.scene.PhysScene;

import javax.swing.*;

/**
 * Created by rat on 04.08.14.
 */
public class ScenePropertiesTableModel extends PropertiesBaseTableModel {

    PhysScene scene;

    private enum Property_ {

        Name(PropertyType.STRING),
        AtlasPath( PropertyType.STRING, false ),
        ViewLeft(PropertyType.NUMBER, DataRole.VIEW_COORDINATES, true ),
        ViewRight(PropertyType.NUMBER, DataRole.VIEW_COORDINATES, true ),
        ViewTop(PropertyType.NUMBER, DataRole.VIEW_COORDINATES, true ),
        ViewBottom(PropertyType.NUMBER, DataRole.VIEW_COORDINATES, true ),
        ViewCenterX(PropertyType.NUMBER, DataRole.VIEW_COORDINATES, true ),
        ViewCenterY(PropertyType.NUMBER, DataRole.VIEW_COORDINATES, true );

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

    public ScenePropertiesTableModel(JTree modelJTree) {
        super(modelJTree);
    }

    @Override
    public Object getComboSelectedObject(int rowIndex) {
        return null;
    }

    public PhysScene getScene() {
        return scene;
    }

    public void setScene(PhysScene scene) {
        this.scene = scene;
    }

    @Override
    public Array<Object> getSelectorArray(int rowIndex) {
        return null;
    }

    @Override
    public PropertyType getPropertyType(int rowIndex) {
        Property_ p = Property_.values[rowIndex];
        return p.propertyType;
    }

    @Override
    public DataRole getDataRole(int rowIndex) {
        Property_ p = Property_.values[rowIndex];
        return p.dataRole;
    }

    @Override
    public int getPropertiesCount() {
        return Property_.values.length;
    }

    @Override
    public boolean isPropertyEditable(int rowIndex) {
        Property_ p = Property_.values[rowIndex];
        return p.editable;
    }

    @Override
    public Object getPropertyName(int rowIndex) {
        Property_ p = Property_.values[rowIndex];
        return p.name();
    }

    @Override
    public Object getPropertyValue(int rowIndex) {
        if ( scene == null )
            return null;
        Property_ p = Property_.values[rowIndex];

        switch ( p ) {
            case Name:
                return scene.getName();
            case AtlasPath:
                return scene.getInternalTextureAtlasPath();
            case ViewLeft:
                return scene.getViewLeft();
            case ViewRight:
                return scene.getViewRight();
            case ViewTop:
                return scene.getViewTop();
            case ViewBottom:
                return scene.getViewBottom();
            case ViewCenterX:
                return scene.getViewCenterX();
            case ViewCenterY:
                return scene.getViewCenterY();
        }

        return null;
    }

    @Override
    public void setProperty(Object aValue, int rowIndex) {
        if ( scene == null )
            return;
        Property_ p = Property_.values[rowIndex];

        switch ( p ) {
            case Name:
                scene.setName((String) aValue);
                break;
            case AtlasPath:
                break;
            case ViewLeft:
                scene.setViewLeft((Float) aValue);
                break;
            case ViewRight:
                scene.setViewRight((Float) aValue);
                break;
            case ViewTop:
                scene.setViewTop((Float) aValue);
                break;
            case ViewBottom:
                scene.setViewBottom((Float) aValue);
                break;
            case ViewCenterX:
                scene.setViewCenterX((Float) aValue);
                break;
            case ViewCenterY:
                scene.setViewCenterY((Float) aValue);
                break;
        }

        return;
    }
}
