package storage;

import exceptions.OperationExecutionException;
import main.*;
import xmlutils.SetObjectXMLReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static classutils.LoadPaths.DATAPATH;
import static classutils.LoadPaths.SETOBJECTPATH;

public class SetObjectStorage{
    private static SetObjectStorage STORAGE = null;

    private static final Map<String, SetObject> database = new HashMap<>();

    public static SetObjectStorage getInstance() {
        if (STORAGE == null) STORAGE = new SetObjectStorage();
        return STORAGE;
    }

    public SetObject load(String filename){
        if (database.containsKey(filename)){
            System.out.println("loading "+ filename + " from storage");
            return this.get(filename);
        } else {
            System.out.println("loading "+ filename + " from disk");
            SetObject result = SetObjectXMLReader.getInstance().readXML(new File(DATAPATH
                    + Project.getInstance().getName()
                    + SETOBJECTPATH + filename + ".xml"));
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
                    + SETOBJECTPATH + filename + ".xml");
            writer.write(xmlString);
            writer.close();
    }

    public void put(SetObject setObject){
        String filename = setObject.getName();
        if (!database.containsKey(filename)){
            System.out.println("saving "+ filename + " to storage");
            database.put(filename, setObject);
        }
    }

    private SetObject get(String filename){
        try {
            if (!database.containsKey(filename))
                throw new OperationExecutionException("Set object not found");
            return database.get(filename);
        } catch (OperationExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void empty(){
        database.clear();
    }

    public void createSumSetObject(String name, String leftComponent, String rightComponent){
        try {
            SetObject left = database.get(leftComponent);
            SetObject right = database.get(rightComponent);

            if (database.containsKey(name))
                throw new OperationExecutionException("A SumSetObject with the name " + name + " already exists");

            if (left == null || right == null)
                throw new OperationExecutionException("Both components of the Sum Set must exist");

            SumSetObject sumSetObject = new SumSetObject(name, left, right);
            this.save(sumSetObject.getName(), sumSetObject.toXMLString());
        } catch (OperationExecutionException | IOException e){
            throw new RuntimeException(e);
        }
    }

    public void createProductSetObject(String name, String leftComponent, String rightComponent){
        try {
            SetObject left = database.get(leftComponent);
            SetObject right = database.get(rightComponent);

            if (database.containsKey(name))
                throw new OperationExecutionException("A ProductSetObject with the name " + name + " already exists");

            if (left == null || right == null)
                throw new OperationExecutionException("Both components of the Product Set must exist");

            ProductSetObject productSetObject = new ProductSetObject(name, left, right);
            this.save(productSetObject.getName(), productSetObject.toXMLString());
        } catch (OperationExecutionException | IOException e){
            throw new RuntimeException(e);
        }
    }

    public void createPowerSetObject(String name, String bodyComponent, String basisComponent){
        try {
            SetObject body = database.get(bodyComponent);
            Basis basis = BasisStorage.getInstance().get(basisComponent);

            if (database.containsKey(name))
                throw new OperationExecutionException("A PowerSetObject with the name " + name + " already exists");

            if (body == null || basis == null)
                throw new OperationExecutionException("Both components of the Power Set must exist");

            PowerSetObject powerSetObject = new PowerSetObject(name, body, basis);
            this.save(powerSetObject.getName(), powerSetObject.toXMLString());
        } catch (OperationExecutionException | IOException e){
            throw new RuntimeException(e);
        }
    }
}
