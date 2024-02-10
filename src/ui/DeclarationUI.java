package ui;


import storage.RelationStorage;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

public class DeclarationUI extends JFrame {
    JTextField decI, varCountI;
    JLabel decl, varCountL;
    JButton executeB, loadVarsB;
    int xaxis = 50, yaxis = 50, height = 30;

    DeclarationUI() {
        createComponents();

        setSize(600, 500);
        setLayout(null);
        setVisible(true);
    }

    private void createComponents() {
        decl = new JLabel("Declaration:");
        decl.setBounds(xaxis, yaxis, 120, height);

        decI = new JTextField();
        decI.setBounds(xaxis * 3, 50, 350, height);


        JComboBox<String> relsBox = new JComboBox<String>(RelationStorage.getInstance().getEntityNames());
        relsBox.setBounds(xaxis, yaxis * 2, 120, height);
//        varCountL = new JLabel("No. of Vars:");
//        varCountL.setBounds(xaxis, yaxis * 2, 120, height);
//
//        varCountI = new JTextField();
//        varCountI.setBounds(xaxis * 3, yaxis * 2, 50, height);
//
//        loadVarsB = new JButton("Load Vars");
//        loadVarsB.setBounds(xaxis*4, yaxis*2, 100, height);
//        loadVarsB.addActionListener(this::loadVarsButtonAction);

        executeB = new JButton("Execute");
        executeB.setBounds(280, 250, 95, height);
        executeB.addActionListener(this::executeButtonAction);
        add(decI);
        add(decl);
        add(varCountL);
        add(varCountI);
        add(relsBox);

        add(executeB);
        add(loadVarsB );
    }

    public void executeButtonAction(ActionEvent e) {
        try {
            String host = decI.getText();
            String ip = java.net.InetAddress.getByName(host).getHostAddress();
            decl.setText("IP of " + host + " is: " + ip);
            add(decl);
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