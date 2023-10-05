package main;

import exceptions.OperationExecutionException;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class Relation extends Relationals{
    
    private final SetObject source;
    private final SetObject target;
    private final Basis truth;
    private final int[][] matrix;

    public Relation(SetObject source, SetObject target, Basis truth, int[][] matrix) {
        this.source = source;
        this.target = target;
        this.truth = truth;
        this.matrix = matrix;
    }
    public Relation(String name, SetObject source, SetObject target, Basis truth, int[][] matrix) {
        this(source, target, truth, matrix);
        this.name = name;
    }
    
    public static Relation ideal(SetObject source, SetObject target, Basis basis, int n) {
        if (0 <= n && n < basis.getNumElements()) {
            int[][] matrix = new int[source.getNumElements()][target.getNumElements()];
            for (int i = 0; i < source.getNumElements(); i++) {
                for (int j = 0; j < target.getNumElements(); j++) {
                    matrix[i][j] = n;
                }
            }
            return new Relation(source, target, basis, matrix);
        } else {
            throw new OperationExecutionException("The value of n isn't valid in basis");
        }
    }
    public static Relation bot(SetObject source, SetObject target, Basis basis) {
        Relation result = null;
        try {
            result = ideal(source, target, basis, basis.getBot()); // this exception should never happen!
        } catch (OperationExecutionException ex) {}
        return result;
    }

    public static Relation top(SetObject source, SetObject target, Basis basis) {
        Relation result = null;
        try {
            result = ideal(source, target, basis, basis.getTop()); // this exception should never happen!
        } catch (OperationExecutionException ex) {}
        return result;
    }

    public static Relation scalar(SetObject setObject, Basis basis, int n) {
        if (0 <= n && n < basis.getNumElements()) {
            int numElements = setObject.getNumElements();
            int bot = basis.getBot();
            int[][] matrix = new int[numElements][numElements];
            for (int i = 0; i < numElements; i++) {
                for (int j = 0; j < numElements; j++) {
                    matrix[i][j] = i==j? n : bot;
                }
            }
            return new Relation(setObject, setObject, basis, matrix);
        } else {
            throw new OperationExecutionException("The valid of n isn't valid in basis");
        }
    }
    
    public static Relation identity(SetObject setObject, Basis basis, int n) {
        Relation result = null;
        try {
            result = scalar(setObject, basis, basis.getTop()); // this exception should never happen!
        } catch (OperationExecutionException ex) {}
        return result;
    }

    public static Relation pi(ProductSetObject productSetObject, Basis basis){
        SetObject left = productSetObject.getLeft();
        int bot = basis.getBot();
        int top = basis.getTop();
        int pels = productSetObject.getNumElements();
        int lels = left.getNumElements();
        int rels = productSetObject.getRight().getNumElements();
        int[][] matrix = new int[pels][lels];
        for (int i = 0; i<pels; i++) {
            for (int j = 0; j < lels; j++) {
                matrix[i][j] = i / rels == j ? top : bot;
            }
        }
        return new Relation(productSetObject, left, basis, matrix);
    }

    public static Relation rho(ProductSetObject productSetObject, Basis basis){
        SetObject right = productSetObject.getRight();
        int bot = basis.getBot();
        int top = basis.getTop();
        int pels = productSetObject.getNumElements();
        int rels = right.getNumElements();
        int[][] matrix = new int[pels][rels];
        for (int i = 0; i<pels; i++) {
            for (int j = 0; j < rels; j++) {
                matrix[i][j] = i % rels == j ? top : bot;
            }
        }
        return new Relation(productSetObject, right, basis, matrix);
    }

    public static Relation iota(SumSetObject sumSetObject, Basis basis){
        SetObject left = sumSetObject.getLeft();
        int bot = basis.getBot();
        int top = basis.getTop();
        int sels = sumSetObject.getNumElements();
        int lels = left.getNumElements();
        int[][] matrix = new int[lels][sels];
        for (int i = 0; i<lels; i++) {
            for (int j = 0; j < sels; j++) {
                matrix[i][j] = i % lels == j ? top : bot;
            }
        }
        return new Relation(left, sumSetObject, basis, matrix);
    }

    public static Relation kappa(SumSetObject sumSetObject, Basis basis){
        SetObject right = sumSetObject.getRight();
        int bot = basis.getBot();
        int top = basis.getTop();
        int sels = sumSetObject.getNumElements();
        int rels = right.getNumElements();
        int[][] matrix = new int[rels][sels];
        for (int i = 0; i<rels; i++) {
            for (int j = sels; j > 0; j--) {
                matrix[rels-1-i][j-1] = sels - i == j ? top : bot;
            }
        }
        return new Relation(right, sumSetObject, basis, matrix);
    }

    public static Relation epsi(PowerSetObject powerSetObject){
        SetObject body = powerSetObject.getBody();
        Basis basis = powerSetObject.getBasis();
        int source = body.getNumElements();
        int target = powerSetObject.getNumElements();
        int[][] matrix = new int[source][target];
        for (int i = 0; i<source; i++) {
            for (int j = 0; j< target; j++) {
                //get set at j and subset at i, then degree element at 1
                String degreeElement = powerSetObject.getElementNames()[j] //get set a j
                        .replaceAll("[{}]", "") //replace {}
                        .split(",")[i] //get subset at i
                        .split("\\|")[1]; // get degree element at 1

                matrix[i][j] = Arrays.stream(basis.getElementNames()).toList().indexOf(degreeElement);
            }
        }
        return new Relation(body, powerSetObject, basis, matrix);
    }
    
    public int[][] getMatrix() {
        return matrix;
    }
    
    public Relation converse() {
        int[][] transMatrix = new int[target.getNumElements()][source.getNumElements()];
        for (int i = 0; i < source.getNumElements(); i++) {
            for (int j = 0; j < target.getNumElements(); j++) {
                transMatrix[j][i] = this.matrix[i][j];
            }
        }
        return new Relation(target, source, truth, transMatrix);
    }
    public Relation complement() {
        int[][] compMatrix = new int[source.getNumElements()][target.getNumElements()];
        int bot = truth.getBot();
        for (int i = 0; i < source.getNumElements(); i++) {
            for (int j = 0; j < target.getNumElements(); j++) {
                compMatrix[i][j] = truth.getImpl(this.matrix[i][j],bot);
            }
        }
        return new Relation(source, target, truth, compMatrix);
    }

    public Relation meet(Relation r){
        return applyBinary(r, truth::getMeet);
    }

    public Relation join(Relation r){
        return applyBinary(r, truth::getJoin);
    }

    public Relation impl(Relation r){
        return applyBinary(r, truth::getImpl);
    }

    public Relation leftResidual(Relation r) {
        return multiply(r, false, false, truth::getImpl, truth::getMeet);
    }

    public Relation rightResidual(Relation r) {
        return r.multiply(this, true, true, truth::getImpl, truth::getMeet);
    }

    public Relation syq(Relation r) {
        BiFunction<Integer, Integer, Integer> equiv = (i, j) -> truth.getMeet(truth.getImpl(i, j),truth.getImpl(j, i));
        return multiply(r, false, false, equiv, truth::getMeet);
    }
    
    public Relation composition(Relation r) {
        return multiply(r, true, false, truth::getMeet, truth::getJoin);
    }

    private Relation applyBinary(Relation r, BiFunction<Integer, Integer, Integer> operation) {
        if (this.source == r.source && this.target == r.target) {
            int sourceEl = this.source.getNumElements();
            int targetEl = this.target.getNumElements();
            int[][] matrix_ = new int[sourceEl][targetEl];
            for (int i=0; i< sourceEl; i++) {
                for (int j=0; j < targetEl; j++) {
                    matrix_[i][j] = operation.apply(this.matrix[i][j], r.matrix[i][j]);
                }
            }
            return new Relation(this.source, this.target, this.truth, matrix_ );
        } else {
            throw new OperationExecutionException("Relations target and source objects are must be the same");
        }
    }

    private Relation multiply(Relation r, boolean row1, boolean row2, BiFunction<Integer, Integer, Integer> operation, BiFunction<Integer, Integer, Integer> aggregate) {
        Basis resultBasis;
        BiFunction<Integer, Integer, Integer> accessThis;
        BiFunction<Integer, Integer, Integer> accessR;
        SetObject resultSource;
        SetObject resultTarget;
        SetObject rangeObject;

        if (this.truth == r.truth) {
            resultBasis = truth;
            if (row1) {
                resultSource = this.source;
                rangeObject = this.target;
                accessThis = (i,j) -> this.matrix[i][j];
                if (row2) {
                    if (this.target==r.target) {
                        accessR = (i,j) -> r.matrix[i][j];
                        resultTarget = r.source;
                    } else {
                        throw new OperationExecutionException("Relations Target objects must be the same");
                    }
                } else {
                    if (this.target==r.source) {
                        accessR = (i,j) -> r.matrix[j][i];
                        resultTarget = r.target;
                    } else {
                        throw new OperationExecutionException("Relations Target and Source objects must be the same");
                    }
                }
            } else {
                resultSource = this.target;
                rangeObject = this.source;
                accessThis = (i,j) -> this.matrix[j][i];
                if (row2) {
                    if (this.source==r.target) {
                        accessR = (i,j) -> r.matrix[i][j];
                        resultTarget = r.source;
                    } else {
                        throw new OperationExecutionException("Relations Source and Target objects must be the same");
                    }
                } else {
                    if (this.source==r.source) {
                        accessR = (i,j) -> r.matrix[j][i];
                        resultTarget = r.target;
                    } else {
                        throw new OperationExecutionException("Relations source objects must be the same");
                    }
                }
            }
        } else {
            throw new OperationExecutionException("Relation basis must be the same");
        }

        int[][] resultMatrix = new int[resultSource.getNumElements()][resultTarget.getNumElements()];
        for (int i = 0; i < resultSource.getNumElements(); i++) {
            for (int j = 0; j < resultTarget.getNumElements(); j++) {
                int element = operation.apply(accessThis.apply(i,0),accessR.apply(j,0));
                for (int k = 1; k < rangeObject.getNumElements(); k++) {
                    element = aggregate.apply(element, operation.apply(accessThis.apply(i,k),accessR.apply(j,k)));
                }
                resultMatrix[i][j] = element;
            }
        }

        return new Relation(resultSource,resultTarget,resultBasis,resultMatrix);
    }

    @Override
    public String toString() {
        StringBuilder matrixStr = new StringBuilder();
        for (int i = 0; i < source.getNumElements(); i++) {
            for (int j = 0; j < target.getNumElements(); j++) {
                matrixStr.append(matrix[i][j]).append(" ");
            }
            matrixStr.append("\n");
        }
        return "Relation{" +
                "matrix=\n" + matrixStr +
                '}';
    }

    @Override
    public String toXMLString() {
            StringBuilder relationMatrix = new StringBuilder();
            for (int i = 0; i < source.getNumElements(); i++) {
                for (int j = 0; j < target.getNumElements(); j++) {
                    relationMatrix.append(this.getMatrix()[i][j]).append(",");
                }
                relationMatrix.append("\n\t\t");
            }

            StringBuilder relationXML = new StringBuilder();
            relationXML.append("<Relation>\n");

            relationXML.append("\t<Basis name=\"");
            relationXML.append(truth.getName().replace("out","")); //remove out keyword

            relationXML.append("\"/>\n\t<SetObject Source=\"");
            relationXML.append(source.getName().replace("out","")); //remove out keyword
            relationXML.append("\" Target=\"");
            relationXML.append(target.getName().replace("out","")); //remove out keyword

            relationMatrix = new StringBuilder(relationMatrix.deleteCharAt(relationMatrix.lastIndexOf(",")));
            relationXML.append("\"/>\n\t<Matrix>\n\t\t");
            relationXML.append(relationMatrix.toString().strip());
            relationXML.append("\n\t</Matrix>\n");

            relationXML.append("</Relation>");

            return relationXML.toString();
    }

    public SetObject getDom() {
        return source;
    }
    public SetObject getCod() {
        return target;
    }
    public Basis getAlgebra() {
        return truth;
    }
}
