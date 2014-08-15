package org.skr.gdx.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import org.skr.gdx.physmodel.animatedactorgroup.AagDescription;
import org.skr.gdx.physmodel.animatedactorgroup.AnimatedActorGroup;

import java.util.Iterator;

/**
 * Created by rat on 13.08.14.
 */
public class GroupChildrenDescriptions {
    public enum ChildType {
        TILED_ACTOR,
        AAG_ACTOR
    }

    Array<TiledActorDescription> tiledActorDescriptions = new Array<TiledActorDescription>();
    Array<AagDescription> aagDescriptions = new Array<AagDescription>();
    Array<ChildType> childrenOrder = new Array<ChildType>();

    public Array<TiledActorDescription> getTiledActorDescriptions() {
        return tiledActorDescriptions;
    }

    public void setTiledActorDescriptions(Array<TiledActorDescription> tiledActorDescriptions) {
        this.tiledActorDescriptions = tiledActorDescriptions;
    }

    public Array<AagDescription> getAagDescriptions() {
        return aagDescriptions;
    }

    public void setAagDescriptions(Array<AagDescription> aagDescriptions) {
        this.aagDescriptions = aagDescriptions;
    }

    public Array<ChildType> getChildrenOrder() {
        return childrenOrder;
    }

    public void setChildrenOrder(Array<ChildType> childrenOrder) {
        this.childrenOrder = childrenOrder;
    }

    public void addChild( Actor actor ) {
        if ( actor instanceof TiledActor ) {
            TiledActor ta = (TiledActor) actor;
            tiledActorDescriptions.add( ta.getDescription() );
            childrenOrder.add( ChildType.TILED_ACTOR );
        } else if ( actor instanceof AnimatedActorGroup ) {
            AnimatedActorGroup aag = (AnimatedActorGroup) actor;
            aagDescriptions.add( aag.getDescription() );
            childrenOrder.add( ChildType.AAG_ACTOR );
        }
    }

    private PhysScene scene;
    private Iterator<TiledActorDescription> taDi = null;
    private Iterator<AagDescription> aagDi = null;
    private Iterator<ChildType> typeI = null;


    public void beginLoading( PhysScene scene ) {
        this.scene = scene;
        taDi = tiledActorDescriptions.iterator();
        aagDi = aagDescriptions.iterator();
        typeI = childrenOrder.iterator();
    }

    public Actor getNextActor() {
        if ( scene == null )
            return null;
        ChildType type = typeI.next();
        if ( type == null )
            return null;
        switch ( type ) {
            case TILED_ACTOR:
                if ( !taDi.hasNext() )
                    break;
                TiledActorDescription taD = taDi.next();
                TiledActor ta = new TiledActor( scene );
                ta.loadFromDescription( taD );
                return ta;
            case AAG_ACTOR:
                if ( !aagDi.hasNext() )
                    break;
                AagDescription aagD = aagDi.next();
                AnimatedActorGroup aag = new AnimatedActorGroup( aagD, scene.getAtlas() );
                return aag;
        }
        return null;
    }

    public boolean hasNextActor() {
        if ( scene == null )
            return false;
        if ( taDi.hasNext() )
            return true;
        if ( aagDi.hasNext() )
            return true;
        return false;
    }

    public void endLoading() {
        this.scene = null;
    }
}
