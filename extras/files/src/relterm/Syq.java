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

public class Syq extends Relterm {
    private final Relterm left;
    private final Relterm right;

    public Syq(Relterm left, Relterm right) {
        this.left = left;
        this.right = right;
    }

    @Override
    protected void type(VariableGenerator gen, Map<String, RelationType> env, List<RelationType> cons) throws TypingException {
        left.type(gen, env, cons);
        right.type(gen, env, cons);
        RelationType leftType = left.getType();
        RelationType rightType = right.getType();

        try {
            Map<String, Typeterm> subst = leftType.source.matchTo(rightType.source);
            env.forEach((var,type) -> type.substitute(subst));
            cons.forEach(type -> type.substitute(subst));
        } catch (UnificationException e) {
            throw new TypingException(e.getMessage());
        }
    }

    @Override
    public Map<String, RelationType> getTypedVars() {
        Map<String, RelationType> typedVars = left.getTypedVars();
        typedVars.putAll(right.getTypedVars());

        return typedVars;
    }

    @Override
    public RelationType getType() {
        return new RelationType(left.getType().target,right.getType().target);
    }

    @Override
    public String toStringPrec(int prec) {
        return "syQ(%s,%s)".formatted(left.toStringPrec(0), right.toStringPrec(0));
    }

    @Override
    public Relation execute(Map<String, Relation> rels, Map<String, SetObject> params, Basis basis) {
        return left.execute(rels, params, basis).syq(right.execute(rels, params, basis));
    }

    @Override
    public void substituteInType(Map<String, Typeterm> subst) {
        left.substituteInType(subst);
        right.substituteInType(subst);
    }
}
