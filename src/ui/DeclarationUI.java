package ui;


import storage.RelationStorage;
import ui.utils.UIMethods;

import javax.swing.*;
import java.awt.event.*;

import static ui.utils.UIConstants.BUTTONHEIGHT;
import static ui.utils.UIConstants.BUTTONWIDTH;

public class DeclarationUI extends JFrame {
    JTextField decI, varCountI;
    JLabel decl, varCountL, varL;
    JButton executeB, loadVarsB;
    int xaxis = 50, yaxis = 50, height = 30;

    DeclarationUI() {
        createComponents();

        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
        addWindowListener(UIMethods.getInstance().windowEvent());
    }

    private void createComponents() {
        decl = new JLabel("Declaration:");
        decl.setBounds(xaxis, yaxis, 120, height);

        decI = new JTextField();
        decI.setBounds(xaxis * 3, 50, 350, height);


        varL = new JLabel("Relation:");
        varL.setBounds(xaxis, yaxis * 2, 120, height);

        JComboBox<String> relsBox = new JComboBox<String>(RelationStorage.getInstance().getEntityNames());
        relsBox.insertItemAt("--select relation--", 0);
        relsBox.setSelectedIndex(0);
        relsBox.setBounds(xaxis * 3, yaxis * 2, 200, height);

//        varCountI = new JTextField();
//        varCountI.setBounds(xaxis * 3, yaxis * 2, 50, height);
//
//        loadVarsB = new JButton("Load Vars");
//        loadVarsB.setBounds(xaxis*4, yaxis*2, 100, height);
//        loadVarsB.addActionListener(this::loadVarsButtonAction);

        executeB = new JButton("Execute");
        executeB.setBounds(xaxis * 3, yaxis*3, BUTTONWIDTH, BUTTONHEIGHT);
        executeB.addActionListener(this::executeButtonAction);
        add(decI);
        add(decl);
        add(varL);
//        add(varCountI);
        add(relsBox);

        add(executeB);
//        add(loadVarsB );
    }

    public void executeButtonAction(ActionEvent e) {
        try {
            String dec = decI.getText();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    public void loadVarsButtonAction(ActionEvent e) {
        int c = Integer.parseInt(varCountI.getText());
        String[] rels = RelationStorage.getInstance().getEntityNames();
        int y = yaxis*3;
        for (int i = 0; i < c; i++) {
            System.out.println(c-i);
            JComboBox comboBox = new JComboBox(rels);
            comboBox.setBounds(xaxis,y*(i+1), 120, height);
            comboBox.setVisible(true);
        }

    }
}
//    public static void main(String[] args) {
//        new DeclarationUI();
//    } }