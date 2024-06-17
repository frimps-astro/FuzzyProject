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
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + body.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof Power other) {
            result = body.equals(other.body);
        }
        return result;
    }

    @Override
    public SetObject execute(Map<String, SetObject> params, Basis basis) {
        return new PowerSetObject(body.execute(params, basis), basis);
    }

    @Override
    public void unify(Typeterm other, Map<String, Typeterm> unifier) throws UnificationException {
        if (other instanceof TypeVariable){
            other.unify(this, unifier);
        } else if (other instanceof Power otherPower){
            body.unify(otherPower.body, unifier);
        } else throw new UnificationException("Cannot unify " + this + " and " + other + ".");
    }
    
    @Override
    public void matchTo(Typeterm other, Map<String, Typeterm> unifier) throws UnificationException {
        if (other instanceof Power otherPower) {
            body.matchTo(otherPower.body, unifier);
        } else throw new UnificationException("Cannot match " + this + " and " + other + ".");
    }
}
