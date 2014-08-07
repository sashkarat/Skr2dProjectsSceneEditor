package org.skr.Skr2dProjectsSceneEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import org.skr.Skr2dProjectsSceneEditor.PropertiesTableModel.PropertiesBaseTableModel;
import org.skr.Skr2dProjectsSceneEditor.PropertiesTableModel.PropertiesCellEditor;
import org.skr.Skr2dProjectsSceneEditor.PropertiesTableModel.PropertiesTableCellRenderer;
import org.skr.Skr2dProjectsSceneEditor.PropertiesTableModel.ScenePropertiesTableModel;
import org.skr.Skr2dProjectsSceneEditor.gdx.SkrGdxAppSceneEditor;
import org.skr.gdx.PhysWorld;
import org.skr.gdx.scene.PhysScene;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
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


    SkrGdxAppSceneEditor gApp;
    PhysScene scene;
    String sceneAbsolutePath = "";

    private PropertiesCellEditor propertiesCellEditor;
    private ScenePropertiesTableModel scenePropertiesTableModel;


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

        JTableHeader th = tableProperties.getTableHeader();
        panelProperties.add(th, BorderLayout.NORTH);
        propertiesCellEditor = new PropertiesCellEditor();
        tableProperties.setDefaultEditor(
                PropertiesBaseTableModel.Property.class,
                propertiesCellEditor );
        tableProperties.setDefaultRenderer(PropertiesBaseTableModel.Property.class,
                new PropertiesTableCellRenderer() );

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
            treeScene.setModel( new DefaultTreeModel( null ) );
            return;
        }

        SceneTreeNode rootNode = new SceneTreeNode(SceneTreeNode.Type.ROOT );
        rootNode.setUserObject( scene );

        DefaultTreeModel treeSceneModel = new DefaultTreeModel( rootNode );
        SceneTreeNode layersGroupNode = new SceneTreeNode(SceneTreeNode.Type.LAYERS_GROUP );
        layersGroupNode.setUserObject( scene );
        rootNode.add( layersGroupNode );
        treeScene.setModel( treeSceneModel );
        Gdx.app.log("MainGui.loadJTree", " OK");
    }

    void processJTreeSelection( TreeSelectionEvent e ) {
        propertiesCellEditor.cancelEditing();
        SceneTreeNode node = (SceneTreeNode) treeScene.getLastSelectedPathComponent();
        if ( node == null )
            return;

        switch ( node.getType() ) {
            case ROOT:
                scenePropertiesTableModel.setScene( scene );
                tableProperties.setModel( scenePropertiesTableModel );
                break;
            case LAYER:
                break;
            case MODEL_DESC_HANDLER:
                break;
            case MODEL_ITEM:
                break;
            case LAYERS_GROUP:
                break;
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


    private static void setGuiElementEnable(Container c, boolean state) {

        Component [] cl = c.getComponents();

        for ( int i = 0; i < cl.length; i++) {

            if ( cl[i] instanceof Container) {
                setGuiElementEnable((Container) cl[i], state);
            } else {
                cl[i].setEnabled( state );
            }

        }

        c.setEnabled( state );
    }

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
