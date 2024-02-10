package ui;

import relations.Relation;
import storage.BasisStorage;
import storage.HeytingAlgebraStorage;
import storage.RelationStorage;
import storage.SetObjectStorage;
import ui.utils.UIConstants;
import ui.utils.UIDialogs;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ProjectUI extends JFrame{
    private JComponent setObjectPanel, heytingPanel, basisPanel;
    JSplitPane jSplitPane = new JSplitPane();

    public ProjectUI(){
        setLocation(UIConstants.SCREENSIZE.width/4, UIConstants.SCREENSIZE.height/4);
        rightPaneSetup();
        setTitle("PROJECT: "+UIConstants.PROJECTNAME.toUpperCase());

        //create right split pane components
        JTabbedPane rightTabbedPane = new JTabbedPane();
        rightTabbedPane.setBackground(UIConstants.BGCOLOR);

        rightTabbedPane.add("SetObject", setObjectPanel);
        rightTabbedPane.add("HeytingAlgebra", heytingPanel);
        rightTabbedPane.add("Basis", basisPanel);


        //pane attributes
        jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        jSplitPane.setResizeWeight(1.0);
        jSplitPane.setRightComponent(rightTabbedPane);

        addLeftComponent();

        add(jSplitPane);
        setVisible(true);
        setSize(800, 600);
    }

    public void addLeftComponent(){
        Relation rho = RelationStorage.getInstance().load("rho");
        RelationDisplay relationDisplay = new RelationDisplay();
        relationDisplay.setRelation(rho);
        jSplitPane.setLeftComponent(relationDisplay);
    }

    private JList<String> storageItems(String storage){
        JList<String> entities;

        switch (storage){
            case "basis" -> entities = new JList<>(BasisStorage.getInstance().getEntityNames());
            case "heyting" -> entities = new JList<>(HeytingAlgebraStorage.getInstance().getEntityNames());
            default -> entities = new JList<>(SetObjectStorage.getInstance().getEntityNames());
        }

        entities.setBackground(UIConstants.LISTBGCOLOR);
        return entities;
    }

    private void rightPaneSetup(){
        setObjectPanel = new JPanel();
        heytingPanel = new JPanel();
        basisPanel = new JPanel();
        setObjectPanel.setBackground(UIConstants.BGCOLOR);
        heytingPanel.setBackground(UIConstants.BGCOLOR);
        basisPanel.setBackground(UIConstants.BGCOLOR);

        setObjectPanel.add(storageItems(""));
        heytingPanel.add(storageItems("heyting"));
        basisPanel.add(storageItems("basis"));

        //add button for new set object
        setObjectPanel.add(setObjectCreateBtn());

//        SwingUtilities.updateComponentTreeUI(frame);
    }

    private Component setObjectCreateBtn(){
        JButton createSetObjectBtn = new JButton("Create SetObject");
        createSetObjectBtn.addActionListener(e-> {
                UIDialogs.getInstance().createSetObjectDialog(this).setVisible(true);
        });
        return createSetObjectBtn;
    }
}
