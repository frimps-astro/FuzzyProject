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
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + left.hashCode();
        hash = 83 * hash + right.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof Product other) {
            result = left.equals(other.left) && right.equals(other.right);
        }
        return result;
    }

    @Override
    public void unify(Typeterm other, Map<String, Typeterm> unifier) throws UnificationException {
        if (other instanceof TypeVariable){
            other.unify(this, unifier);
        } else if (other instanceof Product otherProd){
            left.unify(otherProd.left, unifier);
            right.substitute(unifier).unify(otherProd.right.substitute(unifier),unifier);
        } else throw new UnificationException("Cannot unify " + this + " and " + other + ".");
    }
    
    @Override
    public void matchTo(Typeterm other, Map<String, Typeterm> unifier) throws UnificationException {
        if (other instanceof Product otherProduct) {
            left.matchTo(otherProduct.left, unifier);
            right.matchTo(otherProduct.right,unifier);
        } else throw new UnificationException("Cannot match " + this + " and " + other + ".");
    }
}
