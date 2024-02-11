package ui;

import relations.Relation;
import storage.BasisStorage;
import storage.HeytingAlgebraStorage;
import storage.RelationStorage;
import storage.SetObjectStorage;
import ui.utils.UIDialogs;
import ui.utils.UIMethods;

import javax.swing.*;
import java.awt.*;

import static ui.utils.UIConstants.*;

public class ProjectUI extends JFrame{

    JSplitPane jSplitPane = new JSplitPane();

    public ProjectUI(){
//        this.setLocationRelativeTo(null);
        setLocation(SCREENSIZE.width/5, SCREENSIZE.height/4);
        setTitle("PROJECT: "+ PROJECTNAME.toUpperCase());
        //pane attributes
        jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        jSplitPane.setResizeWeight(0.6);

        jSplitPane.setLeftComponent(leftPaneComponent());
        jSplitPane.setRightComponent(rightPaneComponent());

        add(jSplitPane);
        setVisible(true);
        setSize(1000, 600);
        addWindowListener(UIMethods.getInstance().windowEvent());
    }

    public JComponent leftPaneComponent(){
        JPanel leftPane = new JPanel();
        JComboBox<String> relsBox = new JComboBox<>(RelationStorage.getInstance().getEntityNames());
        relsBox.insertItemAt("--select relation--", 0);
        relsBox.setSelectedIndex(0);

        relsBox.setBounds(20, 10, 200,30);
        leftPane.add(relsBox);

        JButton declarationBtn = new JButton("Declaration");
        declarationBtn.setBounds(230, 10, BUTTONWIDTH, 30);
        declarationBtn.addActionListener(e -> {
            JFrame ui = INTERFACES.get("DeclarationUI");
            if(ui == null) {
                new Dialog(new DeclarationUI());
            } else {
                ui.toFront();
                ui.repaint();
            }
        });
        leftPane.add(declarationBtn);

        final String[] oldRel = {""};
        relsBox.addActionListener(e->{
            String selectedRel = relsBox.getSelectedItem().toString();

            if (relsBox.getSelectedIndex() != 0 && !oldRel[0].equals(selectedRel)) {
                oldRel[0] = selectedRel;
                Relation rel = RelationStorage.getInstance().load(selectedRel);
                RelationDisplay relationDisplay = new RelationDisplay();
                relationDisplay.setRelation(rel);
                relationDisplay.setBounds(20, 50, 350, 350);

                leftPane.removeAll();
                leftPane.add(relsBox);
                leftPane.add(declarationBtn);
                leftPane.add(relationDisplay);
                SwingUtilities.updateComponentTreeUI(leftPane);
            }
        });

        leftPane.setLayout(null);

        return leftPane;
    }
    public JComponent rightPaneComponent(){
        JComponent setObjectPanel, heytingPanel, basisPanel;

        setObjectPanel = new JPanel();
        heytingPanel = new JPanel();
        basisPanel = new JPanel();
        setObjectPanel.setBackground(BGCOLOR);
        heytingPanel.setBackground(BGCOLOR);
        basisPanel.setBackground(BGCOLOR);

        setObjectPanel.add(storageItems(""));
        heytingPanel.add(storageItems("heyting"));
        basisPanel.add(storageItems("basis"));

        //add button for new set object
        setObjectPanel.add(setObjectCreateBtn());

        //create right split pane components
        JTabbedPane rightTabbedPane = new JTabbedPane();
        rightTabbedPane.setBackground(BGCOLOR);

        rightTabbedPane.add("SetObject", setObjectPanel);
        rightTabbedPane.add("HeytingAlgebra", heytingPanel);
        rightTabbedPane.add("Basis", basisPanel);

        return rightTabbedPane;
    }

    private JList<String> storageItems(String storage){
        JList<String> entities;

        switch (storage){
            case "basis" -> entities = new JList<>(BasisStorage.getInstance().getEntityNames());
            case "heyting" -> entities = new JList<>(HeytingAlgebraStorage.getInstance().getEntityNames());
            default -> entities = new JList<>(SetObjectStorage.getInstance().getEntityNames());
        }

        entities.setBackground(LISTBGCOLOR);
        return entities;
    }

    private Component setObjectCreateBtn(){
        JButton createSetObjectBtn = new JButton("Create SetObject");
        createSetObjectBtn.addActionListener(e-> {
                UIDialogs.getInstance().createSetObjectDialog(this).setVisible(true);
        });
        return createSetObjectBtn;
    }
}
