package main;

import storage.SetObjectStorage;

import java.io.IOException;
import java.util.Arrays;

public class ProductSetObject extends SetObject {
    
    private final SetObject left, right;

    public ProductSetObject(SetObject left, SetObject right) {
        String[] lelems = left.getElementNames();
        String[] relems = right.getElementNames();
        int m = left.getNumElements();
        int n = right.getNumElements();
        elementNames = new String[m*n];
        for(int i=0; i<m; i++) {
            for(int j=0; j<n; j++) {
                elementNames[i*n+j] = "(" + lelems[i] + "," + relems[j] + ")";
            }
        }
        this.left = left;
        this.right = right;
    }
    
    public ProductSetObject(String name, SetObject left, SetObject right) {
        this(left,right);
        this.name = name;

        //once a SetObject is created by hand, put to storage
       SetObjectStorage.getInstance().put(this);
    }

    public SetObject getLeft() {
        return left;
    }

    public SetObject getRight() {
        return right;
    }

    @Override
    public void save() throws IOException {
        SetObjectStorage.getInstance().save(name, toXMLString());
        this.left.save();
        this.right.save();
    }

    @Override
    public String toXMLString() {
        StringBuilder setObjectXML = new StringBuilder();
        setObjectXML.append("<ProductSetObject>\n");
        setObjectXML.append("\t<Component SetObject=\"");
        setObjectXML.append(left.getName().replace("out",""));
        setObjectXML.append("\"/>\n\t<Component SetObject=\"");
        setObjectXML.append(right.getName().replace("out",""));
        setObjectXML.append("\"/>\n</ProductSetObject>");
        return setObjectXML.toString();
    }

    public String toString() {
        return "\nNumber of Left Elements: " + left.getNumElements() + " Left Elements: " + Arrays.toString(left.getElementNames())
                +"\nNumber of Right Elements: " + right.getNumElements() + " Right Elements: " + Arrays.toString(right.getElementNames());
    }
}
