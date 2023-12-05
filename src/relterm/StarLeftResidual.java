package relterm;

import exceptions.TypingException;
import exceptions.UnificationException;
import main.VariableGenerator;
import typeterm.RelationType;
import typeterm.Typeterm;

import java.util.List;
import java.util.Map;

public class StarLeftResidual extends Relterm {
    private final Relterm left;
    private final Relterm right;
    private static final int precedence = 30;

    public StarLeftResidual(Relterm left, Relterm right) {
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
        String result = left.toStringPrec(precedence) + "\\*" + right.toStringPrec(precedence);
        if (prec > precedence) result = "(" + result + ")";
        return result;
    }
}
