package sets;

import storage.SetObjectStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.pow;
public class PowerSetObject extends SetObject {
    private final SetObject body;
    public PowerSetObject(SetObject body) {
        List<String> bodyelems = Arrays.stream(body.getElementNames()).toList();
        int m = body.getNumElements();
        int pow = (int)pow(2, m);
        elementNames = new String[pow];
        int c = pow-1;
        System.out.println();
        for (List<String> s : powerSet(bodyelems)) {
            Collections.reverse(s);
            elementNames[c] = s.toString().replace("[","{")
                    .replace("]","}");
            c--;
        }
        this.body = body;
    }

    private List<List<String>> powerSet(List<String> initialSet) {
        List<List<String>> sets = new ArrayList<>();
        if (initialSet.isEmpty()) {
            sets.add(new ArrayList<>());
            return sets;
        }
        List<String> list = new ArrayList<>(initialSet);
        String h = list.getLast();
        List<String> remaining = list.subList(0, list.size()-1);
        for (List<String> s : powerSet(remaining)) {
            List<String> n = new ArrayList<>();
            n.add(h);
            n.addAll(s);
            sets.add(n);
            sets.add(s);
        }
        return sets;
    }

    public PowerSetObject(String name, SetObject body) {
        this(body);
        this.name = name;

        //once a SetObject is created by hand, put to storage
        SetObjectStorage.getInstance().put(this);
    }

    public SetObject getBody() {
        return body;
    }


    @Override
    public String toXMLString() {
        StringBuilder setObjectXML = new StringBuilder();
        setObjectXML.append("<PowerSetObject>\n");

        setObjectXML.append("\t<Component body=\"");
        setObjectXML.append(body.getName().replace("out","")); //remove out keyword

        setObjectXML.append("\"/>\n</PowerSetObject>");

        return setObjectXML.toString();
    }

    @Override
    public String toString() {
        return STR."PowerSetObject{body=\{body}, name='\{name}\{'\''}, elementNames=\{Arrays.toString(elementNames)}\{'}'}";
    }
}
