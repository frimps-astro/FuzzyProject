package storage;

import exceptions.OperationExecutionException;
import relations.Relation;
import main.Project;
import xmlutils.RelationXMLReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static classutils.LoadPaths.RELATIONPATH;
import static classutils.LoadPaths.DATAPATH;

public class RelationStorage {
    private static RelationStorage STORAGE = null;

    private static final Map<String, Relation> database = new HashMap<>();

    public static RelationStorage getInstance() {
        if (STORAGE == null) STORAGE = new RelationStorage();
        return STORAGE;
    }

    public Relation load(String filename) {
        if (database.containsKey(filename)) {
            System.out.println("loading "+ filename + " from storage");
            return this.get(filename);
        } else {
            System.out.println("loading "+ filename + " from disk");
            Relation result = RelationXMLReader.getInstance().readXML(new File(DATAPATH
                    + Project.getInstance().getName()
                    + RELATIONPATH + filename + ".xml"));
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
                + RELATIONPATH + filename + ".xml");
        writer.write(xmlString);
        writer.close();
    }

    public void put(Relation rel){
        String filename = rel.getName();
        if (!database.containsKey(filename)){
            System.out.println("saving "+ filename + " to storage");
            database.put(filename, rel);
        }
    }
    public void forcePut(Relation rel){
        String filename = rel.getName();
        System.out.println("overwriting "+ filename + " in storage");
        database.put(filename, rel);
    }

    public Relation get(String filename){
        try {
            if (!database.containsKey(filename))
                throw new OperationExecutionException("Relation Object not found");
            return database.get(filename);
        } catch (OperationExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getEntityNames(){
        List<String> data = new ArrayList<>();
        database.forEach((k, v) -> {
            data.add(STR."\{k} : [\{v.getTruth().getName()}] \{v.getSourceTerm()}->\{v.getTargetTerm()}");
        });
        return data.toArray(new String[0]);
    }
    public void delete(Relation rel) {
        String filename = rel.getName();
        if (database.containsKey(filename)){
            System.out.println("removing "+ filename + " from storage");
            database.remove(filename, rel);
            System.out.println(STR."removing \{filename} from disk");
            File deleteRel = new File(STR."\{DATAPATH}\{Project.getInstance().getName()}\{RELATIONPATH}\{filename}.xml");
            deleteRel.delete();
        }
    }
    public void empty(){
        database.clear();
    }

    public Map<String, Relation> getDatabase(){
        return database;
    }
}

