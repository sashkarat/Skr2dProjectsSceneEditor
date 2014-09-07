package org.skr.Skr2dProjectsSceneEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import org.skr.Skr2dProjectsSceneEditor.PropertiesTableModel.*;
import org.skr.Skr2dProjectsSceneEditor.gdx.SkrGdxAppSceneEditor;
import org.skr.Skr2dProjectsSceneEditor.gdx.controllers.ModelsController;
import org.skr.Skr2dProjectsSceneEditor.gdx.screens.EditorScreen;
import org.skr.gdx.Environment;
import org.skr.gdx.PhysWorld;
import org.skr.gdx.editor.Controller;
import org.skr.gdx.physmodel.animatedactorgroup.AagDescription;
import org.skr.gdx.physmodel.animatedactorgroup.AnimatedActorGroup;
import org.skr.gdx.scene.*;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
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
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
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
    private JTextField tfTexAtlasFilePath;
    private JButton btnBrowseTexAtlasFile;
    private JPanel panelProperties;
    private JButton btnAddNode;
    private JButton btnRemoveNode;
    private JCheckBox chbDisplayGrid;
    private JCheckBox chbDisplayGridFirst;
    private JCheckBox chbDebugRender;
    private JButton btnDuplicateNode;
    private JLabel lblCameraDataInfo;
    private JComboBox comboDebugViewRectResolution;
    private JCheckBox chbActivePhysics;
    private JTextField tfVelocityIterations;
    private JTextField tfPositionIterations;
    private JTextField tfTargetFps;
    private JButton btnUpdatePhParameters;
    private JButton btnDoPhysWorldStep;
    private JComboBox comboSelectionMode;
    private JButton btnSetSelectionGroup;


    private Timer cameraDataRefreshTimer;

    private SkrGdxAppSceneEditor gApp;
    private PhysScene scene;
    private String sceneAbsolutePath = "";
    private DefaultTreeModel treeSceneModel;
    private DefaultTableModel defaultTableModel = new DefaultTableModel();

    private PropertiesCellEditor propertiesCellEditor;
    private ScenePropertiesTableModel scenePropertiesTableModel;
    private LayersGroupPropertiesTableModel layersGroupPropertiesTableModel;
    private LayerPropertiesTableModel layerPropertiesTableModel;
    private TiledActorPropertiesTableModel tiledActorPropertiesTableModel;
    private AagPropertiesTableModel aagPropertiesTableModel;
    private PhysModelItemPropertiesTableModel modelItemPropertiesTableModel;
    private ModelsControllerPropertiesTableModel modelsControllerPropertiesTableModel;

    private EditorScreen editorScreen;

    private SceneTreeNode selectionGroupsNode;
    private SceneTreeNode rootSceneNode;
    private SceneTreeNode modelsNode;

    private class SceneSnapshot {
        PhysSceneDescription description;
    }


    private Array< SceneSnapshot > snapshots = new Array<SceneSnapshot>();
    private int lastSnapshotIndex = -1;
    private boolean sceneChanged = false;
    private Timer snapshotTimer = new Timer( 2000, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            onSnapshotTimer();
        }
    });

    MainGui() {




        cameraDataRefreshTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCameraDataInfo();
            }
        });


        for ( EditorScreen.SelectionMode mode : EditorScreen.SelectionMode.values() ) {
            comboSelectionMode.addItem( mode );
        }

        rootPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                comboSelectionMode.setSelectedItem( EditorScreen.SelectionMode.MODEL_ITEM);
                changeSelectionMode();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        rootPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                comboSelectionMode.setSelectedItem( EditorScreen.SelectionMode.DISABLED);
                changeSelectionMode();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_0, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        gApp = new SkrGdxAppSceneEditor();
        final LwjglAWTCanvas gdxCanvas = new LwjglAWTCanvas( gApp );

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setContentPane(rootPanel);
        panelGdx.add(gdxCanvas.getCanvas(), BorderLayout.CENTER);
        pack();
        setSize(1280, 800);
        ApplicationSettings.load();
        updateGuiFromSetting();

        loadJTree();

        MainGuiWindowListener guiWindowListener = new MainGuiWindowListener( this );
        addWindowListener(guiWindowListener);
        guiWindowListener.addTimer(cameraDataRefreshTimer);
        guiWindowListener.addTimer( snapshotTimer );
        PhysScene.setSceneStateListener( sceneStateListener );

        snapshotTimer.start();

        for ( EditorScreen.DebugResolution dr : EditorScreen.DebugResolution.values() ) {
            comboDebugViewRectResolution.addItem( dr );
        }

        scenePropertiesTableModel = new ScenePropertiesTableModel( treeScene );
        layersGroupPropertiesTableModel = new LayersGroupPropertiesTableModel( treeScene );
        layerPropertiesTableModel = new LayerPropertiesTableModel( treeScene );
        tiledActorPropertiesTableModel = new TiledActorPropertiesTableModel( treeScene );
        aagPropertiesTableModel=  new AagPropertiesTableModel( treeScene );
        modelItemPropertiesTableModel = new PhysModelItemPropertiesTableModel( treeScene );
        modelsControllerPropertiesTableModel = new ModelsControllerPropertiesTableModel( treeScene );

        JTableHeader th = tableProperties.getTableHeader();
        panelProperties.add(th, BorderLayout.NORTH);
        propertiesCellEditor = new PropertiesCellEditor();
        tableProperties.setDefaultEditor(
                PropertiesBaseTableModel.Property.class,
                propertiesCellEditor );

        propertiesCellEditor.addCellEditorListener( new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                makeSnapshot();
            }

            @Override
            public void editingCanceled(ChangeEvent e) {

            }
        });
        tableProperties.setDefaultRenderer(PropertiesBaseTableModel.Property.class,
                new PropertiesTableCellRenderer() );


        setupMenu();
        setupGdxApp();
        setTitle("Skr2DProjectsSceneEditor");


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
                processNewNodeCreation();
            }
        });
        btnRemoveNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processNodesRemoving();
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
                Environment.debugRender = chbDebugRender.isSelected();
            }
        });
        btnDuplicateNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processNodesDuplication();
            }
        });
        comboDebugViewRectResolution.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeDebugViewRect();
            }
        });
        chbActivePhysics.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPhysicsActive();
            }
        });
        btnDoPhysWorldStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doPhysWorldStep();
            }

        });
        btnUpdatePhParameters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePhysWorldParameters();
            }
        });
        comboSelectionMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeSelectionMode();
            }
        });
        btnSetSelectionGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSelectionGroup();
            }
        });
    }


    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Scene");

        JMenuItem mnuItem = new JMenuItem("New");
        mnuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK) );
        mnuItem.addActionListener( new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewScene();
            }
        });
        menu.add( mnuItem );

        mnuItem = new JMenuItem("Load");
        mnuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK) );
        mnuItem.addActionListener( new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadScene();
            }
        });
        menu.add(mnuItem);


        mnuItem = new JMenuItem("Save");
        mnuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK) );
        mnuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveScene();
            }
        });
        menu.add(mnuItem);


        mnuItem = new JMenuItem("Save As ...");
        mnuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK ) );
        mnuItem.addActionListener( new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSceneAs();
            }
        });
        menu.add(mnuItem);
        menu.addSeparator();


        menu.addSeparator();
        mnuItem = new JMenuItem("Exit");
        mnuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK) );
        mnuItem.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processExitCall();
            }
        });
        menu.add(mnuItem);

        menuBar.add( menu );


        menu = new JMenu("Edit");

        mnuItem = new JMenuItem("Undo");
        mnuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK) );
        mnuItem.addActionListener( new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processUndo();
            }
        });
        menu.add(mnuItem);

        mnuItem = new JMenuItem("Redo");
        mnuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK) );
        mnuItem.addActionListener( new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processRedo();
            }
        });
        menu.add(mnuItem);

        menuBar.add( menu );
        setJMenuBar(menuBar);

    }

    private void setupGdxApp() {
        Gdx.app.postRunnable( new Runnable() {
            @Override
            public void run() {
                editorScreen = gApp.getEditorScreen();
                chbDebugRender.setSelected( Environment.debugRender );
                chbDisplayGrid.setSelected( editorScreen.isDisplayGrid() );
                chbDisplayGridFirst.setSelected( editorScreen.isDisplayGridFirst() );
                cameraDataRefreshTimer.start();

                editorScreen.getModelsController().setControlPointListener( new Controller.controlPointListener() {
                    @Override
                    public void changed(Object controlledObject, Controller.ControlPoint controlPoint) {
                        onModelsChangedByController();
                    }
                });

                editorScreen.getAagController().setControlPointListener( new Controller.controlPointListener() {
                    @Override
                    public void changed(Object controlledObject, Controller.ControlPoint controlPoint) {
                        onAagChangedByController();
                    }
                });

                editorScreen.setSelectionListener( new EditorScreen.SelectionListener() {
                    @Override
                    public void itemsSelected(Object object, boolean ctrl) {
                        onItemSelectedByScreen( object, ctrl );
                    }
                });

                modelsControllerPropertiesTableModel.setModelsController( editorScreen.getModelsController() );

            }
        });
    }


    void updateGuiFromSetting() {
        tfTexAtlasFilePath.setText( ApplicationSettings.get().getTextureAtlasFile() );
    }


    // Scene project functions

    private static final FileNameExtensionFilter ffScene = new FileNameExtensionFilter("PhysScene files:", "physscene");
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
        editorScreen.setControllableObject(null, EditorScreen.ControllableObjectType.NONE);
        editorScreen.getModelsController().clearSelection();
    }


    void createNewScene() {

        if ( scene != null ) {
            int n = JOptionPane.showOptionDialog(this, "Do you want to close current scene?", "Create  new scene",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null );

            if ( n != 0 ) {
                return;
            }
        }

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
                editorScreen.setScene(scene);
                MainGui.this.sceneToGui();
                resetSnapshotHistory();
                makeSnapshot();
            }
        });
    }





    void loadScene() {

        if ( scene != null ) {
            int n = JOptionPane.showOptionDialog(this, "Do you want to close current scene?", "Load scene",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null );

            if ( n != 0 ) {
                return;
            }
        }

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
                resetSnapshotHistory();
                makeSnapshot();
            }
        });
    }


    boolean saveScene() {
        if ( scene == null )
            return false;
        if ( sceneAbsolutePath.isEmpty() ) {
            return saveSceneAs();
        }

        PhysScene.saveToFile(scene, Gdx.files.absolute(sceneAbsolutePath) );
        return true;
    }

    boolean saveSceneAs() {

        if ( scene == null )
            return false;

        final JFileChooser fch = new JFileChooser();
        int res;

        fch.setCurrentDirectory( new File( workDirectory + "//data" ) );
        fch.setFileFilter( ffScene );

        res = fch.showSaveDialog(this);

        if ( res != JFileChooser.APPROVE_OPTION )
            return false;

        File fl = fch.getSelectedFile();

        ApplicationSettings.get().setLastDirectory( fl.getParent() );

        sceneAbsolutePath = fl.getAbsolutePath();

        if ( !sceneAbsolutePath.toLowerCase().endsWith( "." + ffScene.getExtensions()[0]) ) {
            sceneAbsolutePath += ("." + ffScene.getExtensions()[0]);
        }


        Gdx.app.log("MainGui.saveSceneAs", "Path: " + sceneAbsolutePath);


        PhysScene.saveToFile( scene, Gdx.files.absolute(sceneAbsolutePath) );
        return true;
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

        rootSceneNode = new SceneTreeNode(SceneTreeNode.Type.ROOT, scene );

        treeSceneModel = new DefaultTreeModel( rootSceneNode );


        modelsNode = new SceneTreeNode(SceneTreeNode.Type.MODELS, scene.getModelDescriptionHandlers(), ": MODELS" );
        loadModelsGroup( modelsNode );
        rootSceneNode.add(modelsNode);

        SceneTreeNode layersGroupNode = new SceneTreeNode( SceneTreeNode.Type.LAYERS_GROUP,
                scene.getFrontLayersGroup() );
        loadLayersGroupNodes(layersGroupNode);
        rootSceneNode.add(layersGroupNode);

        layersGroupNode = new SceneTreeNode(SceneTreeNode.Type.LAYERS_GROUP,
                scene.getBackLayersGroup() );
        loadLayersGroupNodes( layersGroupNode );
        rootSceneNode.add(layersGroupNode);

        selectionGroupsNode = new SceneTreeNode(SceneTreeNode.Type.SELECTION_GROUPS,
                scene.getSelectionGroups(), ": SELECTION GROUPS" );
        loadSelectionGroupsNodes( selectionGroupsNode );

        rootSceneNode.add(selectionGroupsNode);



        treeScene.setModel( treeSceneModel );
        Gdx.app.log("MainGui.loadJTree", " OK");
    }

    void loadSelectionGroupsNodes( SceneTreeNode parentNode ) {
        for ( String key : scene.getSelectionGroups().keySet() ) {

            SceneTreeNode selectionGroupNode = new SceneTreeNode(SceneTreeNode.Type.SELECTION_GROUP,
                    scene.getSelectionGroups().get( key ), key );
            loadSelectionGroupNodes( selectionGroupNode );
            parentNode.add( selectionGroupNode );

        }
    }

    void loadSelectionGroupNodes( SceneTreeNode parenNode ) {
        Array< PhysModelItem > modelItems = (Array<PhysModelItem>) parenNode.getUserObject();
        for ( PhysModelItem mi : modelItems ) {
            SceneTreeNode modelItemNode = new SceneTreeNode(SceneTreeNode.Type.MODEL_ITEM, mi );
            parenNode.add( modelItemNode );
        }
    }


    void loadLayersGroupNodes(SceneTreeNode parentNode) {
        Group layersGroup = (Group) parentNode.getUserObject();
        for (Actor a : layersGroup.getChildren() ) {
            if ( !( a instanceof Layer) )
                continue;
            Layer l = (Layer) a;
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
                loadTiledActorNodes( node );
                parentNode.add( node );
            } else if ( a instanceof AnimatedActorGroup ) {
                AnimatedActorGroup aag = (AnimatedActorGroup) a;
                SceneTreeNode node = new SceneTreeNode(SceneTreeNode.Type.AAG, aag);
                loadAagNodes(node);
                parentNode.add( node );
            }
        }
    }

    public void loadTiledActorNodes( SceneTreeNode parentNode ) {
        TiledActor ta = (TiledActor) parentNode.getUserObject();
        if ( ta.getAag() != null ) {
            AnimatedActorGroup aag = ta.getAag();
            SceneTreeNode aagNode = new SceneTreeNode(SceneTreeNode.Type.AAG, aag);
            loadAagNodes(aagNode);
            parentNode.add( aagNode );
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

    public void loadModelsGroup( SceneTreeNode parentNode ) {
        Array<PhysModelDescriptionHandler> modelHandlers =
                (Array<PhysModelDescriptionHandler>) parentNode.getUserObject();
        for ( PhysModelDescriptionHandler mdh : modelHandlers ) {
            SceneTreeNode dhNode = new SceneTreeNode(SceneTreeNode.Type.MODEL_DESC_HANDLER, mdh );
            loadModelDescriptionNodes( dhNode );
            parentNode.add( dhNode );
        }
    }

    public void loadModelDescriptionNodes( SceneTreeNode parentNode ) {
        PhysModelDescriptionHandler mdh = (PhysModelDescriptionHandler) parentNode.getUserObject();
        String uuidString = mdh.getModelDesc().getUuid();
        for ( Actor a :  scene.getChildren() ) {
            if ( !(a instanceof PhysModelItem))
                continue;
            PhysModelItem modelItem = (PhysModelItem) a;
            if ( uuidString.compareTo(modelItem.getModel().getUuid().toString()) != 0 )
                continue;
            SceneTreeNode node = new SceneTreeNode(SceneTreeNode.Type.MODEL_ITEM, modelItem );
            modelItem.setLiveBasePoint( true );
            parentNode.add( node );
        }
    }


    SceneTreeNode findNode(SceneTreeNode parentNode, Object object ) {

        if ( parentNode == null )
            parentNode = (SceneTreeNode) treeSceneModel.getRoot();

        if ( parentNode.getUserObject() == object ) {
            return parentNode;
        }

        for ( int index = 0; index < parentNode.getChildCount(); index ++ ) {
            SceneTreeNode tn = (SceneTreeNode) parentNode.getChildAt( index );
            tn = findNode( tn, object);
            if ( tn != null )
                return tn;
        }
        return null;
    }

    void selectSingleNode( SceneTreeNode node ) {
        TreePath tp = new TreePath( node.getPath() );
        treeScene.setSelectionPath( tp );
        treeScene.scrollPathToVisible( tp );
        processJTreeSelection( null );
    }

    void selectSingleObject( Object object ) {
        SceneTreeNode node = findNode( null, object );
        if ( node == null )
            return;
        selectSingleNode( node );
    }

    void addObjectToSelection( Object object) {
        SceneTreeNode node = findNode( null,  object );
        if ( node == null )
            return;

        boolean rm = false;

        for ( TreePath tp : treeScene.getSelectionPaths() ) {
            if ( tp.getLastPathComponent() == node ) {
                treeScene.removeSelectionPath( tp );
                rm = true;
                break;
            }
        }

        if ( ! rm ) {
            treeScene.addSelectionPath( new TreePath( node.getPath() ) ) ;
        }

        processJTreeSelection( null );
    }

    void processJTreeSelection( TreeSelectionEvent e ) {
        if ( scene == null )
            return;

        propertiesCellEditor.cancelEditing();
        tableProperties.setModel( defaultTableModel );

        if ( treeScene.getSelectionPaths() == null )
            return;

        if ( treeScene.getSelectionPaths().length > 1 ) {
            processJTreeMultiSelection();
            return;
        }

        SceneTreeNode node = (SceneTreeNode) treeScene.getLastSelectedPathComponent();
        if ( node == null )
            return;

        EditorScreen.ControllableObjectType coType = EditorScreen.ControllableObjectType.NONE;

        switch ( node.getType() ) {
            case ROOT:
                scenePropertiesTableModel.setScene( scene );
                tableProperties.setModel( scenePropertiesTableModel );
                break;
            case LAYER:
                layerPropertiesTableModel.setLayer((Layer) node.getUserObject());
                tableProperties.setModel( layerPropertiesTableModel );
                coType = EditorScreen.ControllableObjectType.LAYER;
                break;
            case MODELS:
                break;
            case MODEL_DESC_HANDLER:
                break;
            case MODEL_ITEM:
                modelItemPropertiesTableModel.setModelItem((PhysModelItem) node.getUserObject());
                tableProperties.setModel( modelItemPropertiesTableModel );
                editorScreen.getModelsController().clearSelection();
                coType = EditorScreen.ControllableObjectType.MODEL_ITEM;
                break;
            case LAYERS_GROUP:
                layersGroupPropertiesTableModel.setGroup((Group) node.getUserObject());
                tableProperties.setModel(layersGroupPropertiesTableModel);
                break;
            case TILED_ACTOR:
                tiledActorPropertiesTableModel.setTiledActor((TiledActor) node.getUserObject());
                tableProperties.setModel( tiledActorPropertiesTableModel );
                coType = EditorScreen.ControllableObjectType.TILED_ACTOR;
                break;
            case AAG:
                aagPropertiesTableModel.setAag((org.skr.gdx.physmodel.animatedactorgroup.AnimatedActorGroup)
                        node.getUserObject());
                aagPropertiesTableModel.setScene( scene );
                tableProperties.setModel( aagPropertiesTableModel );
                coType = EditorScreen.ControllableObjectType.AAG;
                break;
            case SELECTION_GROUPS:
                break;
            case SELECTION_GROUP:
                tableProperties.setModel( modelsControllerPropertiesTableModel );
                editorScreen.getModelsController().clearSelection();
                coType = EditorScreen.ControllableObjectType.SELECTION_ARRAY;
                Gdx.app.postRunnable( new Runnable() {
                    @Override
                    public void run() {
                        modelsControllerPropertiesTableModel.fireTableDataChanged();
                    }
                });
                break;
        }

        editorScreen.setControllableObject( node.getUserObject(), coType );
    }


    void processJTreeMultiSelection() {

        SceneTreeNode.Type type = cleanupJTreeSelection();

        final TreePath [] selectionPaths = treeScene.getSelectionPaths();
        if ( selectionPaths == null )
            return;
        if ( selectionPaths.length == 1 ) {
            processJTreeSelection( null );
            return;
        }

        if ( type != SceneTreeNode.Type.MODEL_ITEM && type != SceneTreeNode.Type.SELECTION_GROUP )
            return;
        final EditorScreen.ControllableObjectType coType = ( type == SceneTreeNode.Type.MODEL_ITEM)?
                EditorScreen.ControllableObjectType.MODEL_ITEM : EditorScreen.ControllableObjectType.SELECTION_ARRAY;

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                editorScreen.getModelsController().clearSelection();
                for (TreePath tp : selectionPaths) {
                    SceneTreeNode node = (SceneTreeNode) tp.getLastPathComponent();
                    editorScreen.setControllableObject(node.getUserObject(), coType );
                }
            }
        });

        tableProperties.setModel( modelsControllerPropertiesTableModel );
        Gdx.app.postRunnable( new Runnable() {
            @Override
            public void run() {
                modelsControllerPropertiesTableModel.fireTableDataChanged();
            }
        });
    }

    SceneTreeNode.Type cleanupJTreeSelection() {
        TreePath [] selectionPaths = treeScene.getSelectionPaths();

        Array<TreePath> pathsToRemove = new Array<TreePath>();
        SceneTreeNode firstNode = (SceneTreeNode) selectionPaths[0].getLastPathComponent();
        for ( TreePath tp : selectionPaths ) {
            SceneTreeNode node = (SceneTreeNode) tp.getLastPathComponent();
            if ( node.getType() != firstNode.getType() ) {
                pathsToRemove.add( tp );
            }
        }
        for ( TreePath tp : pathsToRemove ) {
            treeScene.removeSelectionPath( tp );
        }

        return firstNode.getType();
    }


    private static final SelectionGroupNameDialog selectionGroupNameDlg = new SelectionGroupNameDialog();
    private static final NewNodeSelectorDialog newNodeDlg = new NewNodeSelectorDialog();
    static {
        newNodeDlg.setTitle("Add New Node");
        selectionGroupNameDlg.setTitle("Selection Group Name");
    }

    void processNewNodeCreation() {
        if ( scene == null )
            return;

        SceneTreeNode parentNode = (SceneTreeNode) treeScene.getLastSelectedPathComponent();
        Object parentObject = parentNode.getUserObject();
//        newNodeDlg.setSize( 500, 450);
        boolean res = false;

        switch( parentNode.getType() ) {
            case ROOT:
                res = newNodeDlg.execute( scene  );
                break;
            case MODELS:
                res = newNodeDlg.execute( scene, SceneTreeNode.Type.MODELS );
                break;
            case MODEL_DESC_HANDLER:
                SceneTreeNode modItemNode = createNewModelItem(
                        (PhysModelDescriptionHandler) parentNode.getUserObject());
                if ( modItemNode == null )
                    return;
                parentNode.add( modItemNode );
                updateTree( modItemNode, parentNode );
                return;

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
            case SELECTION_GROUPS:
                break;
            case SELECTION_GROUP:
                break;
        }

        Gdx.app.log("MainGui.processNewNodeCreation", " Res: " + res );

        if ( !res )
            return;

        SceneTreeNode newNode = newNodeDlg.getNewNode();

        if ( newNode == null ) {
            Gdx.app.error("MainGui.processNewNodeCreation", "null NewNode");
            return;
        }

        Gdx.app.log("MainGui.processNewNodeCreation", " New node type: " + newNode.getType() );

        switch ( newNode.getType() ) {

            case ROOT:
                break;
            case MODELS:
                break;
            case MODEL_DESC_HANDLER:
                scene.getModelDescriptionHandlers().add((PhysModelDescriptionHandler) newNode.getUserObject() );
                break;
            case MODEL_ITEM:
                break;
            case LAYERS_GROUP:
                break;
            case LAYER:
                Group layersGroup = (Group) parentNode.getUserObject();
                layersGroup.addActor((Actor) newNode.getUserObject());
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
            case SELECTION_GROUPS:
                break;
            case SELECTION_GROUP:
                break;
        }

        parentNode.add( newNode );
        updateTree( newNode, parentNode );
        makeSnapshot();
    }

    private void updateTree( SceneTreeNode newNode, SceneTreeNode parentNode ) {
        treeSceneModel.nodeStructureChanged( parentNode );
        treeSceneModel.nodeChanged( parentNode );
        treeScene.setSelectionPath( new TreePath( newNode.getPath() ) );
        processJTreeSelection( null );
    }

    public SceneTreeNode createNewModelItem( PhysModelDescriptionHandler mdh ) {
        PhysModelItem modelItem = scene.addModelItem( mdh );
        if ( modelItem == null )
            return  null;
        return new SceneTreeNode(SceneTreeNode.Type.MODEL_ITEM, modelItem );
    }



    void processNodesDuplication() {

        if ( scene == null )
            return;


        TreePath [] selectionPaths = treeScene.getSelectionPaths();

        for ( TreePath tp : selectionPaths ) {

            SceneTreeNode node = (SceneTreeNode) tp.getLastPathComponent();
            SceneTreeNode parentNode = (SceneTreeNode) node.getParent();
            SceneTreeNode nodeCpy = null;
            Layer layer;

            switch (node.getType()) {

                case ROOT:
                    break;
                case MODELS:
                    break;
                case MODEL_DESC_HANDLER:
                    break;
                case MODEL_ITEM:
                    if ( parentNode.getType() == SceneTreeNode.Type.MODEL_DESC_HANDLER ) {
                        PhysModelItem mi = (PhysModelItem) node.getUserObject();
                        PhysModelItemDescription desc = mi.getDescription();
                        desc.setId(scene.genModelItemId());
                        desc.setName(desc.getName() + "Cpy");
                        PhysModelItem miCpy = scene.addModelItem( desc );
                        nodeCpy = new SceneTreeNode(SceneTreeNode.Type.MODEL_ITEM, miCpy );
                    } else if ( parentNode.getType() == SceneTreeNode.Type.SELECTION_GROUP ) {
                        SceneTreeNode realParentNode = findNode( modelsNode, node.getUserObject() );
                        if ( realParentNode == null )
                            continue;
                        PhysModelItem mi = (PhysModelItem) node.getUserObject();
                        PhysModelItemDescription desc = mi.getDescription();
                        desc.setId(scene.genModelItemId());
                        desc.setName(desc.getName() + "Cpy");
                        PhysModelItem miCpy = scene.addModelItem( desc );
                        Array<PhysModelItem> selectionGroup = (Array<PhysModelItem>) parentNode.getUserObject();
                        selectionGroup.add( miCpy );
                        SceneTreeNode nodeRealCopy = new SceneTreeNode(SceneTreeNode.Type.MODEL_ITEM, miCpy );
                        realParentNode.add( nodeRealCopy );
                        nodeCpy = new SceneTreeNode(SceneTreeNode.Type.MODEL_ITEM, miCpy );
                    }

                    break;
                case LAYERS_GROUP:
                    break;
                case LAYER:

                    layer = (Layer) node.getUserObject();
                    LayerDescription lDesc = layer.getDescription();
                    Layer lCpy = new Layer(scene);
                    lCpy.loadFromDescription(lDesc);

                    nodeCpy = new SceneTreeNode(SceneTreeNode.Type.LAYER, lCpy);
                    loadLayerNodes(nodeCpy);

                    Group layersGroup = (Group) parentNode.getUserObject();
                    layersGroup.addActor(lCpy);


                    break;
                case TILED_ACTOR:
                    layer = (Layer) parentNode.getUserObject();
                    TiledActor ta = (TiledActor) node.getUserObject();
                    TiledActorDescription tad = ta.getDescription();
                    TiledActor taCpy = new TiledActor(scene);
                    taCpy.loadFromDescription(tad);
                    nodeCpy = new SceneTreeNode(SceneTreeNode.Type.TILED_ACTOR, taCpy);
                    loadTiledActorNodes(nodeCpy);
                    layer.addTiledActor(taCpy);
                    break;
                case AAG:
                    if (!(parentNode.getUserObject() instanceof Group))
                        break;

                    AnimatedActorGroup aag = (AnimatedActorGroup) node.getUserObject();
                    AagDescription aagDesc = aag.getDescription();
                    AnimatedActorGroup aagCpy = new AnimatedActorGroup(aagDesc, scene.getAtlas());
                    nodeCpy = new SceneTreeNode(SceneTreeNode.Type.AAG, aagCpy);
                    loadAagNodes(nodeCpy);
                    Group gp = (Group) parentNode.getUserObject();
                    gp.addActor(aagCpy);
                    break;
                case SELECTION_GROUPS:
                    break;
                case SELECTION_GROUP:
                    String oldSelectionGroupName = node.getName();
                    String newSelectionGroupName = selectionGroupNameDlg.execute( oldSelectionGroupName + "Cpy");
                    if ( !scene.getSelectionGroups().containsKey( newSelectionGroupName ) ) {
                        scene.getSelectionGroups().put(newSelectionGroupName, new Array<PhysModelItem>());
                    } else {
                        break;
                    }

                    scene.getSelectionGroups().get( newSelectionGroupName ).clear();
                    scene.getSelectionGroups().get( newSelectionGroupName ).
                            addAll((Array<PhysModelItem>) node.getUserObject());

                    nodeCpy = new SceneTreeNode(SceneTreeNode.Type.SELECTION_GROUP,
                            scene.getSelectionGroups().get( newSelectionGroupName), newSelectionGroupName );
                    loadSelectionGroupNodes(nodeCpy);
                    break;
            }


            if (nodeCpy == null)
                continue;

            parentNode.add(nodeCpy);
            treeSceneModel.nodeStructureChanged(parentNode);
            treeSceneModel.nodeChanged(nodeCpy);
            treeScene.setSelectionPath(new TreePath(nodeCpy.getPath()));
        }
        processJTreeSelection( null );
        makeSnapshot();

    }



    void processNodesRemoving() {

        if ( scene == null )
            return;

        int n = JOptionPane.showOptionDialog(this, "Are you sure?", "Remove selected!",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null );

        if ( n != 0 )
            return;

        TreePath [] selectionPaths = treeScene.getSelectionPaths();

        //TODO: insert yes/no dialog

        for ( TreePath tp : selectionPaths ) {

            SceneTreeNode currentNode = (SceneTreeNode) tp.getLastPathComponent() ;
            SceneTreeNode parentNode = (SceneTreeNode) currentNode.getParent();
            Object parentObject = null;
            if ( parentNode != null )
                parentObject = parentNode.getUserObject();

            switch ( currentNode.getType() ) {
                case ROOT:
                    return;
                case MODELS:
                    return;
                case MODEL_DESC_HANDLER:
                    final PhysModelDescriptionHandler mdh = (PhysModelDescriptionHandler) currentNode.getUserObject();
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            scene.removePhysModelDescriptionHandler(mdh);
                            uploadSelectionGroupsNodes();
                        }
                    });
                    break;
                case MODEL_ITEM:

                    if ( parentNode.getType() == SceneTreeNode.Type.SELECTION_GROUP )
                        return;

                    final PhysModelItem modelItem = (PhysModelItem) currentNode.getUserObject();
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            editorScreen.getModelsController().removeModelItem( modelItem );
                            scene.removePhysModelItem(modelItem);
                            uploadSelectionGroupsNodes();
                        }
                    });

                    break;
                case LAYERS_GROUP:
                    return;
                case LAYER:
                    Layer remLayer = (Layer) currentNode.getUserObject();
                    Group layersGroup = (Group) parentNode.getUserObject();
                    layersGroup.removeActor( remLayer );
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

                    } else if ( parentObject instanceof Group ) {
                        Group group = (Group) parentObject;
                        group.removeActor( (AnimatedActorGroup) currentNode.getUserObject() );
                    }
                    break;
                case SELECTION_GROUPS:
                    return;
                case SELECTION_GROUP:
                    scene.getSelectionGroups().remove( currentNode.getName() );
                    break;
            }

            if ( parentNode != null ) {
                parentNode.remove(currentNode);
                treeSceneModel.nodeStructureChanged(parentNode);
                treeScene.setSelectionPath(new TreePath(parentNode.getPath()));
                processJTreeSelection(null);
            }
        }
        makeSnapshot();
    }

    void sceneToGui() {

        loadJTree();

        DefaultTreeModel model = (DefaultTreeModel) treeScene.getModel();
        SceneTreeNode root = (SceneTreeNode) model.getRoot();
        treeScene.setSelectionPath( new TreePath( root.getPath() ));
        processJTreeSelection(null);
        tableProperties.updateUI();
    }

    void updateCameraDataInfo() {
        OrthographicCamera camera = editorScreen.getCamera();
        lblCameraDataInfo.setText("ViewPort: "
                + camera.viewportWidth * camera.zoom + " x " + camera.viewportHeight*camera.zoom
        + " (zoom: " + camera.zoom + ")");
    }


    void changeDebugViewRect() {
        editorScreen.setDebugViewRectResolution((EditorScreen.DebugResolution) comboDebugViewRectResolution.getSelectedItem());
    }


    void setPhysicsActive() {
        if ( scene == null )
            return;

        scene.setActivePhysics( chbActivePhysics.isSelected() );
    }


    void updatePhysWorldParameters() {

        if ( scene == null )
            return;

        float fps = scene.getTargetFps();
        int vi = scene.getVelocityIterations();
        int pi = scene.getPositionIterations();

        try {
            fps = Float.parseFloat(tfTargetFps.getText());
            vi = Integer.parseInt(tfVelocityIterations.getText());
            pi = Integer.parseInt(tfPositionIterations.getText());

            final float finalFps = fps;
            final int finalVi = vi;
            final int finalPi = pi;
            Gdx.app.postRunnable( new Runnable() {
                @Override
                public void run() {
                    scene.setTargetFps( finalFps );
                    scene.setVelocityIterations( finalVi );
                    scene.setPositionIterations( finalPi );
                }
            });

        } catch ( NumberFormatException e ) {
            return;
        }
    }

    void doPhysWorldStep() {
        if ( scene == null )
            return;
        scene.doPhysWorldStep();
    }


    void onModelsChangedByController() {
        modelItemPropertiesTableModel.fireTableDataChanged();
        modelsControllerPropertiesTableModel.fireTableDataChanged();
        setSceneChanged();
    }

    void onAagChangedByController() {
        aagPropertiesTableModel.fireTableDataChanged();
        setSceneChanged();
    }


    void processExitCall() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    void changeSelectionMode() {
        EditorScreen.SelectionMode mode = (EditorScreen.SelectionMode) comboSelectionMode.getSelectedItem();
        editorScreen.setSelectionMode( mode );
    }

    void onItemSelectedByScreen( Object object, boolean ctrl ) {
        if ( !ctrl ) {
            selectSingleObject(object);
        } else {
            addObjectToSelection( object );
        }
    }

    void uploadSelectionGroupsNodes() {
        selectionGroupsNode.removeAllChildren();
        loadSelectionGroupsNodes( selectionGroupsNode );
        treeSceneModel.nodeStructureChanged( selectionGroupsNode );
    }

    void setSelectionGroup() {
        ModelsController modelsController = editorScreen.getModelsController();

        if ( modelsController.getModelItems().size == 0 )
            return;


        String name = selectionGroupNameDlg.execute("");
        if ( name == null )
            return;
        if ( name.isEmpty() )
            return;

        if ( !scene.getSelectionGroups().containsKey( name ) ) {
            scene.getSelectionGroups().put(name, new Array<PhysModelItem>());
        }


        scene.getSelectionGroups().get( name ).clear();
        scene.getSelectionGroups().get( name ).addAll( modelsController.getModelItems() );

        uploadSelectionGroupsNodes();

    }

    void setSceneChanged() {
        sceneChanged = true;
    }

    void onSnapshotTimer(){
        if ( sceneChanged ) {
            makeSnapshot();
            sceneChanged = false;
        }
    }

    void makeSnapshot() {
        if ( scene == null )
            return;

        PhysSceneDescription desc = scene.getDescription();
        if ( lastSnapshotIndex < snapshots.size - 1 )
            snapshots.removeRange( lastSnapshotIndex + 1, snapshots.size - 1 );
        SceneSnapshot snapshot = new SceneSnapshot();
        snapshot.description = desc;
        snapshots.add( snapshot );
        lastSnapshotIndex = snapshots.size - 1;
        Gdx.app.log("MainGui.makeSnapshot", "lastSnapshotIndex: " + lastSnapshotIndex + " historySize: " + snapshots.size);
    }

    void resetSnapshotHistory() {
        snapshots.clear();
        lastSnapshotIndex = -1;
    }

    void loadSnapshot( final SceneSnapshot snapshot ) {
        clear();
        scene = new PhysScene( PhysWorld.getPrimaryWorld() );
        Gdx.app.postRunnable( new Runnable() {
            @Override
            public void run() {
                scene.loadFromDescription( snapshot.description );
                editorScreen.setScene(scene);
                sceneToGui();

                Gdx.app.log("MainGui.processUndo", "lastSnapshotIndex; " + lastSnapshotIndex);
            }
        });
    }

    void processUndo() {

        if ( scene == null )
            return;

        if ( lastSnapshotIndex < 1 )
            return;

        loadSnapshot( snapshots.get( lastSnapshotIndex - 1) );
        lastSnapshotIndex--;
    }

    void processRedo() {
        if ( scene == null )
            return;

        if ( lastSnapshotIndex >= snapshots.size - 1) {
            return;
        }

        loadSnapshot( snapshots.get( lastSnapshotIndex + 1) );
        lastSnapshotIndex++;
    }

    public boolean confirmClosure() {
        if (scene != null) {
            int n = JOptionPane.showOptionDialog(this, "Do you want to save current scene?", "Close Application",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            switch ( n  ) {
                case 0:
                    return saveScene();
                case 1:
                    return true;
                case 2:
                    return false;
            }
        }
        return true;
    }

    // ====================== static ================================

    public static String workDirectory = "" ;

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
