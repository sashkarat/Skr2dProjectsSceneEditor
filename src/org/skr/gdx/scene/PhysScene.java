package org.skr.gdx.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;
import org.skr.gdx.Environment;
import org.skr.gdx.PhysWorld;
import org.skr.gdx.utils.ModShapeRenderer;

/**
 * Created by rat on 02.08.14.
 */


public class PhysScene extends Group {



    World world;
    CameraController cameraController;
    OrthographicCamera camera;

    TextureAtlas atlas;
    String internalTextureAtlasPath;
    Array< String > textureRegionNames = new Array<String>();
    Array<PhysModelDescriptionHandler> modelHandlers = new Array<PhysModelDescriptionHandler>();
    float viewCenterX = 0;
    float viewCenterY = 0;

    float viewLeft = -500;
    float viewRight = 500;
    float viewTop = 500;
    float viewBottom = -500;

    Group backLayersGroup = new Group();
    Group frontLayersGroup = new Group();

    ModShapeRenderer shapeRenderer = null;

    public PhysScene( World world ) {
        super();
        this.world = world;
        backLayersGroup.setName("BACK LAYERS");
        frontLayersGroup.setName("FRONT LAYERS");
    }

    public void initializeScene( Stage stage ) {
        setStage( stage );
        cameraController = new CameraController( this );
        shapeRenderer = new ModShapeRenderer();
        camera = (OrthographicCamera) stage.getCamera();
    }

    public String getInternalTextureAtlasPath() {
        return internalTextureAtlasPath;
    }

    public void setInternalTextureAtlasPath(String internalTextureAtlasPath) {
        this.internalTextureAtlasPath = internalTextureAtlasPath;
    }

    public Array<PhysModelDescriptionHandler> getModelHandlers() {
        return modelHandlers;
    }

    public float getViewLeft() {
        return viewLeft;
    }

    public void setViewLeft(float viewLeft) {
        this.viewLeft = viewLeft;
    }

    public float getViewRight() {
        return viewRight;
    }

    public void setViewRight(float viewRight) {
        this.viewRight = viewRight;
    }

    public float getViewTop() {
        return viewTop;
    }

    public void setViewTop(float viewTop) {
        this.viewTop = viewTop;
    }

    public float getViewBottom() {
        return viewBottom;
    }

    public void setViewBottom(float viewBottom) {
        this.viewBottom = viewBottom;
    }

    public float getViewCenterX() {
        return viewCenterX;
    }

    public void setViewCenterX(float viewCenterX) {
        this.viewCenterX = viewCenterX;
    }

    public float getViewCenterY() {
        return viewCenterY;
    }

    public void setViewCenterY(float viewCenterY) {
        this.viewCenterY = viewCenterY;
    }

    public CameraController getCameraController() {
        return cameraController;
    }

    public Array<Layer> getBackLayers() {
        Array<Layer> lrs = new Array<Layer>();
        for (Actor la :  backLayersGroup.getChildren() ) {
            if ( la instanceof Layer )
                lrs.add((Layer) la);
        }
        return  lrs;
    }

    public Array<Layer> getFrontLayers() {
        Array<Layer> lrs = new Array<Layer>();
        for ( Actor a : frontLayersGroup.getChildren() ) {
            if ( a instanceof  Layer ) {
                lrs.add((Layer) a);
            }
        }

        return lrs;
    }

    public void addFrontLayer( Layer layer ) {
        frontLayersGroup.addActor( layer );
    }

    public void removeFrontLayer( Layer layer ) {
        frontLayersGroup.removeActor( layer );
    }

    public Group getBackLayersGroup() {
        return backLayersGroup;
    }

    public Group getFrontLayersGroup() {
        return frontLayersGroup;
    }

    public void addBackLayer( Layer layer ) {
        backLayersGroup.addActor(layer);
    }

    public void removeBackLayer(Layer layer) {
        backLayersGroup.removeActor(layer);
    }

    public boolean loadTextureAtlas() {

        if ( this.internalTextureAtlasPath.isEmpty() )
            return false;

        return loadTextureAtlas( this.internalTextureAtlasPath );
    }

