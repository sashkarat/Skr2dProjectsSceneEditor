package org.skr.gdx.scene;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

/**
 * Created by rat on 06.08.14.
 */

public class Layer extends Group implements Comparable {
    PhysScene scene;
    Array<TiledActor> actors;

    float distance = 9999f;

    public Layer(PhysScene scene) {
        this.scene = scene;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void addTiledActor( TiledActor ta ) {
        actors.add( ta );
        addActor( ta );
    }

    public void removeTiledActor( TiledActor ta ) {
        removeActor( ta );
        actors.removeValue( ta, true );
    }

    public void loadFromDescription( LayerDescription desc ) {
        setName( desc.getName() );
        setPosition( desc.getX(), desc.getY() );
        setRotation( desc.getRotation() );
        for ( TiledActorDescription td : desc.getTiledActorDescriptions() ) {
            TiledActor ta = new TiledActor( scene );
            ta.loadFromDescription( td );
            addTiledActor( ta );
        }
    }

    public LayerDescription getDescription() {
        LayerDescription desc = new LayerDescription();

        desc.setName( getName() );
        desc.setX( getX() );
        desc.setY( getY() );
        desc.setRotation( getRotation() );
        Array<TiledActorDescription> tdList = new Array<TiledActorDescription>();
        for ( TiledActor ta : actors )
            tdList.add( ta.getDescription());
        desc.setTiledActorDescriptions( tdList );

        return desc;
    }

    @Override
    public int compareTo(Object o) {
        if ( o.equals( this ))
            return 0;
        Layer l = (Layer) o;
        if ( distance < l.getDistance() )
            return -1;
        if ( distance > l.getDistance() )
            return 1;
        return 0;
    }
}
