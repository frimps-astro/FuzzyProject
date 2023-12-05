package relterm;

import exceptions.TypingException;
import main.VariableGenerator;
import typeterm.RelationType;

import java.util.List;
import java.util.Map;

public class Up extends Relterm {
    private final Relterm leaf;
    private static final int precedence = 50;

    public Up(Relterm leaf) {
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
        return leaf.getType();
    }

    @Override
    public String toStringPrec(int prec) {
        return "%sꜛ".formatted(leaf.toStringPrec(precedence));
    }
}
