package ui.utils;

import exceptions.OperationExecutionException;
import exceptions.TypingException;
import exceptions.UnificationException;
import relations.Relation;
import relterm.Declaration;
import storage.RelationStorage;
import storage.SetObjectStorage;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ui.utils.UIConstants.INTERFACES;


public class UIMethods {
    public static UIMethods UIMETHODS = null;

    public static UIMethods getInstance() {
        if (UIMETHODS == null) UIMETHODS = new UIMethods();
        return UIMETHODS;
    }

    public boolean createSetObject(int selectedIndex, String component1, String component2) {
        boolean created = false;
        if (component1 != null && component2 != null){
            try {
                switch (selectedIndex){
                    case 1 -> SetObjectStorage.getInstance()
                            .createSumSetObject(component1+"+"+component2, component1, component2);
                    case 2 -> SetObjectStorage.getInstance()
                            .createProductSetObject(component1+"x"+component2, component1, component2);
                    case 3 -> SetObjectStorage.getInstance()
                            .createPowerSetObject(component1+"^"+component2, component2, component1);
                }
                return !created;
            } catch (OperationExecutionException e) {
                UIDialogs.getInstance().notifyDialog(formatNotification(e.getMessage().split(":")[1]));
            }
        }
        return created;
    }

    public void executeButtonAction(Declaration declaration, List<JComboBox<String>> rels, String resName) {
                    List<Relation> relsList = new ArrayList<>();
                    rels.forEach(r-> relsList.add(RelationStorage.getInstance().load(r.getSelectedItem().toString())));
                    try {
                        declaration.type();
                        Relation executedDec = declaration.execute(relsList, relsList.getFirst().getAlgebra());
                        executedDec.setName(resName);

                        RelationStorage.getInstance().save(executedDec.getName(), executedDec.toXMLString());
                        RelationStorage.getInstance().put(executedDec);
                        System.out.println(executedDec);
                    } catch (IOException | TypingException | UnificationException e){
                        System.out.println(e.getMessage());
                    }
    }

    private String formatNotification(String note) {
        String newNote = "<html>";
        System.out.println(note.length());
        if (note.length() > 40) {
            newNote += note.substring(0, 40);
            newNote += "...<br>"+note.substring(40)+"</html>";
            return newNote;
        }
        return note;
    }

    public String[] readDataList(String path){
        File file = new File(path);
        String[] projects = file.list((current, name) -> new File(current, name).isDirectory());

        return projects;
    }

    public WindowAdapter windowEvent() {
       return new WindowAdapter() {
           @Override
           public void windowOpened(WindowEvent e) {
               var window = e.getWindow().getClass();
               System.out.println("opened: "+ window.getSimpleName());
               INTERFACES.put(window.getSimpleName(), (JFrame) e.getComponent());
               super.windowOpened(e);
           }

            @Override
            public void windowClosing(WindowEvent e) {
                String window = e.getWindow().getClass().getSimpleName();
                System.out.println("closed: "+window);
                INTERFACES.remove(window);
                super.windowClosed(e);
            }
        };
    }
}
