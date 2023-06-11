package main;

import exceptions.RelationException;

import java.util.function.BiFunction;

public class Relation {
    private final SetObject source;
    private final SetObject target;
    private final Basis truth;
    private int[][] matrix;


    public Relation(SetObject source, SetObject target, Basis truth, int n) {
        this.source = source;
        this.target = target;
        this.truth = truth;

        new Relation(this.source, this.target, this.truth,
                fillMatrix(this.source.getNumElements(), this.target.getNumElements(), n));
    }

    public Relation(SetObject source, SetObject target, Basis truth, int[][] matrix) {
        this.source = source;
        this.target = target;
        this.truth = truth;
        this.matrix = matrix;
    }

    private int[][] fillMatrix(int sourceNum, int targetNum, int n) {
        int[][] matrix_ = new int[sourceNum][targetNum];

        for (int i = 0; i < sourceNum; i++) {
            for (int j = 0; j < targetNum; j++) {
                matrix_[i][j] = n;
            }
        }
        return matrix_;
    }

    public Relation converse() {
        int[][] transMatrix = new int[source.getNumElements()][target.getNumElements()];
        for (int i = 0; i < source.getNumElements(); i++) {
            for (int j = 0; j < target.getNumElements(); j++) {
                transMatrix[j][i] = this.matrix[i][j];
            }
        }
        return new Relation(source, target, truth, transMatrix);
    }

    public static Relation ideal(SetObject source, SetObject target, Basis basis, int n) {
        int[][] star = basis.getStar();

        //run n against all values in star
        try {
            if (n <= 0 || n > basis.getNumElements())
                throw new RelationException("The value of n not valid in basis");

            for (int i = 0; i < source.getNumElements(); i++) {
                for (int j = 0; j < target.getNumElements(); j++) {
                    if (star[i][j] != n)
                        throw new RelationException("basis does not contain only n values");
                }
            }
        } catch (RelationException e) {
            throw new RuntimeException(e);
        }
        return new Relation(source, target, basis, n);
    }
    public static Relation bot(SetObject source, SetObject target, Basis basis) {
        return ideal(source, target, basis, basis.getBot());
    }

    public static Relation top(SetObject source, SetObject target, Basis basis) {
        return ideal(source, target, basis, basis.getTop());
    }

    public static Relation scalar(SetObject setObject, Basis basis, int n) {
        return diagonalMatrix(setObject, basis, n, basis.getBot());
    }

    public static Relation identity(SetObject setObject, Basis basis, int n) {
        return diagonalMatrix(setObject, basis, n, basis.getTop());
    }

    private static Relation diagonalMatrix(SetObject setObject, Basis basis, int n, int pos) {
        Relation relation = ideal(setObject, setObject, basis, n);
        int[][] star = basis.getStar();
        int numElements = setObject.getNumElements();
        for (int i = 0; i < numElements; i++) {
            for (int j = 0; j < numElements; j++) {
                //check diagonals else check for bot/top
                try {
                    if (i == j && star[i][j] != n) {
                        throw new RelationException("Basis matrix isn't a valid diagonal matrix");
                    } else {
                        if (star[i][j] != pos) {
                            throw new RelationException("A non diagonal value isn't bot");
                        }
                    }
                } catch (RelationException ex){
                    throw new RuntimeException(ex);
                }
            }
        }
        return relation;
    }

    public Integer meet(Relation r){
        return applyBinary(r, truth::getMeet);
    }

    public Integer join(Relation r){
        return applyBinary(r, truth::getJoin);
    }

    public Integer impl(Relation r){
        return applyBinary(r, truth::getImpl);
    }

    private static Integer applyBinary(Relation r, BiFunction<Integer, Integer, Integer> operation) {
        int s = r.source.getNumElements();
        int t = r.target.getNumElements();

        try {
            if (s != t)
                throw new RelationException("Basis of source and target are not the same");
            else
                return operation.apply(s, t);
        } catch (RelationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Relation composition(Relation r) {
        return multiply(r, false, true, truth::getMeet, truth::getJoin);
    }

    private Relation multiply(Relation r, boolean row1, boolean row2, BiFunction<Integer, Integer, Integer>
            operation, BiFunction<Integer, Integer, Integer> aggregate) {
        int firstMatrix;
        int secondMatrix;
        int[][] resultMatrix;

        //check if basis are the same
        if (this.truth.getNumElements() == r.truth.getNumElements()){
            if (row1 && row2){
                firstMatrix = this.target.getNumElements();
                secondMatrix = r.target.getNumElements(); // OR = firstMatrix ??
            } else if (row1) {
                firstMatrix = this.target.getNumElements();
                secondMatrix = r.source.getNumElements(); // OR = firstMatrix ??
            } else if (row2) {
                firstMatrix = this.source.getNumElements();
                secondMatrix = r.target.getNumElements();
            } else {
                firstMatrix = this.source.getNumElements();
                secondMatrix = r.source.getNumElements();
            }
            resultMatrix = new int[firstMatrix][secondMatrix];
            int len = resultMatrix.length;

            for (int i = 0; i < firstMatrix; i++) {
                for (int j = 0; j < secondMatrix; j++) {
                    int result = 0;

                    //in HeytingAlgebra::computeImpl, len here was the number of elements
                    //i am not sure of this one
                    for (int k = 0; k < len; k++) {
                        result = aggregate.apply(result, operation.apply(this.matrix[i][k], r.matrix[k][j]));
                    }
                    resultMatrix[i][j] = result;
                }
            }

            return new Relation(source, target, truth, resultMatrix);
        }

        return null; //value to return if basis not true
    }
}
