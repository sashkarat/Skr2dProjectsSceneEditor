package org.skr.Skr2dProjectsSceneEditor.PropertiesTableModel;

import com.badlogic.gdx.utils.Array;
import org.skr.gdx.scene.Layer;

import javax.swing.*;

/**
 * Created by rat on 08.08.14.
 */
public class LayerPropertiesTableModel extends PropertiesBaseTableModel {


    private enum Property_ {

        Name(PropertyType.STRING),
        PositionX( PropertyType.NUMBER, DataRole.VIEW_COORDINATES ),
        PositionY( PropertyType.NUMBER, DataRole.VIEW_COORDINATES ),
        Rotation( PropertyType.NUMBER, DataRole.VIEW_COORDINATES ),
        zIndex( PropertyType.NUMBER ),
        FollowCameraX(PropertyType.BOOLEAN),
        FollowCameraY(PropertyType.BOOLEAN),
        OffsetAttenuationX(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        OffsetAttenuationY(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        EnableOffsetLimitX(PropertyType.BOOLEAN),
        EnableOffsetLimitY(PropertyType.BOOLEAN),
        OffsetLimitXMin(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        OffsetLimitXMax(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        OffsetLimitYMin(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        OffsetLimitYMax(PropertyType.NUMBER, DataRole.VIEW_COORDINATES);

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


    Layer layer;

    public LayerPropertiesTableModel(JTree modelJTree) {
        super(modelJTree);
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
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
        if ( layer == null )
            return null;

        Property_ p = Property_.values()[ rowIndex ];

        switch ( p ) {
            case Name:
                return layer.getName();
            case PositionX:
                return layer.getX();
            case PositionY:
                return layer.getY();
            case Rotation:
                return layer.getRotation();
            case zIndex:
                return layer.getZIndex();
            case FollowCameraX:
                return layer.getLayerSettings().isFollowCameraX();
            case FollowCameraY:
                return layer.getLayerSettings().isFollowCameraY();
            case OffsetAttenuationX:
                return layer.getLayerSettings().getOffsetAttenuationX();
            case OffsetAttenuationY:
                return layer.getLayerSettings().getOffsetAttenuationY();
            case EnableOffsetLimitX:
                return layer.getLayerSettings().isEnableOffsetLimitX();
            case EnableOffsetLimitY:
                return layer.getLayerSettings().isEnableOffsetLimitY();
            case OffsetLimitXMin:
                return layer.getLayerSettings().getOffsetLimitXMin();
            case OffsetLimitXMax:
                return layer.getLayerSettings().getOffsetLimitXMax();
            case OffsetLimitYMin:
                return layer.getLayerSettings().getOffsetLimitYMin();
            case OffsetLimitYMax:
                return layer.getLayerSettings().getOffsetLimitYMax();
        }

        return null;
    }

    @Override
    public void setProperty(Object aValue, int rowIndex) {

        if ( layer == null )
            return;

        Property_ p = Property_.values()[ rowIndex ];
        switch ( p ) {

            case Name:
                layer.setName((String) aValue);
                break;
            case PositionX:
                layer.setX((Float) aValue);
                break;
            case PositionY:
                layer.setY((Float) aValue);
                break;
            case Rotation:
                layer.setRotation((Float) aValue);
                break;
            case zIndex:
                layer.setZIndex(Math.round((Float) aValue));
                break;
            case FollowCameraX:
                layer.getLayerSettings().setFollowCameraX((Boolean) aValue);
                break;
            case FollowCameraY:
                layer.getLayerSettings().setFollowCameraY((Boolean) aValue);
                break;
            case OffsetAttenuationX:
                layer.getLayerSettings().setOffsetAttenuationX((Float) aValue);
                break;
            case OffsetAttenuationY:
                layer.getLayerSettings().setOffsetAttenuationY((Float) aValue);
                break;
            case EnableOffsetLimitX:
                layer.getLayerSettings().setEnableOffsetLimitX((Boolean) aValue);
                break;
            case EnableOffsetLimitY:
                layer.getLayerSettings().setEnableOffsetLimitY((Boolean) aValue);
                break;
            case OffsetLimitXMin:
                layer.getLayerSettings().setOffsetLimitXMin((Float) aValue);
                break;
            case OffsetLimitXMax:
                layer.getLayerSettings().setOffsetLimitXMax((Float) aValue);
                break;
            case OffsetLimitYMin:
                layer.getLayerSettings().setOffsetLimitYMin((Float) aValue);
                break;
            case OffsetLimitYMax:
                layer.getLayerSettings().setOffsetLimitYMax((Float) aValue);
                break;
        }
    }
}
