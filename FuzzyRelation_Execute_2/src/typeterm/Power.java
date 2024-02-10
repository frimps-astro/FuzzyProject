package typeterm;

import exceptions.UnificationException;
import main.Basis;
import sets.PowerSetObject;
import sets.SetObject;

import java.util.Map;
import java.util.Set;

public class Power extends Typeterm {
    private final Typeterm body;

    public Power(Typeterm body){
        this.body = body;
    }

    @Override
    public String toStringPrec(int prec) {
        return "P(" + body.toStringPrec(0) + ")";
    }

    @Override
    public Set<String> variables() {
        return body.variables();
    }

    @Override
    public Typeterm substitute(Map<String, Typeterm> subst) {
        return new Power(body.substitute(subst));
    }

    public Typeterm getBody() {
        return body;
    }

    @Override
    public SetObject execute(Map<String, SetObject> params, Basis basis) {
        return new PowerSetObject(body.execute(params, basis), basis);
    }

    @Override
    public void unify(boolean unify, Typeterm other, Map<String, Typeterm> unifier) throws UnificationException {
        if (unify && other instanceof TypeVariable){
            other.unify(true, this, unifier);
        } else if (other instanceof Power otherPower){
            body.unify(unify, otherPower.body, unifier);
        } else throw new UnificationException("Cannot unify " + this + " and " + other + ".");
    }
}
