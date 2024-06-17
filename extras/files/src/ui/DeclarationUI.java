package ui;


import relterm.Declaration;
import ui.utils.UIComponents;
import ui.utils.UIDialogs;
import ui.utils.UIMethods;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static ui.utils.UIConstants.*;

public class DeclarationUI extends JFrame {
    JButton executeB;
    Declaration declaration;

    public DeclarationUI(Declaration declaration) {
        this.declaration = declaration;
        setTitle("Declaration Execution");
        createComponents();

        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
        setBackground(BGCOLOR);
        addWindowListener(UIMethods.getInstance().windowEvent());
        setResizable(false);
    }

    private void createComponents() {
        this.setBackground(BGCOLOR);
        AtomicBoolean relCheck = new AtomicBoolean(true);
        List<JComboBox<String>> rels = UIComponents.getInstance().varRelations(declaration, this);

        JLabel note = new JLabel("Make Var to Relation Selections");
        note.setBounds(180, 5, 200, 30);


        JLabel resL = new JLabel("Result Name:");
        resL.setBounds(150, 380, 100, 30);

        JTextField resName = new JTextField();
        resName.setBounds(250, 380, 150, 30);

        executeB = new JButton("Execute");
        executeB.setBounds(210, 420, BUTTONWIDTH, BUTTONHEIGHT);
        executeB.addActionListener(e-> {
            rels.forEach(list -> {
                if (list.getSelectedIndex() == 0)
                    relCheck.set(false);
            });

            if (relCheck.get()){
                if (resName.getText().isBlank() || resName.getText().strip().length() < 3) {
                    UIDialogs.getInstance().notifyDialog("Please enter a valid result name");
                } else {
                    UIMethods.getInstance().executeButtonAction(declaration, rels, resName.getText().toLowerCase());
                }
            }
            else{
                relCheck.set(true);
                UIDialogs.getInstance().notifyDialog("Please select relation for every variable");
            }
        });


        add(note);
        add(resL);
        add(resName);
        add(executeB);
        setLayout(null);
    }
}