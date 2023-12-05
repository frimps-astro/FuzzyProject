package relterm;

import exceptions.TypingException;
import exceptions.UnificationException;
import main.VariableGenerator;
import typeterm.RelationType;
import typeterm.Typeterm;

import java.util.List;
import java.util.Map;

public class Complement extends Relterm {
    private final Relterm leaf;
    private static final int precedence = 50;

    public Complement(Relterm leaf) {
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
        String result = leaf.toStringPrec(precedence) + "'";
        return result;
    }
}
