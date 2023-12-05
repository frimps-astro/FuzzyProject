package sets;

public class Unit extends SetObject{
    public Unit() {
        String[] elems = new String[1];
        elems[0] = "\u22C6";
        this.elementNames = elems;
    }

    @Override
    public String toXMLString() {
        return null;
    }
}
