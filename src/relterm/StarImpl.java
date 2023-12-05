package relterm;

import exceptions.TypingException;
import exceptions.UnificationException;
import main.VariableGenerator;
import typeterm.RelationType;
import typeterm.Typeterm;

import java.util.List;
import java.util.Map;

public class StarImpl extends Relterm {
    private final Relterm left;
    private final Relterm right;
    private static final int precedence = 10;

    public StarImpl(Relterm left, Relterm right) {
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
            Map<String, Typeterm> subst2 = leftType.target.substitute(subst).matchTo(rightType.target.substitute(subst));
            subst.forEach((var,t) -> t.substitute(subst2));
            subst.putAll(subst2);
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
        return new RelationType(left.getType().source,left.getType().target);
    }

    @Override
    public String toStringPrec(int prec) {
        String result = left.toStringPrec(precedence) + "\u2192*" + right.toStringPrec(precedence);
        if (prec > precedence) result = "(" + result + ")";
        return result;
    }
}
