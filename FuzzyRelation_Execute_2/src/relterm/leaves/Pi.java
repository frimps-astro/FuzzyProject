package relterm.leaves;

import exceptions.TypingException;
import main.Basis;
import main.VariableGenerator;
import relations.Relation;
import relterm.Relterm;
import sets.SetObject;
import typeterm.RelationType;
import typeterm.Sum;
import typeterm.TypeVariable;
import typeterm.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pi extends Relterm {
    private RelationType type;

    @Override
    public RelationType getType() {
        return type;
    }

    @Override
    protected void type(VariableGenerator gen, Map<String, RelationType> env, List<RelationType> cons) throws TypingException {
        TypeVariable source = new TypeVariable(gen.newVarName());
        TypeVariable target = new TypeVariable(gen.newVarName());

        type = new RelationType(new Product(source, target), source);
        cons.add(type);
    }

    @Override
    public Map<String, RelationType> getTypedVars() {
        return new HashMap<>();
    }

    @Override
    public String toStringPrec(int prec) {
        return "\u03C0";
    }

    @Override
    public Relation execute(Map<String, Relation> rels, Map<String, SetObject> sets, Basis basis) {
        Product productSetObjectTerm = new Product(type.source, type.target);
        return Relation.pi(productSetObjectTerm, sets, basis);
    }
}
