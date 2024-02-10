package relations;

import exceptions.OperationExecutionException;
import main.Basis;
import sets.PowerSetObject;
import sets.ProductSetObject;
import sets.SetObject;
import sets.SumSetObject;
import typeterm.Power;
import typeterm.Product;
import typeterm.Sum;
import typeterm.Typeterm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Relation extends Relationals{

    private final SetObject source;
    private final SetObject target;
    private final Basis truth;
    private final int[][] matrix;
    private final Typeterm sourceTerm;
    private final Typeterm targetTerm;
    private final Map<String,SetObject> params;

    private Relation(Typeterm sourceTerm, Typeterm targetTerm, SetObject source, SetObject target, Map<String, SetObject> params, Basis truth, int[][] matrix) {
        this.sourceTerm = sourceTerm;
        this.targetTerm = targetTerm;
        this.params = params;
        this.truth = truth;
        this.matrix = matrix;
        this.source = source;
        this.target = target;
    }
    
    public Relation(Typeterm sourceTerm, Typeterm targetTerm, Map<String, SetObject> params, Basis truth, int[][] matrix) {
        this(sourceTerm,targetTerm,sourceTerm.execute(params, truth),targetTerm.execute(params, truth),params,truth,matrix);
    }
    
    public static Relation ideal(Typeterm sourceTerm, Typeterm targetTerm, Map<String, SetObject> params, Basis basis, int n) {
        SetObject source = sourceTerm.execute(params, basis);
        SetObject target = targetTerm.execute(params, basis);
        if (0 <= n && n < basis.getNumElements()) {
            int[][] matrix = new int[source.getNumElements()][target.getNumElements()];
            for (int i = 0; i < source.getNumElements(); i++) {
                for (int j = 0; j < target.getNumElements(); j++) {
                    matrix[i][j] = n;
                }
            }

            Map<String,SetObject> newParams = new HashMap<>();
            sourceTerm.variables().forEach(var -> newParams.put(var, params.get(var)));
            targetTerm.variables().forEach(var -> newParams.put(var, params.get(var)));

            return new Relation(sourceTerm, targetTerm, source, target, newParams, basis, matrix);
        } else {
            throw new OperationExecutionException("The value of n isn't valid in basis");
        }
    }
    public static Relation bot(Typeterm sourceTerm, Typeterm targetTerm, Map<String, SetObject> params, Basis basis) {
        Relation result = null;
        try {
            Map<String,SetObject> newParams = new HashMap<>();
            sourceTerm.variables().forEach(var -> newParams.put(var, params.get(var)));
            targetTerm.variables().forEach(var -> newParams.put(var, params.get(var)));

            result = ideal(sourceTerm, targetTerm, newParams, basis, basis.getBot()); // this exception should never happen!
        } catch (OperationExecutionException ex) {}
        return result;
    }

    public static Relation top(Typeterm sourceTerm, Typeterm targetTerm, Map<String, SetObject> params, Basis basis) {
        Relation result = null;
        try {
            Map<String,SetObject> newParams = new HashMap<>();
            sourceTerm.variables().forEach(var -> newParams.put(var, params.get(var)));
            targetTerm.variables().forEach(var -> newParams.put(var, params.get(var)));

            result = ideal(sourceTerm, targetTerm, newParams, basis, basis.getTop()); // this exception should never happen!
        } catch (OperationExecutionException ex) {}
        return result;
    }

    public static Relation scalar(Typeterm setObjectTerm, Map<String, SetObject> params, Basis basis, int n) {
        SetObject setObject = setObjectTerm.execute(params, basis);
        if (0 <= n && n < basis.getNumElements()) {
            int numElements = setObject.getNumElements();
            int bot = basis.getBot();
            int[][] matrix = new int[numElements][numElements];
            for (int i = 0; i < numElements; i++) {
                for (int j = 0; j < numElements; j++) {
                    matrix[i][j] = i==j? n : bot;
                }
            }
            Map<String,SetObject> newParams = new HashMap<>();
            setObjectTerm.variables().forEach(var -> newParams.put(var, params.get(var)));

            return new Relation(setObjectTerm, setObjectTerm, setObject, setObject, newParams, basis, matrix);
        } else {
            throw new OperationExecutionException("The valid of n isn't valid in basis");
        }
    }
    
    public static Relation identity(Typeterm setObjectTerm, Map<String, SetObject> params, Basis basis) {
        Relation result = null;
        try {
            Map<String,SetObject> newParams = new HashMap<>();
            setObjectTerm.variables().forEach(var -> newParams.put(var, params.get(var)));

            result = scalar(setObjectTerm, newParams, basis, basis.getTop()); // this exception should never happen!
        } catch (OperationExecutionException ex) {}
        return result;
    }

    public static Relation pi(Product productSetObjectTerm, Map<String, SetObject> params, Basis basis){
        Typeterm leftTerm = productSetObjectTerm.getLeft();
        Typeterm rightTerm = productSetObjectTerm.getRight();
        SetObject left = leftTerm.execute(params,basis);
        SetObject right = rightTerm.execute(params,basis);
        int bot = basis.getBot();
        int top = basis.getTop();
        int lels = left.getNumElements();
        int rels = right.getNumElements();
        int pels = lels*rels;
        int[][] matrix = new int[pels][lels];
        for (int i = 0; i<pels; i++) {
            for (int j = 0; j < lels; j++) {
                matrix[i][j] = i / rels == j ? top : bot;
            }
        }

        Map<String,SetObject> newParams = new HashMap<>();
        productSetObjectTerm.variables().forEach(var -> newParams.put(var, params.get(var)));
        leftTerm.variables().forEach(var -> newParams.put(var, params.get(var)));

        return new Relation(productSetObjectTerm, leftTerm, new ProductSetObject(left,right), left, newParams, basis, matrix);
    }

    public static Relation rho(Product productSetObjectTerm, Map<String, SetObject> params, Basis basis){
        Typeterm leftTerm = productSetObjectTerm.getLeft();
        Typeterm rightTerm = productSetObjectTerm.getRight();
        SetObject left = leftTerm.execute(params,basis);
        SetObject right = rightTerm.execute(params,basis);
        int bot = basis.getBot();
        int top = basis.getTop();
        int lels = left.getNumElements();
        int rels = right.getNumElements();
        int pels = lels*rels;
        int[][] matrix = new int[pels][lels];
        for (int i = 0; i<pels; i++) {
            for (int j = 0; j < rels; j++) {
                matrix[i][j] = i % rels == j ? top : bot;
            }
        }
        Map<String,SetObject> newParams = new HashMap<>();
        productSetObjectTerm.variables().forEach(var -> newParams.put(var, params.get(var)));
        rightTerm.variables().forEach(var -> newParams.put(var, params.get(var)));

        return new Relation(productSetObjectTerm, rightTerm, new ProductSetObject(left,right), right, newParams, basis, matrix);
    }

    public static Relation iota(Sum sumSetObjectTerm, Map<String, SetObject> params, Basis basis){
        Typeterm leftTerm = sumSetObjectTerm.getLeft();
        Typeterm rightTerm = sumSetObjectTerm.getRight();
        SetObject left = leftTerm.execute(params,basis);
        SetObject right = rightTerm.execute(params,basis);
        int bot = basis.getBot();
        int top = basis.getTop();
        int lels = left.getNumElements();
        int rels = right.getNumElements();
        int sels = lels+rels;
        int[][] matrix = new int[lels][sels];
        for (int i = 0; i<lels; i++) {
            for (int j = 0; j < sels; j++) {
                matrix[i][j] = i % lels == j ? top : bot;
            }
        }

        Map<String,SetObject> newParams = new HashMap<>();
        leftTerm.variables().forEach(var -> newParams.put(var, params.get(var)));
        sumSetObjectTerm.variables().forEach(var -> newParams.put(var, params.get(var)));

        return new Relation(leftTerm, sumSetObjectTerm, left, new SumSetObject(left,right), newParams, basis, matrix);
    }

    public static Relation kappa(Sum sumSetObjectTerm, Map<String, SetObject> params, Basis basis){
        Typeterm leftTerm = sumSetObjectTerm.getLeft();
        Typeterm rightTerm = sumSetObjectTerm.getRight();
        SetObject left = leftTerm.execute(params,basis);
        SetObject right = rightTerm.execute(params,basis);
        int bot = basis.getBot();
        int top = basis.getTop();
        int lels = left.getNumElements();
        int rels = right.getNumElements();
        int sels = lels+rels;
        int[][] matrix = new int[rels][sels];
        for (int i = 0; i<rels; i++) {
            for (int j = 0; j < sels; j++) {
                matrix[i][j] = i % rels == j ? top : bot;
            }
        }

        Map<String,SetObject> newParams = new HashMap<>();
        rightTerm.variables().forEach(var -> newParams.put(var, params.get(var)));
        sumSetObjectTerm.variables().forEach(var -> newParams.put(var, params.get(var)));

        return new Relation(rightTerm, sumSetObjectTerm, right, new SumSetObject(left,right), newParams, basis, matrix);
    }

    public static Relation epsi(Power powerSetObjectTerm, Map<String, SetObject> params, Basis basis){
        Typeterm bodyTerm = powerSetObjectTerm.getBody();
        SetObject body = bodyTerm.execute(params, basis);
        SetObject powerSetObject = new PowerSetObject(body,basis);
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

        Map<String,SetObject> newParams = new HashMap<>();
        bodyTerm.variables().forEach(var -> newParams.put(var, params.get(var)));
        powerSetObjectTerm.variables().forEach(var -> newParams.put(var, params.get(var)));

        return new Relation(bodyTerm, powerSetObjectTerm, body, powerSetObject, newParams, basis, matrix);
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

        Map<String,SetObject> newParams = new HashMap<>();
        sourceTerm.variables().forEach(var -> newParams.put(var, params.get(var)));
        targetTerm.variables().forEach(var -> newParams.put(var, params.get(var)));

        return new Relation(targetTerm, sourceTerm, target, source, newParams, truth, transMatrix);
    }
    public Relation complement() {
        int[][] compMatrix = new int[source.getNumElements()][target.getNumElements()];
        int bot = truth.getBot();
        for (int i = 0; i < source.getNumElements(); i++) {
            for (int j = 0; j < target.getNumElements(); j++) {
                compMatrix[i][j] = truth.getImpl(this.matrix[i][j],bot);
            }
        }

        Map<String,SetObject> newParams = new HashMap<>();
        sourceTerm.variables().forEach(var -> newParams.put(var, params.get(var)));
        targetTerm.variables().forEach(var -> newParams.put(var, params.get(var)));

        return new Relation(sourceTerm, targetTerm, source, target, newParams, truth, compMatrix);
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
    public Relation composition(Relation r) {
        return multiply(r, true, false, truth::getMeet, truth::getJoin);
    }

    public Relation syq(Relation r) {
        BiFunction<Integer, Integer, Integer> equiv = (i, j) -> truth.getMeet(truth.getImpl(i, j),truth.getImpl(j, i));
        return multiply(r, false, false, equiv, truth::getMeet);
    }

    public Relation star(Relation r){
        return applyBinary(r, truth::getStar);
    }
    public Relation starImpl(Relation r){
        return applyBinary(r, truth::getStarImpl);
    }

    public Relation starLeftResidual(Relation r) {
        return multiply(r, false, false, truth::getStarImpl, truth::getMeet);
    }

    public Relation starRightResidual(Relation r) {
        return r.multiply(this, true, true, truth::getStarImpl, truth::getMeet);
    }

    public Relation starSyq(Relation r) {
        BiFunction<Integer, Integer, Integer> equiv = (i, j) -> truth.getMeet(truth.getStarImpl(i, j),truth.getStarImpl(j, i));
        return multiply(r, false, false, equiv, truth::getMeet);
    }
    public Relation starComposition(Relation r) {
        return multiply(r, true, false, truth::getStar, truth::getJoin);
    }

    public Relation up(){
        int sourceEl = source.getNumElements();
        int targetEl = target.getNumElements();
        int[][] upMatrix = new int[sourceEl][targetEl];

        for (int i=0; i< source.getNumElements(); i++){
            for (int j=0; j< target.getNumElements(); j++){
                upMatrix[i][j] = this.matrix[i][j] == truth.getBot()? truth.getBot(): truth.getTop();
            }
        }

        Map<String,SetObject> newParams = new HashMap<>();
        sourceTerm.variables().forEach(var -> newParams.put(var, params.get(var)));
        targetTerm.variables().forEach(var -> newParams.put(var, params.get(var)));

        return new Relation(sourceTerm, targetTerm, source, target, newParams, truth, upMatrix);
    }
    public Relation down(){
        int sourceEl = source.getNumElements();
        int targetEl = target.getNumElements();
        int[][] downMatrix = new int[sourceEl][targetEl];

        for (int i=0; i< source.getNumElements(); i++){
            for (int j=0; j< target.getNumElements(); j++){
                downMatrix[i][j] = this.matrix[i][j] == truth.getTop()? truth.getTop(): truth.getBot();
            }
        }

        Map<String,SetObject> newParams = new HashMap<>();
        sourceTerm.variables().forEach(var -> newParams.put(var, params.get(var)));
        targetTerm.variables().forEach(var -> newParams.put(var, params.get(var)));

        return new Relation(sourceTerm, targetTerm, source, target, newParams, truth, downMatrix);
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
            Map<String,SetObject> newParams = new HashMap<>();
            sourceTerm.variables().forEach(var -> newParams.put(var, params.get(var)));
            r.targetTerm.variables().forEach(var -> newParams.put(var, r.params.get(var)));

            return new Relation(sourceTerm, targetTerm, source, target, newParams, truth, matrix_ );
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
        Typeterm resultSourceTerm;
        Typeterm resultTargetTerm;

        if (this.truth == r.truth) {
            resultBasis = truth;
            if (row1) {
                resultSource = this.source;
                resultSourceTerm = sourceTerm;
                rangeObject = this.target;
                accessThis = (i,j) -> this.matrix[i][j];
                if (row2) {
                    if (this.target==r.target) {
                        accessR = (i,j) -> r.matrix[i][j];
                        resultTarget = r.source;
                        resultTargetTerm = r.sourceTerm;
                    } else {
                        throw new OperationExecutionException("Relations Target objects must be the same");
                    }
                } else {
                    if (this.target==r.source) {
                        accessR = (i,j) -> r.matrix[j][i];
                        resultTarget = r.target;
                        resultTargetTerm = r.targetTerm;
                    } else {
                        throw new OperationExecutionException("Relations Target and Source objects must be the same");
                    }
                }
            } else {
                resultSource = this.target;
                resultSourceTerm = targetTerm;
                rangeObject = this.source;
                accessThis = (i,j) -> this.matrix[j][i];
                if (row2) {
                    if (this.source==r.target) {
                        accessR = (i,j) -> r.matrix[i][j];
                        resultTarget = r.source;
                        resultTargetTerm = r.sourceTerm;
                    } else {
                        throw new OperationExecutionException("Relations Source and Target objects must be the same");
                    }
                } else {
                    if (this.source==r.source) {
                        accessR = (i,j) -> r.matrix[j][i];
                        resultTarget = r.target;
                        resultTargetTerm = r.targetTerm;
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
        
        Map<String,SetObject> resultParams = new HashMap<>();
        for(String var : resultSourceTerm.variables()) {
            resultParams.put(var,params.get(var));
        }
        for(String var: resultTargetTerm.variables()) {
            resultParams.put(var,r.params.get(var));
        }

        return new Relation(resultSourceTerm, resultTargetTerm, resultSource,resultTarget,resultParams,resultBasis,resultMatrix);
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
    public Typeterm getSourceTerm() {
        return sourceTerm;
    }

    public Typeterm getTargetTerm() {
        return targetTerm;
    }

    public Basis getTruth() {
        return truth;
    }

    public Map<String, SetObject> getParams() {
        return params;
    }
}
