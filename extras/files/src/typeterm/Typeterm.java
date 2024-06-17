package typeterm;

import exceptions.UnificationException;
import main.Basis;
import sets.SetObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Typeterm {
    public abstract String toStringPrec(int prec);
    
    public abstract Set<String> variables();

    public abstract Typeterm substitute(Map<String,Typeterm> subst);

    public Typeterm substitute(String var, Typeterm t) {
        Map<String,Typeterm> subst = new HashMap<>();
        subst.put(var,t);
        return substitute(subst);
    }
    
    public abstract SetObject execute(Map<String,SetObject> params, Basis basis);
    
    protected abstract void unify(Typeterm other, Map<String, Typeterm> unifier) throws UnificationException;

    protected Map<String,Typeterm> unify(Typeterm other) throws UnificationException {
        Map<String,Typeterm> unifier = new HashMap<>();
        unify(other,unifier);
        return unifier;
    }
    
    protected abstract void matchTo(Typeterm other, Map<String, Typeterm> unifier) throws UnificationException;

    public Map<String,Typeterm> matchTo(Typeterm other) throws UnificationException {
        Map<String,Typeterm> unifier = new HashMap<>();
        matchTo(other,unifier);
        return unifier;
    }

    @Override
    public String toString(){
        return toStringPrec(0);
    }

}
