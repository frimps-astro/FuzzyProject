package ui.utils;

import main.Basis;
import relations.Relation;
import relterm.Declaration;
import sets.SetObject;
import storage.*;
import ui.DeclarationUI;
import ui.RelationDisplay;

import javax.crypto.spec.PSource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ui.utils.UIConstants.*;

public class UIComponents {
    public static UIComponents UICOMPONENTS = null;
    private boolean check = false;
    JPanel leftPane = new JPanel();
    JButton accept, delete, expand;
    JTabbedPane rightTabbedPane = new JTabbedPane();
    FocusListener focus;

    JFrame frame;

    private Relation rel;
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

        DATAUPDATED.put("sets", Boolean.FALSE);
        DATAUPDATED.put("heyting", Boolean.FALSE);
        DATAUPDATED.put("relation", Boolean.FALSE);
        DATAUPDATED.put("basis", Boolean.FALSE);

        this.frame = frame;
        JComponent setObjectPanel, heytingPanel, basisPanel, declarationPanel;

        setObjectPanel = new JPanel();heytingPanel = new JPanel();basisPanel = new JPanel();
        setObjectPanel.setName("sets"); heytingPanel.setName("heyting"); basisPanel.setName("basis");

        JComponent relationPanel = new JPanel();
        declarationPanel = new JPanel();
        relationPanel.setName("relation");declarationPanel.setName("declaration");

        setObjectPanel.setBackground(BGCOLOR);
        heytingPanel.setBackground(BGCOLOR);
        basisPanel.setBackground(BGCOLOR);
        relationPanel.setBackground(BGCOLOR);
        declarationPanel.setBackground(BGCOLOR);

        JScrollPane setList = new JScrollPane(storageItems("", relationPanel));
        setList.setBounds(180, 10, 150, 250);
        setObjectPanel.add(setList);
        JButton createSetBtn = setObjectCreateBtn();
        createSetBtn.setBounds(180, 300, BUTTONWIDTH,50);
        setObjectPanel.add(createSetBtn);
        setObjectPanel.setLayout(null);

        JScrollPane heytingList = new JScrollPane(storageItems("heyting", relationPanel));
        heytingList.setBounds(180, 10, 150, 250);
        heytingPanel.add(heytingList);
        heytingPanel.setLayout(null);

        JScrollPane basisList = new JScrollPane(storageItems("basis", relationPanel));
        basisList.setBounds(180, 10, 150, 250);
        basisPanel.add(basisList);
        basisPanel.setLayout(null);

        JScrollPane relList = new JScrollPane(storageItems("relation", relationPanel));
        relList.setBounds(100, 10, 300, 250);
        relationPanel.add(relList);
        JButton relationCreateBtn = relationCreateBtn(relationPanel);
        relationCreateBtn.setBounds(180, 300, BUTTONWIDTH,50);
        relationPanel.add(relationCreateBtn);
        relationPanel.setLayout(null);

        JScrollPane declarationList = new JScrollPane(storageItems("declaration", relationPanel));
        declarationList.setBounds(180, 10, 150, 250);
        declarationPanel.add(declarationList);
        JButton declarationBtn = getDeclarationBtn();
        declarationBtn.setBounds(180, 300, BUTTONWIDTH,50);
        declarationPanel.add(declarationBtn);
        declarationPanel.setLayout(null);

        rightTabbedPane.setBackground(BGCOLOR);

        rightTabbedPane.add("SetObject", setObjectPanel);
        rightTabbedPane.add("HeytingAlgebra", heytingPanel);
        rightTabbedPane.add("Basis", basisPanel);
        rightTabbedPane.add("Relation", relationPanel);
        rightTabbedPane.add("Declaration", declarationPanel);

