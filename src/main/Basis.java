package main;

public class Basis {
    private HeytingAlgebra heytingAlgebra;
    private int[][] star;

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
}
