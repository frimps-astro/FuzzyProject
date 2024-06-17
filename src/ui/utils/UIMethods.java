package ui.utils;

import exceptions.OperationExecutionException;
import exceptions.TypingException;
import exceptions.UnificationException;
import main.Basis;
import relations.Relation;
import relterm.Declaration;
import sets.SetObject;
import storage.*;
import typeterm.Typeterm;
import typeterm.TypetermParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static classutils.LoadPaths.*;
import static ui.utils.UIConstants.INTERFACES;
import static xmlutils.XMLTools.getStringAttribute;


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

    public boolean executeButtonAction(Declaration declaration, List<JComboBox<String>> rels, String resName) {
        List<Relation> relsList = new ArrayList<>();
        rels.forEach(r-> {
            relsList.add(RelationStorage.getInstance().load(r.getSelectedItem().toString().split(":")[0].strip()));
        });
        try {
            declaration.type();
            Relation executedDec = declaration.execute(relsList, relsList.getFirst().getAlgebra());
            executedDec.setName(resName);
            RelationStorage.getInstance().save(executedDec.getName(), executedDec.toXMLString());
            RelationStorage.getInstance().put(executedDec);
        } catch (IOException | TypingException | UnificationException e){
            System.out.println(e.getMessage());
            return false;
        }

        return true;

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

    public int[][] relMatrixClone(int[][] matrix) {
        int[][] newMatrix = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++)
            newMatrix[i] = matrix[i].clone();

        return newMatrix;
    }

    public Map<String, Object> editRelation(Relation rel) {
        var res = UIDialogs.getInstance().confirmCreateAction("Accept new matrix and save relation?");
        String oldName = rel.getName();
        res.put("old", oldName);
        if ((boolean) res.get("confirmed")) {
            try {
                String newName = res.get("name").toString();
                if (!newName.isBlank()) {
                    rel.setName(newName);
                }
                RelationStorage.getInstance().save(rel.getName(), rel.toXMLString());
                RelationStorage.getInstance().put(rel);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return res;
    }

    public boolean removeRelation(Relation rel) {
        try {
            RelationStorage.getInstance().delete(rel);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

    public boolean createNewRelation(String b, String source, String target, String relName) {
        AtomicBoolean checker = new AtomicBoolean(true);
        if (b.isBlank() || source.isBlank() || target.isBlank() || relName.isBlank()){
            UIDialogs.getInstance().notifyDialog("Please provide all fields");
        } else {
            Basis basis;
            Typeterm sourceTerm = TypetermParser.getTypeParser().parse(source);
            Typeterm targetTerm = TypetermParser.getTypeParser().parse(target);

            if (!checkFile(b, "basis")){
                UIDialogs.getInstance().notifyDialog(STR."The basis '\{b}' does not exists");
                checker.set(false);
            }
            basis = BasisStorage.getInstance().load(b);

            Map<String, SetObject> params = new HashMap<>();
            loadSets(sourceTerm, params, checker);
            loadSets(targetTerm, params, checker);

            if (checker.get()) {
                int s = sourceTerm.execute(params, basis).getNumElements();
                int t =targetTerm.execute(params, basis).getNumElements();

                int[][] matrix = new int[s][t];

                Relation relation = new Relation(sourceTerm, targetTerm, params, basis, matrix);
                relation.setName(relName);

                try{
                    RelationStorage.getInstance().save(relName, relation.toXMLString());
                    RelationStorage.getInstance().put(relation);
                } catch (Exception ex) {
                    checker.set(false);
                    System.out.println(ex.getMessage());
                }
                UIDialogs.getInstance().notifyDialog(STR."Relation '\{relName}' created successfully");
            }
        }
        return checker.get();
    }

    private void loadSets(Typeterm term, Map<String, SetObject> params, AtomicBoolean checker) {
        if (checker.get()) {
            for (String var : term.variables()) {
                if (!checkFile(var, "setobject")){
                    checker.set(false);
                    UIDialogs.getInstance().notifyDialog(STR."The set '\{var}' does not exists");
                    break;
                }
                SetObject setObject = SetObjectStorage.getInstance().load(var);
                params.put(setObject.getName(), setObject);
            }
        }

    }

    private boolean checkFile(String filename, String path) {
        switch (path){
            case "basis" -> {
                return BasisStorage.getInstance().getDatabase().containsKey(filename);
            }
            case "heyting" -> {
                return HeytingAlgebraStorage.getInstance().getDatabase().containsKey(filename);
            }
            case "relation" -> {
                return RelationStorage.getInstance().getDatabase().containsKey(filename);
            }
            case "declaration" -> {
                return DeclarationStorage.getInstance().getDatabase().containsKey(filename);
            }
            default -> {
                return SetObjectStorage.getInstance().getDatabase().containsKey(filename);
            }
        }
    }
}
