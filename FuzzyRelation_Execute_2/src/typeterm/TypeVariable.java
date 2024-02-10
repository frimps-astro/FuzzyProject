package typeterm;

import exceptions.UnificationException;
import main.Basis;
import sets.SetObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TypeVariable extends Typeterm {
    private final String name;

    public TypeVariable(String name) {
        this.name = name;
    }

    @Override
    public String toStringPrec(int prec) {
        return name;
    }

    @Override
    public Set<String> variables() {
        Set<String> vars = new HashSet<>();
        vars.add(name);
        return vars;
    }

    @Override
    public Typeterm substitute(Map<String, Typeterm> subst) {
        return subst.getOrDefault(name, this);
    }

    @Override
    public SetObject execute(Map<String, SetObject> params, Basis basis) {
        return params.get(name);
    }

    @Override
    public void unify(boolean unify, Typeterm other, Map<String, Typeterm> unifier) throws UnificationException {
        if (!(other instanceof TypeVariable otherVar && otherVar.name.equals(name))) {
            if (other.variables().contains(name))
                throw new UnificationException("Unification leads to an infinite term.");
            else {
                unifier.forEach((var,t) -> t.substitute(name, other));
                unifier.put(name,other);
            }
        }
    }
}
