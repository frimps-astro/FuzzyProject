package typeterm;

import exceptions.UnificationException;
import main.Basis;
import sets.SetObject;
import sets.Unit;

import java.util.*;

public class UnitTerm extends Typeterm {
    private final String name = "\u0835";

    @Override
    public String toStringPrec(int prec) {
        return name;
    }

    @Override
    public Set<String> variables() {
        return new HashSet<>();
    }

    @Override
    public Typeterm substitute(Map<String, Typeterm> subst) {
        return this;
    }

    @Override
    public SetObject execute(Map<String, SetObject> params, Basis basis) {
        return new Unit();
    }

    @Override
    protected void unify(boolean unify, Typeterm other, Map<String, Typeterm> unifier) throws UnificationException {
        if (!(other instanceof UnitTerm)) {
            if (other.variables().contains(name))
                throw new UnificationException("Unification leads to an infinite term.");
            else {
                unifier.forEach((var,t) -> t.substitute(name, other));
                unifier.put(name,other);
            }
        }
    }
}
