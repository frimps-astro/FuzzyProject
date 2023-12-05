package relterm.leaves;

import exceptions.TypingException;
import main.VariableGenerator;
import relterm.Relterm;
import typeterm.RelationType;
import typeterm.TypeVariable;
import typeterm.Power;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Epsi extends Relterm {
    private RelationType type;

    @Override
    public RelationType getType() {
        return type;
    }

    @Override
    protected void type(VariableGenerator gen, Map<String, RelationType> env, List<RelationType> cons) throws TypingException {
        TypeVariable source = new TypeVariable(gen.newVarName());
        TypeVariable target = new TypeVariable(gen.newVarName());

        type = new RelationType(source, new Power(target));
        cons.add(type);
    }

    @Override
    public Map<String, RelationType> getTypedVars() {
        return new HashMap<>();
    }

    @Override
    public String toStringPrec(int prec) {
        return "\u03B5";
    }
}
