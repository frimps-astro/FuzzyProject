package relterm;

import exceptions.TypingException;
import exceptions.UnificationException;
import main.Basis;
import main.VariableGenerator;
import relations.Relation;
import sets.SetObject;
import typeterm.RelationType;
import typeterm.Typeterm;

import java.util.List;
import java.util.Map;

public class Converse extends Relterm {
    private final Relterm leaf;
    private static final int precedence = 50;

    public Converse(Relterm leaf) {
        this.leaf = leaf;
    }

    @Override
    protected void type(VariableGenerator gen, Map<String, RelationType> env, List<RelationType> cons) throws TypingException {
        leaf.type(gen, env, cons);
    }

    @Override
    public Map<String, RelationType> getTypedVars() {
        return leaf.getTypedVars();
    }

    @Override
    public RelationType getType() {
        return new RelationType(leaf.getType().target, leaf.getType().source);
    }

    @Override
    public String toStringPrec(int prec) {
        return leaf.toStringPrec(precedence) + "\u02D8";
    }

    @Override
    public Relation execute(Map<String, Relation> rels, Map<String, SetObject> sets, Basis basis) {
        return leaf.execute(rels, sets, basis).converse();
    }
}
