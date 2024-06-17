package ui.utils;

import relations.Relation;
import relterm.Declaration;
import storage.*;
import ui.DeclarationUI;
import ui.RelationDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ui.utils.UIConstants.*;
import static ui.utils.UIConstants.BGCOLOR;

public class UIComponents {
    public static UIComponents UICOMPONENTS = null;
    private boolean check = false;
    JPanel leftPane = new JPanel();
    private Declaration declaration;


    public static UIComponents getInstance() {
        if (UICOMPONENTS == null) UICOMPONENTS = new UIComponents();
        return UICOMPONENTS;
    }

    public JComponent projectUILeftPane(){
        leftPane.setBackground(BGCOLOR);
        leftPane.setLayout(null);
        return leftPane;
    }

    public JComponent projectUIRightPane(JFrame frame){
        JComponent setObjectPanel, heytingPanel, basisPanel, relationPanel, declarationPanel;

        setObjectPanel = new JPanel();heytingPanel = new JPanel();basisPanel = new JPanel();
        relationPanel = new JPanel();declarationPanel = new JPanel();

        setObjectPanel.setBackground(BGCOLOR);
        heytingPanel.setBackground(BGCOLOR);
        basisPanel.setBackground(BGCOLOR);
        relationPanel.setBackground(BGCOLOR);
        declarationPanel.setBackground(BGCOLOR);


        setObjectPanel.add(storageItems(""));
        setObjectPanel.add(setObjectCreateBtn(frame));

        heytingPanel.add(storageItems("heyting"));
        basisPanel.add(storageItems("basis"));
        relationPanel.add(storageItems("relation"));

        declarationPanel.add(storageItems("declaration"));
        JButton declarationBtn = getDeclarationBtn();
        declarationPanel.add(declarationBtn);

        //create right split pane components
        JTabbedPane rightTabbedPane = new JTabbedPane();
        rightTabbedPane.setBackground(BGCOLOR);

        rightTabbedPane.add("SetObject", setObjectPanel);
        rightTabbedPane.add("HeytingAlgebra", heytingPanel);
        rightTabbedPane.add("Basis", basisPanel);
        rightTabbedPane.add("Relation", relationPanel);
        rightTabbedPane.add("Declaration", declarationPanel);

        return rightTabbedPane;
    }

    private JButton getDeclarationBtn() {
        JButton declarationBtn = new JButton("Execute");
        declarationBtn.addActionListener(e -> {
            JFrame ui = INTERFACES.get("DeclarationUI");
            if(ui == null) {
                if (declaration != null)
                    new Dialog(new DeclarationUI(declaration));
                else
                    UIDialogs.getInstance().notifyDialog("Please select a declaration");
            } else {
                ui.toFront();
                ui.repaint();
            }
        });
        return declarationBtn;
    }

    private Component setObjectCreateBtn(JFrame frame){
        JButton createSetObjectBtn = new JButton("New SetObject");
        createSetObjectBtn.addActionListener(e-> {
            UIDialogs.getInstance().createSetObjectDialog(frame).setVisible(true);
        });
        return createSetObjectBtn;
    }

    private JList<String> storageItems(String storage){
        JList<String> entities;

        switch (storage){
            case "basis" -> entities = new JList<>(BasisStorage.getInstance().getEntityNames());
            case "heyting" -> entities = new JList<>(HeytingAlgebraStorage.getInstance().getEntityNames());
            case "relation" -> entities = new JList<>(RelationStorage.getInstance().getEntityNames());
            case "declaration" -> entities = new JList<>(DeclarationStorage.getInstance().getEntityNames());
            default -> entities = new JList<>(SetObjectStorage.getInstance().getEntityNames());
        }

        entities.setBackground(LISTBGCOLOR);
        entities.addListSelectionListener(e -> {
            if (check && (storage.equals("relation") || storage.equals("declaration"))) {
                setUpLeftPane(storage, entities.getSelectedValue());
            }
            check = !check;
        });

        return entities;
    }

    private void setUpLeftPane(String storage, String item) {
        switch (storage) {
            case "relation" -> {
                Relation rel = RelationStorage.getInstance().load(item);
                RelationDisplay relationDisplay = RelationDisplay.getInstance();
                relationDisplay.setRelation(rel);
                relationDisplay.setBounds(20, 50, 350, 350);

                leftPane.removeAll();
                leftPane.add(relationDisplay);
            }
            default -> {
                declaration = DeclarationStorage.getInstance().load(item);
                JLabel dec = new JLabel(declaration.getDeclaration());
                dec.setBounds(20, 50, 350, 50);
                leftPane.removeAll();
                leftPane.add(dec);
                leftPane.updateUI();
            }
        }
    }

    public List<JComboBox<String>> varRelations(Declaration declaration, JFrame frame) {
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        List<JComboBox<String>> relsList = new ArrayList<>();

        var vars = declaration.getVars();
        var keys = vars.keySet().toArray();
        int xaxis = 30, lyaxis=20, ryaxis = 20;

        for (int i = 0; i < vars.size(); i++) {
            JComboBox<String> relsBox = new JComboBox<>(RelationStorage.getInstance().getEntityNames());
            relsBox.insertItemAt("--select relation--", 0);
            relsBox.setSelectedIndex(0);

            JLabel varL = new JLabel(keys[i].toString()+":");

            if (i % 2 == 0) {
                varL.setBounds(10, lyaxis*(i+1),30, 30);
                relsBox.setBounds(xaxis, lyaxis*(i+1), 200, 30);
                leftPanel.add(varL);
                leftPanel.add(relsBox);
            } else {
                varL.setBounds(0, ryaxis*(i),30, 30);
                relsBox.setBounds(xaxis, ryaxis*(i), 200, 30);
                rightPanel.add(varL);
                rightPanel.add(relsBox);
            }
            relsList.add(relsBox);
        }
        leftPanel.setBounds(0, 30, 280, 230);
        leftPanel.setLayout(null);
        rightPanel.setBounds(300, 30, 280, 230);
        rightPanel.setLayout(null);

        frame.add(leftPanel);
        frame.add(rightPanel);

        return relsList;
    }
}
