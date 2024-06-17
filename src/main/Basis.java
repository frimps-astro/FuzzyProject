package main;

import exceptions.InvalidXYvalueException;
import storage.BasisStorage;
import xmlutils.XMLObject;
import java.util.Arrays;
import java.util.List;

public class Basis implements XMLObject {
    private final HeytingAlgebra heytingAlgebra;
    private final int[][] star;
    private int[][] starImpl;
    private final int[][] plus;
    private int[][] minus;

    private String name;

    public Basis(HeytingAlgebra heytingAlgebra, int[][] star, int[][] plus) {
        this.heytingAlgebra = heytingAlgebra;
        this.star = star;
        this.plus = plus;

        computeStarImpl();
        computeMinus();
    }

    public Basis(String name, HeytingAlgebra heytingAlgebra, int[][] star, int[][] plus) {
        this(heytingAlgebra, star, plus);
        this.name = name;

        BasisStorage.getInstance().put(this);
    }

    @Override
    public String toXMLString() {
        StringBuilder starStr = new StringBuilder();
        List<int[]> elementLists = Arrays.stream(star).toList();

        for (int j = 0; j < star.length; j++) {
            starStr.append(Arrays.toString(elementLists.get(j))).append(",");
            starStr.append("\n\t\t");
        }

        StringBuilder plusStr = new StringBuilder();
        List<int[]> plustlist = Arrays.stream(plus).toList();

        for (int j = 0; j < plus.length; j++) {
            plusStr.append(Arrays.toString(plustlist.get(j))).append(",");
            plusStr.append("\n\t\t");
        }

        StringBuilder setObjectXML = new StringBuilder();
        setObjectXML.append("<Basis>\n\t");
        setObjectXML.append("<HeytingAlgebra name=\"");
        setObjectXML.append(heytingAlgebra.getName().replace("out",""));

        starStr = new StringBuilder(starStr.deleteCharAt(starStr.lastIndexOf(",")));

        setObjectXML.append("\"/>\n\t<Star>\n\t\t");
        setObjectXML.append(starStr.toString().strip().replaceAll("[\\[\\] ]", ""));
        setObjectXML.append("\n\t</Star>");

        setObjectXML.append("\n\t<Plus>\n\t\t");
        setObjectXML.append(plusStr.toString().strip().replaceAll("[\\[\\] ]", ""));
        setObjectXML.append("\n\t</Plus>");
        setObjectXML.append("\n</Basis>");

        return setObjectXML.toString();
    }

    public HeytingAlgebra getHeytingAlgebra() {
        return heytingAlgebra;
    }

    public int getStar(int x, int y) {
        return star[x][y];
    }
    public int getPlus(int x, int y) {
        return plus[x][y];
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
    public int getStarImpl(int x, int y) {
        return starImpl[x][y];
    }
    public int getMinus(int x, int y) {
        return minus[x][y];
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

    private void computeStarImpl(){
        int numElements = heytingAlgebra.getNumElements();
        int bot = heytingAlgebra.getBot();
        int[][] join = heytingAlgebra.getJoin();

        starImpl = new int[numElements][numElements];

        for (int x = 0; x < numElements; x++) {
            for (int y = 0; y < numElements; y++) {
                int result = bot;
                for (int z = 0; z < numElements; z++) {
                    if (leq(star[z][x], y)){
                        result = join[result][z];
                    }
                }
                starImpl[x][y] = result;
            }
        }
    }

    private void computeMinus(){
        int numElements = heytingAlgebra.getNumElements();
        int top = heytingAlgebra.getTop();
        int[][] meet = heytingAlgebra.getMeet();

        minus = new int[numElements][numElements];

        for (int x = 0; x < numElements; x++) {
            for (int y = 0; y < numElements; y++) {
                int result = top;
                for (int z = 0; z < numElements; z++) {
                    if (leq(x,plus[z][y])){
                        result = meet[result][z];
                    }
                }
                minus[x][y] = result;
            }
        }
        System.out.println(Arrays.deepToString(plus));
        System.out.println(Arrays.deepToString(minus));
    }
    private boolean leq(int x, int y) {
        return heytingAlgebra.getMeet()[x][y] == x;
    }

    @Override
    public String toString() {
        StringBuilder starStr = new StringBuilder();
        StringBuilder plusStr = new StringBuilder();
        List<int[]> elementLists = Arrays.stream(star).toList();
        List<int[]> pluslist = Arrays.stream(plus).toList();

        for (int j = 0; j < 2; j++) {
            starStr.append(Arrays.toString(elementLists.get(j))).append(",");
            starStr.append("\n");
        }

        for (int j = 0; j < 2; j++) {
            plusStr.append(Arrays.toString(pluslist.get(j))).append(",");
            plusStr.append("\n");
        }

        return "Basis -> HeytingAlgebra: "+heytingAlgebra.toString() + "\n Star Matrix: \n"+starStr + "\n Plus Matrix: \n"+starStr;
    }
}
