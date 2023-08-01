package main;

import storage.SetObjectStorage;

import java.io.IOException;
import java.util.Arrays;
import static java.lang.Math.pow;

public class PowerSetObject extends SetObject {

    private final SetObject body;
    private Basis basis;

    public PowerSetObject(SetObject body, Basis basis) {
        String[] bodyelems = body.getElementNames();
        String[]  basiselems= basis.getElementNames();
        int m = body.getNumElements();
        int n = basis.getNumElements();
        int pow = (int)pow(n, m);
        elementNames = new String[pow];
        StringBuilder set = new StringBuilder("{");
            for(int i=0; i<pow; i++) {
                for (int j=0; j<m; j++){
                    set.append(bodyelems[j]).append("|").append(basiselems[(i / (int) pow(n,m-j-1)) % n]);
                    if (j != m-1) set.append(",");
                }
                set.append("}");
                elementNames[i] = set.toString();
                set.setLength(0);
                set.append("{");
            }
        this.body = body;
        this.basis = basis;
    }

    public PowerSetObject(SetObject body) {
        this.body = body;
    }

    public PowerSetObject(String name, SetObject body, Basis basis) {
        this(body,basis);
        this.name = name;

        //once a SetObject is created by hand, put to storage
        SetObjectStorage.getInstance().put(this);
    }

    public SetObject getBody() {
        return body;
    }

    public Basis getBasis() {
        return basis;
    }

    @Override
    public void save() throws IOException {
        SetObjectStorage.getInstance().save(name, toXMLString());
        this.body.save();
        this.basis.getHeytingAlgebra().save();
        this.basis.save();
    }

    @Override
    public String toXMLString() {
        StringBuilder setObjectXML = new StringBuilder();
        setObjectXML.append("<PowerSetObject>\n");

        setObjectXML.append("\t<Component body=\"");
        setObjectXML.append(body.getName().replace("out","")); //remove out keyword

        setObjectXML.append("\"/>\n\t<Component basis=\"");
        setObjectXML.append(basis.getName().replace("out","")); //remove out keyword
        setObjectXML.append("\"/>\n</PowerSetObject>");

        return setObjectXML.toString();
    }

    public String toString() {
        return "\nNumber of Body Elements: " + body.getNumElements() + " Body Elements: " + Arrays.toString(body.getElementNames());
    }
}
