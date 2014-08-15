package org.skr.Skr2dProjectsSceneEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import org.skr.Skr2dProjectsSceneEditor.PropertiesTableModel.*;
import org.skr.Skr2dProjectsSceneEditor.gdx.SkrGdxAppSceneEditor;
import org.skr.Skr2dProjectsSceneEditor.gdx.screens.EditorScreen;
import org.skr.gdx.PhysWorld;
import org.skr.gdx.physmodel.animatedactorgroup.AnimatedActorGroup;
import org.skr.gdx.scene.Layer;
import org.skr.gdx.scene.PhysScene;
import org.skr.gdx.scene.TiledActor;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by rat on 02.08.14.
 */

public class MainGui extends JFrame {


    private JPanel rootPanel;
    private JPanel panelTopButtons;
    private JPanel panelBottomButtons;
    private JPanel panelGdx;
    private JSplitPane splitMain;
    private JScrollPane scrollMain;
    private JTable tableProperties;
    private JTabbedPane tabbedPane2;
    private JTabbedPane tabPnTrees;
    private JTree treeScene;
    private JButton btnNewScene;
    private JButton btnLoadScene;
    private JButton btnSaveScane;
    private JButton btnSaveSceneAs;
    private JTextField tfTexAtlasFilePath;
    private JButton btnBrowseTexAtlasFile;
    private JPanel panelProperties;
    private JButton btnAddNode;
    private JButton btnRemoveNode;
    private JCheckBox chbDisplayGrid;
    private JCheckBox chbDisplayGridFirst;
    private JCheckBox chbDebugRender;


    SkrGdxAppSceneEditor gApp;
    PhysScene scene;
    String sceneAbsolutePath = "";
    DefaultTreeModel treeSceneModel;
    DefaultTableModel defaultTableModel = new DefaultTableModel();

    private PropertiesCellEditor propertiesCellEditor;
    private ScenePropertiesTableModel scenePropertiesTableModel;
    private LayerPropertiesTableModel layerPropertiesTableModel;
    private TiledActorPropertiesTableModel tiledActorPropertiesTableModel;
    private AagPropertiesTableModel aagPropertiesTableModel;

    private EditorScreen editorScreen;


