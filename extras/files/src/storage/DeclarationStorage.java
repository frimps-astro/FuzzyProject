package storage;

import exceptions.OperationExecutionException;
import main.Project;
import xmlutils.DeclarationXMLReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import relterm.Declaration;

import java.util.HashMap;
import java.util.Map;

import static classutils.LoadPaths.DATAPATH;
import static classutils.LoadPaths.DECLARATIONPATH;

public class DeclarationStorage {
    public static DeclarationStorage STORAGE = null;
    private static final Map<String, Declaration> database = new HashMap<>();


    public static DeclarationStorage getInstance() {
        if (STORAGE == null) STORAGE = new DeclarationStorage();
        return STORAGE;
    }

    public Declaration load(String filename) {
        if (database.containsKey(filename)) {
            System.out.println("loading "+ filename + " from storage");
            return this.get(filename);
        } else {
            System.out.println("loading "+ filename + " from disk");
            Declaration result = DeclarationXMLReader.getInstance().readXML(new File(DATAPATH
                    + Project.getInstance().getName()
                    + DECLARATIONPATH + filename + ".xml"));
            result.setName(filename);

            System.out.println("saving "+ filename + " to storage");
            database.put(filename, result);
            return result;
        }
    }

    public void save(String filename, String xmlString) throws IOException {
        System.out.println("saving "+ filename + " to disk");

        FileWriter writer = new FileWriter(DATAPATH
                + Project.getInstance().getName()
                + DECLARATIONPATH + filename + ".xml");
        writer.write(xmlString);
        writer.close();
    }

    public void put(Declaration dec){
        String filename = dec.getName();
        if (!database.containsKey(filename)){
            System.out.println("saving "+ filename + " to storage");
            database.put(filename, dec);
        }
    }

    public Declaration get(String filename){
        try {
            if (!database.containsKey(filename))
                throw new OperationExecutionException("Declaration Object not found");
            return database.get(filename);
        } catch (OperationExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getEntityNames(){
        return database.keySet().toArray(new String[1]);
    }

    public void empty(){
        database.clear();
    }
    public Map<String, Declaration> loadDeclaration(){
        return database;
    }
}
