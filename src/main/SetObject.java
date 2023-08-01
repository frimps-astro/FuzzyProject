package main;

import java.io.IOException;

import storage.SetObjectStorage;
import xmlutils.XMLObject;

public abstract class SetObject implements XMLObject {
    
    protected String name;
    protected String[] elementNames;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public int getNumElements() {
        return elementNames.length;
    };

    public String[] getElementNames() {
        return elementNames;
    };

    public static SetObject load(String filename){
        return SetObjectStorage.getInstance().load(filename);
    }

    public abstract void save() throws IOException;
}
