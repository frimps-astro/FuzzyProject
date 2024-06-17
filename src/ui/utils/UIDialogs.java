package ui.utils;

import relations.Relation;
import storage.BasisStorage;
import storage.RelationStorage;
import storage.SetObjectStorage;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class UIDialogs {
    private static UIDialogs UIDIALOGS = null;

    public static UIDialogs getInstance() {
        if (UIDIALOGS == null) UIDIALOGS = new UIDialogs();
        return UIDIALOGS;
    }

    public Dialog notifyDialog(String note) {
        JFrame frame = new JFrame();
        Dialog expand = new Dialog(frame, "Notification", true);
        expand.setSize(300, 250);
        expand.setLocationRelativeTo(null);
        expand.setBackground(Color.WHITE);

        JLabel noteL = new JLabel(note, SwingConstants.CENTER);
        noteL.setBounds(0, 50, expand.getWidth(), 30);
        JButton okBtn = new JButton ("OK");
        okBtn.addActionListener(e->expand.setVisible(false));
        okBtn.setBounds(expand.getSize().width/4, expand.getHeight()-80, UIConstants.BUTTONWIDTH,UIConstants.BUTTONHEIGHT+15);

        expand.add(noteL);
        expand.add(okBtn);

        expand.setResizable(false);
        expand.setLayout(null);
        expand.setVisible(true);

        return expand;
    }

    public JDialog createSetObjectDialog(JFrame frame){
        final JDialog modelDialog = new JDialog(frame, "New SetObject",
                Dialog.ModalityType.DOCUMENT_MODAL);
        modelDialog.setSize(300, 300);
        modelDialog.setLocationRelativeTo(null);
        Container dialogContainer = modelDialog.getContentPane();
        dialogContainer.setLayout(new BorderLayout());

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new FlowLayout());

        JComboBox<String> sets = new JComboBox<>(new String[]{"--choose set--", "Sum", "Product", "Power"});
        sets.setSelectedIndex(0);

        dialogContainer.add(sets, BorderLayout.NORTH);

        //set body
        JPanel setSelection = new JPanel();
        String[] setlists = SetObjectStorage.getInstance().getEntityNames();

        JComboBox<String> setListing1= new JComboBox<>(setlists);
        setListing1.insertItemAt("--choose set component 1--", 0);
        setListing1.setSelectedIndex(0);

        JComboBox<String> setListing2= new JComboBox<>(setlists);
        setListing2.insertItemAt("--choose set component 2--", 0);
        setListing2.setSelectedIndex(0);

        String[] basislists = BasisStorage.getInstance().getEntityNames();
        JComboBox<String> basisListing= new JComboBox<>(basislists);
        basisListing.insertItemAt("--choose basis component--", 0);
        basisListing.setSelectedIndex(0);

        sets.addActionListener(e-> {
            int selectedIndex = sets.getSelectedIndex();
            if ( selectedIndex== 1 || selectedIndex == 2) {
                setSelection.removeAll();
                setSelection.add(setListing1);
                setSelection.add(setListing2);

            } else if (selectedIndex == 3) {
                setListing1.removeItemAt(0);
                setListing1.insertItemAt("--choose body component--", 0);
                setListing1.setSelectedIndex(0);

                setSelection.removeAll();
                setSelection.add(basisListing);
                setSelection.add(setListing1);
            }
            SwingUtilities.updateComponentTreeUI(dialogContainer);
        });

        JButton createBtn = new JButton("Create");
        createBtn.addActionListener(e-> {
            int selectedIndex = sets.getSelectedIndex();
            String component1 = null;
            String component2 = null;
            boolean created = false;

            if ( selectedIndex== 1 || selectedIndex == 2) {
                if (setListing1.getSelectedIndex() != 0 && setListing2.getSelectedIndex() != 0){
                    component1 = Objects.requireNonNull(setListing1.getSelectedItem()).toString();
                    component2 = Objects.requireNonNull(setListing2.getSelectedItem()).toString();
                    created = UIMethods.getInstance().createSetObject(selectedIndex, component1, component2);
                    modelDialog.setVisible(!created);
                }
            } else if (selectedIndex == 3) {
                if (setListing1.getSelectedIndex() != 0 && basisListing.getSelectedIndex() != 0){
                    component1 = Objects.requireNonNull(basisListing.getSelectedItem()).toString();
                    component2 = Objects.requireNonNull(setListing1.getSelectedItem()).toString();
                    created = UIMethods.getInstance().createSetObject(selectedIndex, component1, component2);
                    modelDialog.setVisible(!created);
                }
            }
            if (created) {
                UIConstants.DATAUPDATED.put("sets", Boolean.TRUE);
            }
        });

        dialogContainer.add(setSelection, BorderLayout.CENTER);
        dialogPanel.add(createBtn);
        dialogContainer.add(dialogPanel, BorderLayout.SOUTH);

        return modelDialog;
    }
    public Map<String, Object> createNewRelation(JFrame frame){
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("created", false);

        final JDialog modelDialog = new JDialog(frame, "New Relation",
                Dialog.ModalityType.DOCUMENT_MODAL);
        modelDialog.setSize(400, 400);
        modelDialog.setLocationRelativeTo(null);
        Container dialogContainer = modelDialog.getContentPane();
        dialogContainer.setLayout(new BorderLayout());

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new FlowLayout());

        JLabel basisL = new JLabel("Basis:");
        basisL.setBounds(10, 10, 100, 30);

        JTextField basisName = new JTextField();
        basisName.setBounds(105, 10, 200, 30);

        JLabel sourceL = new JLabel("Source:");
        sourceL.setBounds(10, 50, 100, 30);

        JTextField sourceTerm = new JTextField();
        sourceTerm.setBounds(105, 50, 200, 30);

        JLabel targetL = new JLabel("Target:");
        targetL.setBounds(10, 100, 100, 30);

        JTextField targetTerm = new JTextField();
        targetTerm.setBounds(105, 100, 200, 30);

        JLabel resL = new JLabel("Relation Name:");
        resL.setBounds(10, 150, 100, 30);

        JTextField resName = new JTextField();
        resName.setBounds(105, 150, 200, 30);

        JLabel label = new JLabel("");
        label.setBounds(10, 200, 100, 30);

        JButton createBtn = new JButton("Create");
        createBtn.addActionListener(e-> {
            if (UIMethods.getInstance().createNewRelation(basisName.getText(), sourceTerm.getText(),
                    targetTerm.getText(), resName.getText())) {

                resMap.put("created", true);
                basisName.setText("");
                sourceTerm.setText("");
                targetTerm.setText("");
                resName.setText("");
                modelDialog.setVisible(false);
            }
        });

        dialogContainer.add(basisL, BorderLayout.CENTER);
        dialogContainer.add(basisName, BorderLayout.CENTER);
        dialogContainer.add(sourceL, BorderLayout.CENTER);
        dialogContainer.add(sourceTerm, BorderLayout.CENTER);
        dialogContainer.add(targetL, BorderLayout.CENTER);
        dialogContainer.add(targetTerm, BorderLayout.CENTER);
        dialogContainer.add(resL, BorderLayout.CENTER);
        dialogContainer.add(resName, BorderLayout.CENTER);
        dialogContainer.add(label, BorderLayout.CENTER);
        dialogPanel.add(createBtn);
        dialogContainer.add(dialogPanel, BorderLayout.SOUTH);

        resMap.put("dialog", modelDialog);

        return resMap;
    }

    public Map<String, Object> confirmCreateAction(String note) {
        Map<String, Object> resMap = new HashMap<>();

        JFrame frame = new JFrame();
        Dialog expand = new Dialog(frame, "Save Changes", true);
        expand.setSize(300, 250);
        expand.setLocationRelativeTo(null);
        expand.setBackground(Color.WHITE);

        JLabel noteL = new JLabel(note, SwingConstants.CENTER);
        noteL.setBounds(0, 35, expand.getWidth(), 30);

        JLabel resL = new JLabel("Relation Name:", SwingConstants.CENTER);
        resL.setBounds(15, 70, 100, 30);
        JTextField resName = new JTextField();
        resName.setBounds(120, 70, 135, 30);

        JLabel optionalL = new JLabel("if left empty, relation would be overwritten", SwingConstants.CENTER);
        optionalL.setFont(new Font("arial", Font.ITALIC, 11));
        optionalL.setBounds(0, 90, expand.getWidth(), 30);
        optionalL.setForeground(new Color(255, 151, 151));

        JButton okBtn = new JButton ("OK");
        okBtn.setForeground(new Color(0, 0, 238));
        okBtn.addActionListener(e->{
            resMap.put("confirmed", true);
            resMap.put("name", resName.getText());
            expand.setVisible(false);
        });
        okBtn.setBounds(160, expand.getHeight()-50, 80,UIConstants.BUTTONHEIGHT);

        JButton cancelBtn = new JButton ("CANCEL");
        cancelBtn.setForeground(new Color(255, 0, 7));
        cancelBtn.addActionListener(e->{
            resMap.put("confirmed", false);
            expand.setVisible(false);
        });
        cancelBtn.setBounds(60, expand.getHeight()-50, 80,UIConstants.BUTTONHEIGHT);

        expand.add(resL);
        expand.add(resName);
        expand.add(optionalL);
        expand.add(noteL);
        expand.add(okBtn);
        expand.add(cancelBtn);

        expand.setResizable(false);
        expand.setLayout(null);
        expand.setVisible(true);

        return resMap;
    }

    public boolean confirmDeleteAction(String note) {
        AtomicBoolean delete = new AtomicBoolean(false);

        JFrame frame = new JFrame();
        Dialog expand = new Dialog(frame, "Confirm Deletion", true);
        expand.setSize(300, 150);
        expand.setLocationRelativeTo(null);
        expand.setBackground(Color.WHITE);

        JLabel noteL = new JLabel(note, SwingConstants.CENTER);
        noteL.setBounds(0, 35, expand.getWidth(), 30);


        JButton okBtn = new JButton ("OK");
        okBtn.setForeground(UIConstants.OKBUTTONCOLOR);
        okBtn.addActionListener(e->{
            delete.set(true);
            expand.setVisible(false);
        });
        okBtn.setBounds(160, expand.getHeight()-50, 80,UIConstants.BUTTONHEIGHT);

        JButton cancelBtn = new JButton ("CANCEL");
        cancelBtn.setForeground(UIConstants.DANGERBUTTONCOLOR);
        cancelBtn.addActionListener(e->{
            delete.set(false);
            expand.setVisible(false);
        });
        cancelBtn.setBounds(60, expand.getHeight()-50, 80,UIConstants.BUTTONHEIGHT);

        expand.add(noteL);
        expand.add(okBtn);
        expand.add(cancelBtn);

        expand.setResizable(false);
        expand.setLayout(null);
        expand.setVisible(true);

        return delete.get();
    }

    public void expandRelationView(Component rel) {
        JFrame frame = new JFrame();
        Dialog expand = new Dialog(frame, "Relation View", true);
        expand.setSize(800, 800);
        expand.setLocationRelativeTo(null);
        expand.setBackground(Color.WHITE);

        JButton okBtn = new JButton ("CLOSE");
        okBtn.setForeground(UIConstants.OKBUTTONCOLOR);
        okBtn.addActionListener(e->{
            expand.setVisible(false);
        });
        okBtn.setBounds(375, expand.getHeight()-40, 80,UIConstants.BUTTONHEIGHT);

        expand.add(rel);
        expand.add(okBtn);
        expand.setResizable(false);
        expand.setLayout(null);
        expand.setVisible(true);
    }
}
