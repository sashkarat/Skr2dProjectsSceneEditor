package org.skr.Skr2dProjectsSceneEditor.PropertiesTableModel;

import com.badlogic.gdx.utils.Array;
import org.skr.gdx.scene.TiledActor;

import javax.swing.*;

/**
 * Created by rat on 08.08.14.
 */
public class TiledActorPropertiesTableModel extends PropertiesBaseTableModel {

    private enum Property_ {

        Name(PropertyType.STRING),
        Type(PropertyType.SELECTOR),
        ZIndex(PropertyType.NUMBER),
        AagPosX(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        AagPosY(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        SizeX(PropertyType.NUMBER),
        SizeY(PropertyType.NUMBER),
        SpaceX(PropertyType.NUMBER, DataRole.VIEW_COORDINATES),
        SpaceY(PropertyType.NUMBER, DataRole.VIEW_COORDINATES);


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


    TiledActor tiledActor;

    public TiledActorPropertiesTableModel(JTree modelJTree) {
        super(modelJTree);
    }

    public TiledActor getTiledActor() {
        return tiledActor;
    }

    public void setTiledActor(TiledActor tiledActor) {
        this.tiledActor = tiledActor;
    }

    @Override
    public Object getComboSelectedObject(int rowIndex) {
        if ( tiledActor == null )
            return null;
        switch ( getProperty( rowIndex ) ) {
            case Type:
                return tiledActor.getType();
        }
        return null;
    }

    private static Array< Object> typesArray =
            new Array<Object>( TiledActor.Type.values() );

    @Override
    public Array<Object> getSelectorArray(int rowIndex) {
        switch ( getProperty( rowIndex )) {
            case Type:
                return typesArray;
        }
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
        if ( tiledActor == null )
            return null;
        switch ( getProperty(rowIndex) ) {

            case Name:
                return tiledActor.getName();
            case Type:
                return tiledActor.getType();
            case ZIndex:
                return tiledActor.getZIndex();
            case AagPosX:
                return tiledActor.getAagPosX();
            case AagPosY:
                return tiledActor.getAagPosY();
            case SizeX:
                return tiledActor.getNumberX();
            case SizeY:
                return tiledActor.getNumberY();
            case SpaceX:
                return tiledActor.getSpaceX();
            case SpaceY:
                return tiledActor.getSpaceY();
        }

        return  null;
    }


    @Override
    public void setProperty(Object aValue, int rowIndex) {
        if ( tiledActor == null )
            return;
        switch ( getProperty(rowIndex) ) {

            case Name:
                tiledActor.setName((String) aValue);
                break;
            case Type:
                tiledActor.setType( TiledActor.Type.values()[ (Integer) aValue ]);
                break;
            case ZIndex:
                tiledActor.setZIndex((Integer) aValue );
                break;
            case AagPosX:
                tiledActor.setAagPosX((Float) aValue);
                break;
            case AagPosY:
                tiledActor.setAagPosY((Float) aValue);
                break;
            case SizeX:
                tiledActor.setNumberX( Math.round((Float) aValue));
                break;
            case SizeY:
                tiledActor.setNumberY( Math.round((Float) aValue));
                break;
            case SpaceX:
                tiledActor.setSpaceX((Float) aValue );
                break;
            case SpaceY:
                tiledActor.setSpaceY((Float) aValue);
                break;
        }
    }
}