    private boolean loadTextureAtlas( String internalFilePath ) {

        if ( atlas != null ) {
            atlas.dispose();
        }

        FileHandle packFile = Gdx.files.internal( internalFilePath );

        TextureAtlas.TextureAtlasData atlasData = new TextureAtlas.TextureAtlasData(
                packFile, packFile.parent(), false );

        if ( atlasData == null )
            return false;

        atlas = new TextureAtlas( atlasData );

        this.textureRegionNames.clear();

        Array<TextureAtlas.TextureAtlasData.Region> regions = atlasData.getRegions();

        for (TextureAtlas.TextureAtlasData.Region region : regions ) {
            if ( region.index > 1)
                continue;
            this.textureRegionNames.add( region.name );
        }

        this.textureRegionNames.sort();

        return true;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public Array<String> getTextureRegionNames() {
        return textureRegionNames;
    }

    public boolean loadFromDescription( PhysSceneDescription sd ) {

        setName(sd.getName());
        setInternalTextureAtlasPath(sd.getTextureAtlasPath());
        setViewLeft( sd.getViewLeft() );
        setViewRight( sd.getViewRight() );
        setViewTop( sd.getViewTop() );
        setViewBottom( sd.getViewBottom() );
        setViewCenterX( sd.getViewCenterX() );
        setViewCenterY( sd.getViewCenterY() );


        if ( !loadTextureAtlas( this.internalTextureAtlasPath) )
            return false;
        for (String internalFilePath : sd.getModelFiles() ) {
            PhysModelDescriptionHandler hndlr = new PhysModelDescriptionHandler();
            if ( !hndlr.loadDescription( internalFilePath ) )
                continue;
            modelHandlers.add( hndlr );
        }

        for ( LayerDescription ld : sd.getBackLayerDescriptions() ) {
            Layer lr = new Layer( this );
            lr.loadFromDescription(ld);
            backLayersGroup.addActor(lr);
        }

        for ( LayerDescription ld : sd.getFrontLayerDescriptions() ) {
            Layer lr = new Layer( this );
            lr.loadFromDescription( ld );
            frontLayersGroup.addActor( lr );
        }

        //TODO: load children in the Z order



        //TODO: implement method

        return true;
    }

    public PhysSceneDescription getDescription() {

        PhysSceneDescription sd = new PhysSceneDescription();

        sd.setName(getName());
        sd.setTextureAtlasPath(this.internalTextureAtlasPath);
        sd.getModelFiles().clear();

        for ( PhysModelDescriptionHandler hndlr : modelHandlers ) {
            sd.modelFiles.add( hndlr.getInternalFilePath() );
        }

        sd.setViewLeft( getViewLeft() );
        sd.setViewRight(getViewRight());
        sd.setViewTop(getViewTop());
        sd.setViewBottom( getViewBottom() );
        sd.setViewCenterX( getViewCenterX() );
        sd.setViewCenterY( getViewCenterY() );

        sd.getBackLayerDescriptions().clear();

        for ( Actor la: backLayersGroup.getChildren() ) {
            if ( la instanceof Layer ) {
                Layer lr = (Layer) la;
                sd.getBackLayerDescriptions().add(lr.getDescription());
            }
        }

        for ( Actor la : frontLayersGroup.getChildren() ) {
            if ( la instanceof  Layer ) {
                Layer lr = (Layer) la;
                sd.getFrontLayerDescriptions().add( lr.getDescription() );
            }
        }


        //TODO: implement method
        return sd;
    }


    public static interface SceneStateListener {
        void sceneLoaded( PhysScene scene );
        void sceneSaved( PhysScene scene );
    }

    private static SceneStateListener sceneListener = null;

    public static void setSceneStateListener( SceneStateListener listener ) {
        sceneListener = listener;
    }

    public static PhysScene loadFromFile( FileHandle fileHandle ) {
        Json js = new Json();
        PhysScene scene = null;

        try {

            PhysSceneDescription description = js.fromJson(PhysSceneDescription.class, fileHandle);
            scene = new PhysScene( PhysWorld.getPrimaryWorld() );
            if ( !scene.loadFromDescription(description) ) {
                Gdx.app.error("PhysScene.loadFromFile", "Unable to load from description. Scene name: "
                        + description.getName() );
                return null;
            }

        } catch ( SerializationException e) {
            Gdx.app.error("PhysScene.loadFromFile", e.getMessage() );
            e.printStackTrace();
        }

        if ( scene != null ) {
            Gdx.app.log("PhysScene.loadFromFile", "Scene: \"" + scene.getName() + "\" OK");
            if ( sceneListener != null ) {
                sceneListener.sceneLoaded( scene );
            }
        }

        return scene;
    }

    public static void saveToFile(PhysScene scene, FileHandle fileHandle) {
        Json js = new Json();
        try {

            PhysSceneDescription description = scene.getDescription();
            js.toJson(description, PhysSceneDescription.class, fileHandle );
            Gdx.app.log("PhysScene.saveToFile", "Scene: \"" + description.getName() +
                    "\"; File: \"" + fileHandle + "\" OK");
            if ( sceneListener != null ) {
                sceneListener.sceneSaved( scene );
            }
        } catch ( SerializationException e) {
            Gdx.app.error("PhysScene.saveToFile", e.getMessage() );
            e.printStackTrace();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        cameraController.act( delta );
        backLayersGroup.act(delta);
        frontLayersGroup.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        backLayersGroup.draw(batch, parentAlpha);
        super.draw(batch, parentAlpha);
        frontLayersGroup.draw( batch, parentAlpha );

        if ( !Environment.debugRender )
            return;

        shapeRenderer.setProjectionMatrix( batch.getProjectionMatrix() );
        shapeRenderer.setTransformMatrix( batch.getTransformMatrix() );

        shapeRenderer.setColor(1, 1, 1, 1);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(viewLeft + 10, viewBottom + 10,
                viewRight - viewLeft - 20, viewTop - viewBottom - 20);

        shapeRenderer.end();




    }


}
