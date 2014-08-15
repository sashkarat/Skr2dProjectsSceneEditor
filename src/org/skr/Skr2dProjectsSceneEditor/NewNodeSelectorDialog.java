package org.skr.Skr2dProjectsSceneEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import org.skr.gdx.physmodel.animatedactorgroup.AnimatedActorGroup;
import org.skr.gdx.scene.Layer;
import org.skr.gdx.scene.PhysScene;
import org.skr.gdx.scene.TiledActor;

import javax.swing.*;
import java.awt.event.*;

public class NewNodeSelectorDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTabbedPane tpNodes;
    private JTextField tfLayerName;
    private JTextField tfDistance;
    private JCheckBox chbBackdrop;
    private JPanel panelLayer;
    private JPanel panelTiledActor;
    private JTextField tfTiledActorName;
    private JComboBox comboTiledActorType;
    private JTextField tfTiledActorAagPosX;
    private JTextField tfTiledActorAagPosY;
    private JTextField tfTiledActorAagNumberX;
    private JTextField tfTiledActorAagNumberY;
    private JTextField tfTileActorSpaceX;
    private JTextField tfTileActorSpaceY;
    private JCheckBox chbTiledActorCreateAagNode;
    private JPanel panelAag;
    private JTextField tfAagName;
    private JComboBox comboAagRegion;
    private JTextField tfAagWidth;
    private JTextField tfAagHeight;
    private JCheckBox chbAagKeepAspectRatio;
    private JTextField tfAagPositionX;
    private JTextField tfAagPositionY;
    private JTextField tfAagRotation;
    private JCheckBox chbDrawable;
    private JComboBox comboAagPlayMode;
    private JTextField tfAagFrameDuraration;
    private JTextField tfLayerPosX;
    private JTextField tfLayerPosY;
    private JTextField tfLayerRotation;
    private JCheckBox chbLayerFollowCameraX;
    private JCheckBox chbLayerFollowCameraY;
    private JTextField tfLayerOffsetAttenuationX;
    private JTextField tfLayerOffsetAttenuationY;
    private JCheckBox chbLayerEnableOffsetLimitX;
    private JCheckBox chbLayerEnableOffsetLimitY;
    private JTextField tfLayerOffsetLimitXMin;
    private JTextField tfLayerOffsetLimitYMin;
    private JTextField tfLayerOffsetLimitXMax;
    private JTextField tfLayerOffsetLimitYMax;


    private boolean accepted = false;
    private SceneTreeNode.Type selectedType = null;
    private SceneTreeNode newNode = null;
    private PhysScene scene = null;

    public NewNodeSelectorDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        panelLayer.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                selectedType = SceneTreeNode.Type.LAYER;
            }
        });

        panelTiledActor.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                selectedType = SceneTreeNode.Type.TILED_ACTOR;
            }
        });

        panelAag.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                selectedType = SceneTreeNode.Type.AAG;
            }
        });



        for( TiledActor.Type t : TiledActor.Type.values() ) {
            comboTiledActorType.addItem( t );
        }

        for (Animation.PlayMode pm : Animation.PlayMode.values() )
            comboAagPlayMode.addItem( pm );
    }

    private void onOK() {
        accepted = true;
        dispose();
    }

    private void onCancel() {
        accepted = false;
        dispose();
    }


    public boolean execute( PhysScene scene, SceneTreeNode.Type ... types  ) {
        tpNodes.removeAll();

        for ( SceneTreeNode.Type type :  types ) {
            switch ( type ) {

                case ROOT:
                    break;
                case MODEL_DESC_HANDLER:
                    break;
                case MODEL_ITEM:
                    break;
                case LAYERS_GROUP:
                    break;
                case LAYER:
                    tpNodes.add("Layer", panelLayer );
                    break;
                case TILED_ACTOR:
                    tpNodes.add("Tiled Actor", panelTiledActor );
                    break;
                case AAG:
                    tpNodes.add("Animated Actor Group", panelAag );
                    comboAagRegion.removeAllItems();
                    for (String tname: scene.getTextureRegionNames() )
                        comboAagRegion.addItem( tname );
                    comboAagPlayMode.setSelectedItem( Animation.PlayMode.LOOP );
                    break;
            }
        }

        this.scene = scene;
        accepted = false;
        newNode = null;
        selectedType = null;
        setVisible(true);
        if ( accepted )
            createNewNode();
        return accepted;
    }

    private void createNewNode() {

        Gdx.app.log("NewNodeSelectorDialog.createNewNode", "Selected type: " + selectedType );

        switch ( selectedType ) {

            case ROOT:
                break;
            case MODEL_DESC_HANDLER:
                break;
            case MODEL_ITEM:
                break;
            case LAYERS_GROUP:
                break;
            case LAYER:
                createLayerNode();
                break;
            case TILED_ACTOR:
                createTiledActorNode();
                break;
            case AAG:
                createAagNode();
                break;
        }
    }

    private  void createLayerNode() {
        Layer layer = new Layer( scene );
        layer.setName( tfLayerName.getText() );
        try {
            layer.setX(Float.parseFloat(tfLayerPosX.getText()));
            layer.setY(Float.parseFloat(tfLayerPosY.getText()));
            layer.setRotation(Float.parseFloat(tfLayerRotation.getText()));
            layer.getLayerSettings().setFollowCameraX( chbLayerFollowCameraX.isSelected() );
            layer.getLayerSettings().setFollowCameraY( chbLayerFollowCameraY.isSelected() );
            layer.getLayerSettings().setOffsetAttenuationX(Float.parseFloat(tfLayerOffsetAttenuationX.getText()));
            layer.getLayerSettings().setOffsetAttenuationY(Float.parseFloat(tfLayerOffsetAttenuationY.getText()));
            layer.getLayerSettings().setEnableOffsetLimitX(chbLayerEnableOffsetLimitX.isSelected());
            layer.getLayerSettings().setEnableOffsetLimitY(chbLayerEnableOffsetLimitY.isSelected());
            layer.getLayerSettings().setOffsetLimitXMin(Float.parseFloat(tfLayerOffsetLimitXMin.getText()));
            layer.getLayerSettings().setOffsetLimitXMax(Float.parseFloat(tfLayerOffsetLimitXMax.getText()));
            layer.getLayerSettings().setOffsetLimitYMin(Float.parseFloat(tfLayerOffsetLimitYMin.getText()));
            layer.getLayerSettings().setOffsetLimitYMax(Float.parseFloat(tfLayerOffsetLimitYMax.getText()));
        } catch (NumberFormatException e ) {
        }

        newNode = new SceneTreeNode(SceneTreeNode.Type.LAYER, layer );
    }

    private void createTiledActorNode() {
        TiledActor ta = new TiledActor( scene );
        ta.setName(tfTiledActorName.getText());
        ta.setType((TiledActor.Type) comboTiledActorType.getSelectedItem());
        try {
            ta.setAagPosX(Float.parseFloat(tfTiledActorAagPosX.getText()));
            ta.setAagPosY(Float.parseFloat(tfTiledActorAagPosY.getText()));
            ta.setNumberX(Integer.parseInt(tfTiledActorAagNumberX.getText()));
            ta.setNumberY(Integer.parseInt(tfTiledActorAagNumberY.getText()));
            ta.setSpaceX(Float.parseFloat(tfTileActorSpaceX.getText()));
            ta.setSpaceY(Float.parseFloat(tfTileActorSpaceY.getText()));
        } catch ( NumberFormatException e) {

        }
        newNode = new SceneTreeNode(SceneTreeNode.Type.TILED_ACTOR, ta );

        if ( chbTiledActorCreateAagNode.isSelected() ) {
            AnimatedActorGroup aag = new AnimatedActorGroup( scene.getAtlas() );
            aag.setName("New Aag");
            ta.setAag( aag );
            SceneTreeNode aagNode = new SceneTreeNode(SceneTreeNode.Type.AAG, aag );
            newNode.add( aagNode );
        }
    }

    private void createAagNode() {
        AnimatedActorGroup aag = new AnimatedActorGroup( scene.getAtlas() );
        aag.setName( tfAagName.getText() );
        aag.setTextureName((String) comboAagRegion.getSelectedItem());
        aag.updateTextures( scene.getAtlas() );
        try {
            aag.setWidth(Float.parseFloat(tfAagWidth.getText()));
            aag.setHeight(Float.parseFloat(tfAagHeight.getText()));
            aag.setKeepAspectRatio( chbAagKeepAspectRatio.isSelected() );
            aag.setX(Float.parseFloat(tfAagPositionX.getText()));
            aag.setY(Float.parseFloat(tfAagPositionY.getText()));
            aag.setRotation(Float.parseFloat(tfAagRotation.getText()));
            aag.setFrameDuration(Float.parseFloat(tfAagFrameDuraration.getText()));

        } catch ( NumberFormatException e ) {
        }
        aag.setPlayMode((Animation.PlayMode) comboAagPlayMode.getSelectedItem());
        aag.setDrawable(chbDrawable.isSelected());

        newNode =  new SceneTreeNode(SceneTreeNode.Type.AAG, aag);
    }

    public SceneTreeNode.Type getSelectedNodeType() {
        return selectedType;
    }

    public SceneTreeNode getNewNode() {
        return newNode;
    }

    public static void main(String[] args) {
        NewNodeSelectorDialog dialog = new NewNodeSelectorDialog();
        dialog.pack();
        dialog.execute( null );
        System.exit(0);
    }
}
