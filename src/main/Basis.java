package main;

import storage.BasisStorage;
import xmlutils.XMLObject;
import java.util.Arrays;
import java.util.List;

public class Basis implements XMLObject {
    private final HeytingAlgebra heytingAlgebra;
    private final int[][] star;

    private String name;

    public Basis(HeytingAlgebra heytingAlgebra, int[][] star) {
        this.heytingAlgebra = heytingAlgebra;
        this.star = star;
    }

    public Basis(String name, HeytingAlgebra heytingAlgebra, int[][] star) {
        this(heytingAlgebra, star);
        this.name = name;

        BasisStorage.getInstance().put(this);
    }

    @Override
    public String toXMLString() {
        StringBuilder starStr = new StringBuilder();
        List<int[]> elementLists = Arrays.stream(star).toList();

        for (int j = 0; j < 2; j++) {
            starStr.append(Arrays.toString(elementLists.get(j))).append(",");
            starStr.append("\n\t\t");
        }

        StringBuilder setObjectXML = new StringBuilder();
        setObjectXML.append("<Basis>\n\t");
        setObjectXML.append("<HeytingAlgebra name=\"");
        setObjectXML.append(heytingAlgebra.getName().replace("out",""));

        starStr = new StringBuilder(starStr.deleteCharAt(starStr.lastIndexOf(",")));

        setObjectXML.append("\"/>\n\t<Star>\n\t\t");
        setObjectXML.append(starStr.toString().strip().replaceAll("[\\[\\] ]", ""));
        setObjectXML.append("\n\t</Star>");
        setObjectXML.append("\n</Basis>");

        return setObjectXML.toString();
    }

    public HeytingAlgebra getHeytingAlgebra() {
        return heytingAlgebra;
    }

    public int[][] getStar() {
        return star;
    }

    public int getNumElements(){
        return heytingAlgebra.getNumElements();
    }

    public String[] getElementNames(){
        return heytingAlgebra.getElementNames();
    }
    public String getElementNames(Integer integer) {
        return heytingAlgebra.getElementNames()[integer];
    }
    public int getBot(){
        return heytingAlgebra.getBot();
    }

    public int getTop(){
        return heytingAlgebra.getTop();
    }

    public int getMeet(int x, int y) {
        return heytingAlgebra.getMeet(x, y);
    }

    public int getJoin(int x, int y) {
        return heytingAlgebra.getJoin(x, y);
    }

    public int getImpl(int x, int y) {
        return heytingAlgebra.getImpl(x, y);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        StringBuilder starStr = new StringBuilder();
        List<int[]> elementLists = Arrays.stream(star).toList();

        for (int j = 0; j < 2; j++) {
            starStr.append(Arrays.toString(elementLists.get(j))).append(",");
            starStr.append("\n");
        }

        return "Basis -> HeytingAlgebra: "+heytingAlgebra.toString() + "\n Star Matrix: \n"+starStr;
    }
}
