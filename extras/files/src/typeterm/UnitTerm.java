package typeterm;

import exceptions.UnificationException;
import main.Basis;
import sets.SetObject;
import sets.Unit;

import java.util.*;

public class UnitTerm extends Typeterm {

    @Override
    public String toStringPrec(int prec) {
        return "\u0835";
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
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UnitTerm;
    }
    
    @Override
    protected void unify(Typeterm other, Map<String, Typeterm> unifier) throws UnificationException {
        if (!(other instanceof UnitTerm)) {
            throw new UnificationException("Unit cannot be unified.");
        }
    }
    
    @Override
    protected void matchTo(Typeterm other, Map<String, Typeterm> unifier) throws UnificationException {
        if (!(other instanceof UnitTerm)) {
            throw new UnificationException("Unit cannot be matched.");
        }
    }
}