        focus = new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {

            }
        };

        rightTabbedPane.addChangeListener(e->{
            String name = rightTabbedPane.getSelectedComponent().getName();
            if (DATAUPDATED.get(name) == Boolean.TRUE) {
                System.out.println(STR."updating \{name} pane");
                switch (name) {
                    case "sets" -> {
                        JScrollPane setList1 = new JScrollPane(storageItems("", relationPanel));
                        setList1.setBounds(180, 10, 150, 250);
                        setObjectPanel.add(setList1);
                        setObjectPanel.add(createSetBtn);
                        setObjectPanel.setLayout(null);
                        DATAUPDATED.put("sets", Boolean.FALSE);
                    }
                    case "heyting" -> {
                        JScrollPane heytingList1 = new JScrollPane(storageItems("heyting", relationPanel));
                        heytingList1.setBounds(180, 10, 150, 250);
                        heytingPanel.add(heytingList1);
                        heytingPanel.setLayout(null);
                        DATAUPDATED.put("heyting", Boolean.FALSE);
                    }
                    case "basis" -> {
                        JScrollPane basisList1 = new JScrollPane(storageItems("basis", relationPanel));
                        basisList1.setBounds(180, 10, 150, 250);
                        basisPanel.add(basisList1);
                        basisPanel.setLayout(null);
                        DATAUPDATED.put("basis", Boolean.FALSE);
                    }
                    case "declaration" -> {
                        JScrollPane declarationList1 = new JScrollPane(storageItems("declaration", relationPanel));
                        declarationList1.setBounds(180, 10, 150, 250);
                        declarationPanel.add(declarationList1);
                        declarationPanel.add(declarationBtn);
                        declarationPanel.setLayout(null);
                        DATAUPDATED.put("declaration", Boolean.FALSE);
                    }
                    case "relation" -> {
                        JScrollPane relList1 = new JScrollPane(storageItems("relation", relationPanel));
                        relList1.setBounds(100, 10, 300, 250);
                        relationPanel.removeAll();
                        relationPanel.add(relList1);
                        relationPanel.add(relationCreateBtn);
                        relationPanel.setLayout(null);
                        relationPanel.updateUI();
                        DATAUPDATED.put("relation", Boolean.FALSE);
                    }
                }
            } else {
                if (name.equals("relation") && DATAITEMS.get(name) != null){
                    updateRelLeftPane(DATAITEMS.get(name), relationPanel);
                }
                else if (name.equals("declaration")  && DATAITEMS.get(name) != null){
                    updateDecLeftPane(DATAITEMS.get(name));
                } else {
                    leftPane.removeAll();
                    leftPane.updateUI();
                }
            }
        });

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

    private JButton setObjectCreateBtn(){
        JButton createSetObjectBtn = new JButton("New SetObject");
        createSetObjectBtn.addActionListener(e-> {
            UIDialogs.getInstance().createSetObjectDialog(this.frame).setVisible(true);
        });
        return createSetObjectBtn;
    }

    private JButton relationCreateBtn(JComponent relationPanel){
        JButton createNewRelation = new JButton("New Relation");
        createNewRelation.addActionListener(e-> {
            var resMap = UIDialogs.getInstance().createNewRelation(this.frame);
            JDialog dialog = (JDialog) resMap.get("dialog");
            dialog.setVisible(true);

            if ((boolean) resMap.get("created")) {
                updateRelationPanel(false, relationPanel);
                dialog.setVisible(false);
            }
        });
        return createNewRelation;
    }

    private JList<String> storageItems(String storage, JComponent relationPanel){
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
                String item = entities.getSelectedValue();

                if (storage.equals("relation")){
                    String itm = item.split(":")[0].strip();
                    DATAITEMS.put(storage, itm);
                    setUpLeftPane(storage, itm, relationPanel);
                } else {
                    DATAITEMS.put(storage, item);
                    setUpLeftPane(storage, item, relationPanel);
                }

            }
            check = !check;
        });

        return entities;
    }

    private void setUpLeftPane(String storage, String item, JComponent relationPanel) {
        switch (storage) {
            case "relation" -> {
                updateRelLeftPane(item, relationPanel);
            }
            default -> {
                updateDecLeftPane(item);
            }
        }
    }

    private void updateDecLeftPane(String item) {
        declaration = DeclarationStorage.getInstance().load(item);
        JLabel dec = new JLabel(declaration.getDeclaration());
        dec.setBounds(20, 50, 350, 50);
        leftPane.removeAll();
        leftPane.add(dec);
        leftPane.updateUI();
    }

    private void updateRelLeftPane(String item, JComponent relationPanel) {
        accept = new JButton("Accept");
        delete = new JButton("Delete");

        ImageIcon exp = new ImageIcon(getClass().getResource("../expand-view.png"), "expand view icon");
        expand = new JButton("", exp);

        accept.setBounds(120, 500, RELBUTTONWIDTH, BUTTONHEIGHT);
        accept.setForeground(OKBUTTONCOLOR);
        accept.setEnabled(false);

        delete.setBounds(220, 500, RELBUTTONWIDTH, BUTTONHEIGHT);
        delete.setForeground(DANGERBUTTONCOLOR);

        expand.setBounds(320, 500, RELBUTTONWIDTH, BUTTONHEIGHT);
        expand.setForeground(EXPANDBUTTONCOLOR);

        rel = RelationStorage.getInstance().load(item);
        RelationDisplay relationDisplay = RelationDisplay.getInstance();

        int[][] oldMatrix = UIMethods.getInstance().relMatrixClone(rel.getMatrix());

        relationDisplay.setRelation(rel, leftPane, accept, oldMatrix);

        relationDisplay.setBounds(20, 50, 500, 400);

        leftPane.removeAll();
        leftPane.add(relationDisplay);

        accept.addActionListener(e -> {
            Map<String, Object> res = UIMethods.getInstance().editRelation(rel);
            updateRelationPanel(!(Boolean) res.get("confirmed"), relationPanel);

            //reset old relation if not overwritten
            if (!res.get("old").equals(rel.getName())) {
                Relation r = new Relation(rel.getSourceTerm(), rel.getTargetTerm(),
                        rel.getParams(), rel.getTruth(), oldMatrix);
                r.setName((String) res.get("old"));
                RelationStorage.getInstance().forcePut(r);
            }
        });

        delete.addActionListener(e-> {
            if (UIDialogs.getInstance().confirmDeleteAction(
                    STR."Confirm to delete the relation '\{rel.getName()}'")) {
                UIMethods.UIMETHODS.removeRelation(rel);

                relationPanel.removeAll();
                JList<String> relations = storageItems("relation", relationPanel);
                JButton relationCreateBtn = relationCreateBtn(relationPanel);
                relationCreateBtn.setBounds(180, 300, BUTTONWIDTH,50);

                JScrollPane relList1 = new JScrollPane(relations);
                relList1.setBounds(100, 10, 300, 250);
                relationPanel.add(relList1);
                relations.setSelectedIndex(-1);
                relationPanel.add(relationCreateBtn);
                relationPanel.setLayout(null);
                relationPanel.updateUI();

                leftPane.removeAll();
                leftPane.updateUI();
            }
        });

        expand.addActionListener(e-> {
            RelationDisplay expandRelation = new RelationDisplay();
            expandRelation.setBounds(20, 50, 800, 700);
            expandRelation.setRelation(rel, leftPane, accept, oldMatrix);
            UIDialogs.getInstance().expandRelationView(expandRelation);
        });

        leftPane.add(accept);
        leftPane.add(delete);
        leftPane.add(expand);
        leftPane.updateUI();
    }

    private void updateRelationPanel(boolean confirmed, JComponent relationPanel) {
        System.out.println("updating relation screen");
        relationPanel.removeAll();
        JList<String> relations = new JList<>(RelationStorage.getInstance().getEntityNames());
        relationPanel.add(relations);
        relations.setSelectedValue(rel.getName(), true);
        relationPanel.add(relationCreateBtn(relationPanel));
        accept.setEnabled(confirmed);
        relationPanel.updateUI();
        leftPane.updateUI();
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
