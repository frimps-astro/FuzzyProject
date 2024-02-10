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

public class RightResidual extends Relterm {
    private final Relterm left;
    private final Relterm right;
    private static final int precedence = 30;

    public RightResidual(Relterm left, Relterm right) {
        this.left = left;
        this.right = right;
    }

    @Override
    protected void type(VariableGenerator gen, Map<String, RelationType> env, List<RelationType> cons) throws TypingException {
        left.type(gen, env, cons);
        right.type(gen,env, cons);
        RelationType leftType = left.getType();
        RelationType rightType = right.getType();

        try {
            Map<String, Typeterm> subst = leftType.target.matchTo(rightType.target);
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
        return new RelationType(left.getType().source,right.getType().source);
    }

    @Override
    public String toStringPrec(int prec) {
        String result = left.toStringPrec(precedence) + "/" + right.toStringPrec(precedence);
        if (prec > precedence) result = "(" + result + ")";
        return result;
    }

    @Override
    public Relation execute(Map<String, Relation> rels, Map<String, SetObject> sets, Basis basis) {
        return left.execute(rels, sets, basis).rightResidual(right.execute(rels, sets, basis));
    }
}
