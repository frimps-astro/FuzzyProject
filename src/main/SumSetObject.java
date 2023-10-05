package main;

import storage.SetObjectStorage;
import java.util.Arrays;

public class SumSetObject extends SetObject {

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
                elementNames[i] = "\u03b9(" + lelems[i] +")";
            } else {
                elementNames[i] = "\u03ba(" + relems[i-m] +")";
            }
        }
        this.left = left;
        this.right = right;
    }

    public SumSetObject(String name, SetObject left, SetObject right) {
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
    public String toXMLString() {
        StringBuilder setObjectXML = new StringBuilder();
        setObjectXML.append("<SumSetObject>\n");
        setObjectXML.append("\t<Component SetObject=\"");
        setObjectXML.append(left.getName().replace("out","")); //remove out keyword
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
