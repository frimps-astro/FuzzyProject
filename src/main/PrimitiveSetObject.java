package main;

import java.io.*;
import java.util.Arrays;
import static classutils.LoadPaths.SETOBJECTPATH;

public class PrimitiveSetObject extends SetObject {
    
    public PrimitiveSetObject(String[] elementNames) {
        this.elementNames = elementNames;
    }
    
    public PrimitiveSetObject(String name, String[] elementNames) {
        this(elementNames);
        this.name = name;
    }
    
    @Override
    public void save() throws IOException {
        SetObjectStorage.getInstance().save(name, toXMLString());
    }

    @Override
    public String toXMLString() {
        StringBuilder setObjectXML = new StringBuilder();
        setObjectXML.append("<PrimitiveSetObject>\n\t");
        for(int i=0; i<elementNames.length; i++) {
            setObjectXML.append(elementNames[i]);
            if (i<elementNames.length-1) setObjectXML.append(",");
        }
        setObjectXML.append("\n</PrimitiveSetObject>");
        return setObjectXML.toString();
    }

    @Override
    public String toString() {
        return "Number of Elements: " + elementNames.length + " elements: " + Arrays.toString(elementNames);
    }
}


