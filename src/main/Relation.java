package main;

public class Relation {
    private SetObject source;
    private SetObject target;
    private Basis truth;
    private int[][] matrix;

    public Relation() {
        this.matrix = new int[source.getNumElements()][target.getNumElements()];
    }

    private void converseOfMatrix(){
        int[][] temp = new int[source.getNumElements()][target.getNumElements()];
        for (int i = 0; i < source.getNumElements(); i++) {
            for (int j = 0; j < target.getNumElements(); j++) {
                temp[j][i] = this.matrix[i][j];
            }
        }
        this.matrix = temp;
    }
}
