package relterm;

import exceptions.TypingException;
import main.Basis;
import main.VariableGenerator;
import relations.Relation;
import sets.SetObject;
import typeterm.RelationType;
import typeterm.Typeterm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Relterm {
    public abstract RelationType getType();

    protected abstract void type(VariableGenerator gen, Map<String, RelationType> env, List<RelationType> cons) throws TypingException;

    public void type() throws TypingException {
        VariableGenerator gen = new VariableGenerator();
        Map<String, RelationType> env = new HashMap<>();
        List<RelationType> cons = new ArrayList<>();
        type(gen, env, cons);
    }

    public abstract Map<String,RelationType> getTypedVars();

    public abstract String toStringPrec(int prec);

    public abstract Relation execute(Map<String, Relation> rels, Map<String, SetObject> params, Basis basis);

    public abstract void substituteInType(Map<String, Typeterm> subst);

    @Override
    public String toString(){
        return toStringPrec(0);
    }
}
