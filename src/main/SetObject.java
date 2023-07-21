package main;

import java.io.File;
import java.io.IOException;
import static classutils.LoadPaths.SETOBJECTPATH;
import xmlutils.SetObjectXMLReader;
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