    MainGui() {
        gApp = new SkrGdxAppSceneEditor();
        final LwjglAWTCanvas gdxCanvas = new LwjglAWTCanvas( gApp );

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(rootPanel);
        panelGdx.add(gdxCanvas.getCanvas(), BorderLayout.CENTER);
        pack();
        setSize(1280, 800);
        ApplicationSettings.load();
        updateGuiFromSetting();

        loadJTree();

        MainGuiWindowListener guiWindowListener = new MainGuiWindowListener();
        addWindowListener(guiWindowListener);
        PhysScene.setSceneStateListener( sceneStateListener );

        scenePropertiesTableModel = new ScenePropertiesTableModel( treeScene );
        layerPropertiesTableModel = new LayerPropertiesTableModel( treeScene );
        tiledActorPropertiesTableModel = new TiledActorPropertiesTableModel( treeScene );
        aagPropertiesTableModel=  new AagPropertiesTableModel( treeScene );

        JTableHeader th = tableProperties.getTableHeader();
        panelProperties.add(th, BorderLayout.NORTH);
        propertiesCellEditor = new PropertiesCellEditor();
        tableProperties.setDefaultEditor(
                PropertiesBaseTableModel.Property.class,
                propertiesCellEditor );
        tableProperties.setDefaultRenderer(PropertiesBaseTableModel.Property.class,
                new PropertiesTableCellRenderer() );

        Gdx.app.postRunnable( new Runnable() {
            @Override
            public void run() {
                editorScreen = gApp.getEditorScreen();
                chbDebugRender.setSelected( editorScreen.isDoDebugRender() );
                chbDisplayGrid.setSelected( editorScreen.isDisplayGrid() );
                chbDisplayGridFirst.setSelected( editorScreen.isDisplayGridFirst() );
            }
        });

        btnNewScene.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewScene();
            }
        });
        btnLoadScene.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadScene();
            }
        });
        btnSaveScane.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveScene();
            }
        });
        btnSaveSceneAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSceneAs();
            }
        });
        btnBrowseTexAtlasFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseTextureAtlasFile();
            }
        });
        treeScene.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                processJTreeSelection( e );
            }
        });
        btnAddNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNode();
            }
        });
        btnRemoveNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeNode();
            }
        });

        chbDisplayGrid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gApp.getEditorScreen().setDisplayGrid( chbDisplayGrid.isSelected() );
            }
        });
        chbDisplayGridFirst.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gApp.getEditorScreen().setDisplayGridFirst( chbDisplayGridFirst.isSelected() );
            }
        });
        chbDebugRender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gApp.getEditorScreen().setDoDebugRender( chbDebugRender.isSelected() );
            }
        });
    }


    void updateGuiFromSetting() {
        tfTexAtlasFilePath.setText( ApplicationSettings.get().getTextureAtlasFile() );
    }


    // Scene project functions

    private static final FileNameExtensionFilter ffScene = new FileNameExtensionFilter("PhysScene files:", "physscene");
    private static final FileNameExtensionFilter ffModel =  new FileNameExtensionFilter("PhysModel files:", "physmodel");
    private static final FileNameExtensionFilter ffAtlas =  new FileNameExtensionFilter("Atlas file:", "atlas");


    PhysScene.SceneStateListener sceneStateListener = new PhysScene.SceneStateListener() {
        @Override
        public void sceneLoaded(PhysScene scene) {
            MainGui.this.scene = scene;
            Gdx.app.postRunnable( new Runnable() {
                @Override
                public void run() {
                    ((SkrGdxAppSceneEditor)SkrGdxAppSceneEditor.get()).getEditorScreen().setScene(MainGui.this.scene);
                    MainGui.this.sceneToGui();
                }
            });

        }

        @Override
        public void sceneSaved(PhysScene scene) {

        }
    };


    void clear() {
        PhysWorld.clearPrimaryWorld();
    }


    void createNewScene() {

        clear();

        scene = new PhysScene( PhysWorld.getPrimaryWorld() );
        sceneAbsolutePath = "";
        scene.setName("newScene");
        scene.setInternalTextureAtlasPath( tfTexAtlasFilePath.getText() );

        Gdx.app.postRunnable( new Runnable() {
            @Override
            public void run() {
                if ( !scene.loadTextureAtlas() ) {
                    Gdx.app.error( "MainGui.createNewScene", " Unable to load Texture Atlas File " );
                    scene = null;
                    return;
                }
                ((SkrGdxAppSceneEditor)SkrGdxAppSceneEditor.get()).getEditorScreen().setScene( scene );
                MainGui.this.sceneToGui();
            }
        });
    }

    void loadScene() {
        clear();

        final JFileChooser fch = new JFileChooser();
        int res;

        fch.setCurrentDirectory( new File( workDirectory + "//data" ) );
        fch.setFileFilter( ffScene );

        res = fch.showOpenDialog(this);

        if ( res != JFileChooser.APPROVE_OPTION )
            return;

        File fl = fch.getSelectedFile();

        ApplicationSettings.get().setLastDirectory( fl.getParent() );

        sceneAbsolutePath = fl.getAbsolutePath();

        StringBuilder sb = new StringBuilder( sceneAbsolutePath );
        int li = workDirectory.length() + 1; // +1  to don't forget about '/'
        sb = sb.delete(0, li );

        final StringBuilder finalSb = sb;

        Gdx.app.postRunnable( new Runnable() {
            @Override
            public void run() {
                PhysScene.loadFromFile(Gdx.files.internal(finalSb.toString()));
            }
        });
    }


    void saveScene() {
        if ( scene == null )
            return;
        if ( sceneAbsolutePath.isEmpty() ) {
            saveSceneAs();
            return;
        }

        PhysScene.saveToFile(scene, Gdx.files.absolute(sceneAbsolutePath) );
    }

    void saveSceneAs() {

        if ( scene == null )
            return;

        final JFileChooser fch = new JFileChooser();
        int res;

        fch.setCurrentDirectory( new File( workDirectory + "//data" ) );
        fch.setFileFilter( ffScene );

        res = fch.showSaveDialog(this);

        if ( res != JFileChooser.APPROVE_OPTION )
            return;

        File fl = fch.getSelectedFile();

        ApplicationSettings.get().setLastDirectory( fl.getParent() );

        sceneAbsolutePath = fl.getAbsolutePath();

        if ( !sceneAbsolutePath.toLowerCase().endsWith( "." + ffScene.getExtensions()[0]) ) {
            sceneAbsolutePath += ("." + ffScene.getExtensions()[0]);
        }


        Gdx.app.log("MainGui.saveSceneAs", "Path: " + sceneAbsolutePath);


        PhysScene.saveToFile( scene, Gdx.files.absolute(sceneAbsolutePath) );
    }

    //         Gui processing


    void browseTextureAtlasFile() {

        final JFileChooser fch = new JFileChooser();
        int res;

        fch.setCurrentDirectory( new File( workDirectory + "//data" ) );
        fch.setFileFilter( ffAtlas );

        res = fch.showOpenDialog(this);

        if ( res != JFileChooser.APPROVE_OPTION )
            return;

        File fl = fch.getSelectedFile();

        ApplicationSettings.get().setLastDirectory( fl.getParent() );

        StringBuilder sb = new StringBuilder( fl.getAbsolutePath() );
        int li = workDirectory.length() + 1; // +1  to don't forget about '/'
        sb = sb.delete(0, li );

        tfTexAtlasFilePath.setText( sb.toString() );
        ApplicationSettings.get().setTextureAtlasFile( tfTexAtlasFilePath.getText() );
    }


    void loadJTree() {

        if ( scene == null ) {
            treeSceneModel = null;
            treeScene.setModel( new DefaultTreeModel( null ) );
            return;
        }

        SceneTreeNode rootNode = new SceneTreeNode(SceneTreeNode.Type.ROOT, scene );

        treeSceneModel = new DefaultTreeModel( rootNode );
        SceneTreeNode layersGroupNode = new SceneTreeNode( SceneTreeNode.Type.LAYERS_GROUP, scene );
        loadLayersNodes(layersGroupNode);
        rootNode.add( layersGroupNode );
        treeScene.setModel( treeSceneModel );
        Gdx.app.log("MainGui.loadJTree", " OK");
    }

    void loadLayersNodes(SceneTreeNode parentNode) {
        for (Layer l : scene.getBackLayers() ) {
            SceneTreeNode node = new SceneTreeNode(SceneTreeNode.Type.LAYER, l );
            loadLayerNodes(node);
            parentNode.add( node );
        }

    }

    void loadLayerNodes(SceneTreeNode parentNode) {
        Layer l = (Layer) parentNode.getUserObject();

        for (Actor a : l.getChildren() ) {
            if ( a instanceof TiledActor ) {
                TiledActor ta = (TiledActor) a;
                SceneTreeNode node = new SceneTreeNode(SceneTreeNode.Type.TILED_ACTOR, ta );
                if ( ta.getAag() != null ) {
                    AnimatedActorGroup aag = ta.getAag();
                    SceneTreeNode aagNode = new SceneTreeNode(SceneTreeNode.Type.AAG, aag);
                    loadAagNodes(aagNode);
                    node.add( aagNode );
                }
                parentNode.add( node );
            } else if ( a instanceof AnimatedActorGroup ) {
                AnimatedActorGroup aag = (AnimatedActorGroup) a;
                SceneTreeNode node = new SceneTreeNode(SceneTreeNode.Type.AAG, aag);
                loadAagNodes(node);
                parentNode.add( node );
            }
        }
    }

    public void loadAagNodes(SceneTreeNode parentNode ) {
        AnimatedActorGroup parentAag = (AnimatedActorGroup) parentNode.getUserObject();
        for ( int i = 0; i < parentAag.getChildrenCount(); i++ ) {
            AnimatedActorGroup aag = parentAag.getChild( i );
            SceneTreeNode node = new SceneTreeNode(SceneTreeNode.Type.AAG, aag );
            loadAagNodes( node );
            parentNode.add( node );
        }
    }

    void processJTreeSelection( TreeSelectionEvent e ) {
        propertiesCellEditor.cancelEditing();
        tableProperties.setModel( defaultTableModel );
        SceneTreeNode node = (SceneTreeNode) treeScene.getLastSelectedPathComponent();
        if ( node == null )
            return;

        switch ( node.getType() ) {
            case ROOT:
                scenePropertiesTableModel.setScene( scene );
                tableProperties.setModel( scenePropertiesTableModel );
                break;
            case LAYER:
                layerPropertiesTableModel.setLayer((Layer) node.getUserObject());
                tableProperties.setModel( layerPropertiesTableModel );
                break;
            case MODEL_DESC_HANDLER:
                break;
            case MODEL_ITEM:
                break;
            case LAYERS_GROUP:
                break;
            case TILED_ACTOR:
                tiledActorPropertiesTableModel.setTiledActor((TiledActor) node.getUserObject());
                tableProperties.setModel( tiledActorPropertiesTableModel );
                break;
            case AAG:
                aagPropertiesTableModel.setAag((org.skr.gdx.physmodel.animatedactorgroup.AnimatedActorGroup)
                        node.getUserObject());
                aagPropertiesTableModel.setScene( scene );
                tableProperties.setModel( aagPropertiesTableModel );
                break;
        }

        editorScreen.setControllableObject( node.getUserObject() );
    }

    private static final NewNodeSelectorDialog newNodeDlg = new NewNodeSelectorDialog();
    static {
        newNodeDlg.setTitle("Add New Node");
    }



    void addNode() {
        if ( scene == null )
            return;

        SceneTreeNode parentNode = (SceneTreeNode) treeScene.getLastSelectedPathComponent();
        Object parentObject = parentNode.getUserObject();
        newNodeDlg.setSize( 500, 450);
        boolean res = false;

        switch( parentNode.getType() ) {
            case ROOT:
                res = newNodeDlg.execute( scene  );
                break;
            case MODEL_DESC_HANDLER:
                break;
            case MODEL_ITEM:
                break;
            case LAYERS_GROUP:
                res = newNodeDlg.execute( scene, SceneTreeNode.Type.LAYER );
                break;
            case LAYER:
                res = newNodeDlg.execute( scene, SceneTreeNode.Type.TILED_ACTOR,
                        SceneTreeNode.Type.AAG );
                break;
            case TILED_ACTOR:
                if ( ((TiledActor) parentObject ).getAag() != null )
                    break;
                res = newNodeDlg.execute( scene, SceneTreeNode.Type.AAG );
                break;
            case AAG:
                res = newNodeDlg.execute( scene, SceneTreeNode.Type.AAG );
                break;
        }

        Gdx.app.log("MainGui.addNode", " Res: " + res );

        if ( !res )
            return;

        SceneTreeNode newNode = newNodeDlg.getNewNode();

        if ( newNode == null ) {
            Gdx.app.error("MainGui.addNode", "null NewNode");
            return;
        }

        Gdx.app.log("MainGui.addNode", " New node type: " + newNode.getType() );

        switch ( newNode.getType() ) {

            case ROOT:
                break;
            case MODEL_DESC_HANDLER:
                break;
            case MODEL_ITEM:
                break;
            case LAYERS_GROUP:
                break;
            case LAYER:
                scene.addBackLayer((Layer) newNode.getUserObject());
                break;
            case TILED_ACTOR:
                ((Layer) parentObject).addTiledActor((TiledActor) newNode.getUserObject());
                break;
            case AAG:
                if ( parentObject instanceof TiledActor ) {
                    ((TiledActor) parentObject).setAag((AnimatedActorGroup) newNode.getUserObject());
                } else if ( parentObject instanceof Group) {
                    ((Group) parentObject ).addActor((Actor) newNode.getUserObject());
                }
                break;
        }

        parentNode.add( newNode );
        treeSceneModel.nodeStructureChanged( parentNode );
        treeSceneModel.nodeChanged( parentNode );
        treeScene.setSelectionPath( new TreePath( newNode.getPath() ) );
        processJTreeSelection( null );
    }

    void removeNode() {
        if ( scene == null )
            return;

        SceneTreeNode currentNode = (SceneTreeNode) treeScene.getLastSelectedPathComponent();
        SceneTreeNode parentNode = (SceneTreeNode) currentNode.getParent();
        Object parentObject = null;
        if ( parentNode != null )
            parentObject = parentNode.getUserObject();

        switch ( currentNode.getType() ) {
            case ROOT:
                return;
            case MODEL_DESC_HANDLER:
                break;
            case MODEL_ITEM:
                break;
            case LAYERS_GROUP:
                return;
            case LAYER:
                Layer remLayer = (Layer) currentNode.getUserObject();
                scene.removeLayer( remLayer );
                break;
            case TILED_ACTOR:
                TiledActor remTiledActor = (TiledActor) currentNode.getUserObject();
                Layer parentLayer = (Layer) parentNode.getUserObject();
                parentLayer.removeTiledActor( remTiledActor );
                break;
            case AAG:
                if ( parentNode.getType() == SceneTreeNode.Type.TILED_ACTOR) {
                    TiledActor parentTa = (TiledActor) parentNode.getUserObject();
                    parentTa.setAag(null);
                    break;
                } else if ( parentObject instanceof Group ) {
                    Group group = (Group) parentObject;
                    group.removeActor( (AnimatedActorGroup) currentNode.getUserObject() );
                }
        }

        if ( parentNode != null ) {
            parentNode.remove(currentNode);
            treeSceneModel.nodeStructureChanged(parentNode);
            treeScene.setSelectionPath(new TreePath(parentNode.getPath()));
            processJTreeSelection(null);
        }
    }

    void sceneToGui() {

        loadJTree();

        DefaultTreeModel model = (DefaultTreeModel) treeScene.getModel();
        SceneTreeNode root = (SceneTreeNode) model.getRoot();
        treeScene.setSelectionPath( new TreePath( root.getPath() ));
        processJTreeSelection(null);
        tableProperties.updateUI();
    }

    // ====================== static ================================

    private static String workDirectory = "" ;

    //======================= main ================================

    public static void main(String [] args) {

        workDirectory = System.getProperty("user.dir");

        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                MainGui instance = new MainGui();
                instance.setVisible(true);
            }
        });
    }
}
