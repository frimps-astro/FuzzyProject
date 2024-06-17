package storage;

import exceptions.OperationExecutionException;
import main.Basis;
import main.Project;
import xmlutils.BasisXMLReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static classutils.LoadPaths.BASISPATH;
import static classutils.LoadPaths.DATAPATH;

public class BasisStorage{
    private static BasisStorage STORAGE = null;

    private static final Map<String, Basis> database = new HashMap<>();

    public static BasisStorage getInstance() {
        if (STORAGE == null) STORAGE = new BasisStorage();
        return STORAGE;
    }

    public Basis load(String filename){
        if (database.containsKey(filename)){
            System.out.println("loading "+ filename + " from storage");
            return this.get(filename);
        } else {
            System.out.println("loading "+ filename + " from disk");
            Basis result = BasisXMLReader.getInstance().readXML(new File(DATAPATH
                    + Project.getInstance().getName()
                    + BASISPATH + filename + ".xml"));
            result.setName(filename);

            System.out.println("saving "+ filename + " to storage");
            database.put(filename, result);
            return result;
        }
    }

    private void save(String filename, String xmlString) throws IOException {
            System.out.println("saving "+ filename + " to disk");

            FileWriter writer = new FileWriter(DATAPATH
                    + Project.getInstance().getName()
                    + BASISPATH + filename + ".xml");
            writer.write(xmlString);
            writer.close();
    }

    public void put(Basis basis){
        String filename = basis.getName();
        if (!database.containsKey(filename)){
            System.out.println("saving "+ filename + " to storage");
            database.put(filename, basis);
        }
    }

    public Basis get(String filename){
        try {
            if (!database.containsKey(filename))
                throw new OperationExecutionException("Basis Object not found");
            return database.get(filename);
        } catch (OperationExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getEntityNames(){
        return database.keySet().toArray(new String[0]);
    }
    public void empty(){
        database.clear();
    }

    public Map<String, Basis> getDatabase(){
        return database;
    }
}

