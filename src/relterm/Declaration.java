package relterm;

import exceptions.TypingException;
import exceptions.UnificationException;
import main.VariableGenerator;
import sets.Unit;
import typeterm.RelationType;
import typeterm.Typeterm;
import typeterm.UnitTerm;

import java.util.*;

public class Declaration {
    String name;
    RelationType resultType;
    Map<String, RelationType> vars;
    Relterm t;

    public RelationType type() throws TypingException {
        VariableGenerator gen = new VariableGenerator();
        List<RelationType> cons = new ArrayList<>();
        t.type(gen, vars, cons);
        RelationType tType = t.getType();

        try {
            Map<String, Typeterm> subst = tType.source.matchTo(resultType.source);
            vars.forEach((var,type) -> type.substitute(subst));
            cons.forEach(type -> type.substitute(subst));

            Set<String> s1 = new HashSet<>();
            Set<String> t1 = new HashSet<>();
            Set<String> s2 = new HashSet<>();
            Set<String> t2 = new HashSet<>();

            vars.forEach((var, type)->{
                s1.addAll(type.source.variables());
                t1.addAll(type.target.variables());
            });

            //Add ResultType variables of Relterm t
            s1.addAll(tType.source.variables());
            t1.addAll(tType.target.variables());

            cons.forEach(type->{
                s2.addAll(type.source.variables());
                t2.addAll(type.target.variables());
            });

            s2.forEach(var -> {
                if (!s1.contains(var)){
                    cons.forEach(type -> {
                        type.source.variables().remove(var);
                        type.source.variables().add(new UnitTerm().toStringPrec(0));
                    });
                }
            });

            t2.forEach(var -> {
                if (!t1.contains(var)){
                    cons.forEach(type -> {
                        type.target.variables().remove(var);
                        type.target.variables().add(new UnitTerm().toStringPrec(0));
                    });
                }
            });


        } catch (UnificationException e) {
            throw new RuntimeException(e);
        }

        return t.getType();
    }
}
