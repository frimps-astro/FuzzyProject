package typeterm;

import exceptions.UnificationException;
import main.Basis;
import sets.ProductSetObject;
import sets.SetObject;

import java.util.Map;
import java.util.Set;

public class Product extends Typeterm {
    private final Typeterm left;
    private final Typeterm right;
    private static final int precedence = 20;

    public Typeterm getLeft() {
        return left;
    }

    public Typeterm getRight() {
        return right;
    }
    public Product(Typeterm left, Typeterm right){
        this.left = left;
        this.right = right;
    }

    @Override
    public String toStringPrec(int prec) {
        String result = left.toStringPrec(precedence) + "*" + right.toStringPrec(precedence);
        if (prec > precedence) result = "(" + result + ")";
        return result;
    }

    @Override
    public Set<String> variables() {
        Set<String> vars = left.variables();
        vars.addAll(right.variables());
        return vars;
    }

    @Override
    public Typeterm substitute(Map<String, Typeterm> subst) {
        Typeterm newLeft = left.substitute(subst);
        Typeterm newRight = right.substitute(subst);

        return new Product(newLeft, newRight);
    }

    @Override
    public SetObject execute(Map<String, SetObject> params, Basis basis) {
        return new ProductSetObject(left.execute(params, basis), right.execute(params, basis));
    }

    @Override
    public void unify(boolean unify, Typeterm other, Map<String, Typeterm> unifier) throws UnificationException {
        if (unify && other instanceof TypeVariable){
            other.unify(true, this, unifier);
        } else if (other instanceof Product otherProd){
            left.unify(unify, otherProd.left, unifier);
            right.substitute(unifier).unify(unify, otherProd.right.substitute(unifier),unifier);
        } else throw new UnificationException("Cannot unify " + this + " and " + other + ".");
    }
}
