package relterm;

import exceptions.TypingException;
import main.VariableGenerator;
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

    @Override
    public String toString(){
        return toStringPrec(0);
    }
}
