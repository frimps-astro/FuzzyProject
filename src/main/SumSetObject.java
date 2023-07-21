package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import static classutils.LoadPaths.SETOBJECTPATH;

public class SumSetObject extends SetObject{

    private final SetObject left, right;

    public SumSetObject(SetObject left, SetObject right) {
        String[] lelems = left.getElementNames();
        String[] relems = right.getElementNames();
        int m = left.getNumElements();
        int n = right.getNumElements();
        int sum = m + n;
        elementNames = new String[sum];
        for(int i=0; i<sum; i++) {
            if (i < m){
                elementNames[i] = "i(" + lelems[i] +")";
            } else {
                elementNames[i] = "k(" + relems[i-m] +")";
            }
        }
        this.left = left;
        this.right = right;
    }

    public SumSetObject(String name, SetObject left, SetObject right) {
        this(left,right);
        this.name = name;

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
        setObjectXML.append("<SumSetObject>\n");
        setObjectXML.append("\t<Component SetObject=\"");
        setObjectXML.append(left.getName().replace("out",""));
        setObjectXML.append("\"/>\n\t<Component SetObject=\"");
        setObjectXML.append(right.getName().replace("out",""));
        setObjectXML.append("\"/>\n</SumSetObject>");
        return setObjectXML.toString();
    }

    public String toString() {
        return "\nNumber of Left Elements: " + left.getNumElements() + " Left Elements: " + Arrays.toString(left.getElementNames())
                +"\nNumber of Right Elements: " + right.getNumElements() + " Right Elements: " + Arrays.toString(right.getElementNames());
    }
}
