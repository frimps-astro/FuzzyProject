package typeterm;

import exceptions.UnificationException;
import main.Basis;
import sets.SetObject;
import sets.SumSetObject;

import java.util.Map;
import java.util.Set;

public class Sum extends Typeterm {
    private final Typeterm left;
    private final Typeterm right;
    private static final int precedence = 10;
    public Typeterm getLeft() {
        return left;
    }

    public Typeterm getRight() {
        return right;
    }
    public Sum(Typeterm left, Typeterm right){
        this.left = left;
        this.right = right;
    }

    @Override
    public String toStringPrec(int prec) {
        String result = left.toStringPrec(precedence) + "+" + right.toStringPrec(precedence);
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

        return new Sum(newLeft, newRight);
    }

    @Override
    public SetObject execute(Map<String, SetObject> params, Basis basis) {
        return new SumSetObject(left.execute(params, basis), right.execute(params, basis));
    }

    @Override
    public void unify(boolean unify, Typeterm other, Map<String, Typeterm> unifier) throws UnificationException {
        if (unify && other instanceof TypeVariable) {
            other.unify(true, this, unifier);
        } else if (other instanceof Sum otherSum) {
            left.unify(unify, otherSum.left, unifier);
            right.substitute(unifier).unify(unify, otherSum.right.substitute(unifier),unifier);
        } else throw new UnificationException("Cannot unify " + this + " and " + other + ".");
    }
}
