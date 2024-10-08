package sets;

import storage.SetObjectStorage;

import java.util.Arrays;

public class PrimitiveSetObject extends SetObject {
    
    public PrimitiveSetObject(String[] elementNames) {
        this.elementNames = elementNames;
    }
    
    public PrimitiveSetObject(String name, String[] elementNames) {
        this(elementNames);
        this.name = name;

        SetObjectStorage.getInstance().put(this);
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
        return STR."PrimitiveSetObject{name='\{name}\{'\''}, elementNames=\{Arrays.toString(elementNames)}, numberOfElements=\{this.getNumElements()}\{'}'}";
    }
}


