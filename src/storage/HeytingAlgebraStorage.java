package storage;

import exceptions.OperationExecutionException;
import main.HeytingAlgebra;
import main.Project;
import xmlutils.HeytingAlgebraXMLReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static classutils.LoadPaths.DATAPATH;
import static classutils.LoadPaths.HEYTINGALGEBRAPATH;

public class HeytingAlgebraStorage{
    private static HeytingAlgebraStorage STORAGE = null;

    private static final Map<String, HeytingAlgebra> database = new HashMap<>();

    public static HeytingAlgebraStorage getInstance() {
        if (STORAGE == null) STORAGE = new HeytingAlgebraStorage();
        return STORAGE;
    }

    public HeytingAlgebra load(String filename){
        if (database.containsKey(filename)){
            System.out.println("loading "+ filename + " from storage");
            return this.get(filename);
        } else {
            System.out.println("loading "+ filename + " from disk");
            HeytingAlgebra result = HeytingAlgebraXMLReader.getInstance().readXML(new File(DATAPATH
                    + Project.getInstance().getName()
                    + HEYTINGALGEBRAPATH + filename + ".xml"));
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
                    + HEYTINGALGEBRAPATH + filename + ".xml");
            writer.write(xmlString);
            writer.close();
    }

    public void put(HeytingAlgebra heytingAlgebra){
        String filename = heytingAlgebra.getName();
        if (!database.containsKey(filename)){
            System.out.println("saving "+ filename + " to storage");
            database.put(filename, heytingAlgebra);
        }
    }

    public HeytingAlgebra get(String filename){
        try {
                if (!database.containsKey(filename))
                    throw new OperationExecutionException("HeytingAlgebra not found");
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
}

