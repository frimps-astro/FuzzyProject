package relterm;

import exceptions.TypingException;
import main.Basis;
import main.VariableGenerator;
import relations.Relation;
import sets.SetObject;
import typeterm.RelationType;
import typeterm.TypeVariable;

import java.util.*;

public class RelVariable extends Relterm {
    private final String name;
    private RelationType type;

    @Override
    public RelationType getType() {
        return type;
    }

    public RelVariable(String name) {
        this.name = name;
    }

    @Override
    protected void type(VariableGenerator gen, Map<String, RelationType> env, List<RelationType> cons) throws TypingException {
        if (env.containsKey(name)) {
            type = env.get(name);
        }
        else {
            TypeVariable source = new TypeVariable(gen.newVarName());
            TypeVariable target = new TypeVariable(gen.newVarName());

            type = new RelationType(source, target);
            env.put(name, type);
        }
    }

    @Override
    public Map<String, RelationType> getTypedVars() {
        Map<String, RelationType> typedVars = new HashMap<>();
        typedVars.put(name, type);
        return typedVars;
    }

    @Override
    public String toStringPrec(int prec) {
        return name;
    }

    @Override
    public Relation execute(Map<String, Relation> rels, Map<String, SetObject> sets, Basis basis) {
       return rels.get(name);
    }
}
