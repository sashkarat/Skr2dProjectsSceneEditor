package org.skr.Skr2dProjectsSceneEditor.PropertiesTableModel;

import com.badlogic.gdx.utils.Array;
import org.skr.gdx.scene.Layer;

import javax.swing.*;

/**
 * Created by rat on 08.08.14.
 */
public class LayerPropertiesTableModel extends PropertiesBaseTableModel {


    private enum Property_ {


        /*
        boolean followCameraX = false;
        boolean followCameraY = false;
        float offsetAttenuationX = 1;
        float offsetAttenuationY = 1;
        boolean enableOffsetLimitX = false;
        boolean enableOffsetLimitY = false;
        float offsetLimitX = 0;
        float offsetLimitY = 0;
         */
        Name(PropertyType.STRING),
        PositionX( PropertyType.NUMBER, DataRole.VIEW_COORDINATES ),
        PositionY( PropertyType.NUMBER, DataRole.VIEW_COORDINATES ),
        Rotation( PropertyType.NUMBER, DataRole.VIEW_COORDINATES ),
        ZOrder( PropertyType.NUMBER ),
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
            case ZOrder:
                return layer.getzOrder();
            case FollowCameraX:
                return layer.getActionSettings().isFollowCameraX();
            case FollowCameraY:
                return layer.getActionSettings().isFollowCameraY();
            case OffsetAttenuationX:
                return layer.getActionSettings().getOffsetAttenuationX();
            case OffsetAttenuationY:
                return layer.getActionSettings().getOffsetAttenuationY();
            case EnableOffsetLimitX:
                return layer.getActionSettings().isEnableOffsetLimitX();
            case EnableOffsetLimitY:
                return layer.getActionSettings().isEnableOffsetLimitY();
            case OffsetLimitXMin:
                return layer.getActionSettings().getOffsetLimitXMin();
            case OffsetLimitXMax:
                return layer.getActionSettings().getOffsetLimitXMax();
            case OffsetLimitYMin:
                return layer.getActionSettings().getOffsetLimitYMin();
            case OffsetLimitYMax:
                return layer.getActionSettings().getOffsetLimitYMax();
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
            case ZOrder:
                layer.setzOrder((Float) aValue);
                break;
            case FollowCameraX:
                layer.getActionSettings().setFollowCameraX((Boolean) aValue);
                break;
            case FollowCameraY:
                layer.getActionSettings().setFollowCameraY((Boolean) aValue);
                break;
            case OffsetAttenuationX:
                layer.getActionSettings().setOffsetAttenuationX((Float) aValue);
                break;
            case OffsetAttenuationY:
                layer.getActionSettings().setOffsetAttenuationY((Float) aValue);
                break;
            case EnableOffsetLimitX:
                layer.getActionSettings().setEnableOffsetLimitX((Boolean) aValue);
                break;
            case EnableOffsetLimitY:
                layer.getActionSettings().setEnableOffsetLimitY((Boolean) aValue);
                break;
            case OffsetLimitXMin:
                layer.getActionSettings().setOffsetLimitXMin((Float) aValue);
                break;
            case OffsetLimitXMax:
                layer.getActionSettings().setOffsetLimitXMax((Float) aValue);
                break;
            case OffsetLimitYMin:
                layer.getActionSettings().setOffsetLimitYMin((Float) aValue);
                break;
            case OffsetLimitYMax:
                layer.getActionSettings().setOffsetLimitYMax((Float) aValue);
                break;
        }
    }
}
