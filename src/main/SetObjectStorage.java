package main;

import xmlutils.SetObjectXMLReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static classutils.LoadPaths.SETOBJECTPATH;

public class SetObjectStorage{
    private static SetObjectStorage STORAGE = null;

    private static final Map<String, SetObject> database = new HashMap<>();
    private static final Map<String, Boolean> exists = new HashMap<>();

    public static SetObjectStorage getInstance() {
        if (STORAGE == null) STORAGE = new SetObjectStorage();
        return STORAGE;
    }

    private boolean setObjectSaved(String filename){
        if (exists.containsKey(filename))
            return exists.get(filename);

        return false;
    }

    public SetObject load(String filename){
        if (database.containsKey(filename)){
            System.out.println("loading "+ filename + " from storage");
            return this.get(filename);
        } else {
            System.out.println("loading "+ filename + " from disk");
            SetObject result = SetObjectXMLReader.getInstance().readXML(new File(SETOBJECTPATH + filename + ".xml"));
            result.name = filename;

            System.out.println("saving "+ filename + " to storage");
            database.put(filename, result);
            exists.put(filename, true);
            return result;
        }
    }

    public void save(String filename, String xmlString) throws IOException {
        if (!this.setObjectSaved(filename)){
            System.out.println("saving "+ filename + " to disk");

            FileWriter writer = new FileWriter(SETOBJECTPATH + filename + ".xml");
            writer.write(xmlString);
            writer.close();
            exists.put(filename, true);
        }
    }

    public void put(SetObject setObject){
        String filename = setObject.getName();
        if (!database.containsKey(filename)){
            System.out.println("saving "+ filename + " to storage");
            database.put(filename, setObject);
            exists.put(filename, false);
        }
    }

    public SetObject get(String filename){
        if(database.containsKey(filename))
            return database.get(filename);
        else
            return this.load(filename);
    }
}
