package relterm;

import exceptions.TypingException;
import exceptions.UnificationException;
import main.Basis;
import main.VariableGenerator;
import relations.Relation;
import sets.SetObject;
import sets.Unit;
import typeterm.RelationType;
import typeterm.Typeterm;
import typeterm.UnitTerm;

import java.util.*;
import java.util.stream.Stream;

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
            Map<String, Typeterm> substS = tType.source.matchTo(resultType.source);
            Map<String, Typeterm> substT = tType.target.matchTo(resultType.target);
            vars.forEach((var,type) -> type.substitute(substS));
            vars.forEach((var,type) -> type.substitute(substT));
            cons.forEach(type -> type.substitute(substS));
            cons.forEach(type -> type.substitute(substT));

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

    public Relation execute(Map<String, Relation> rels, Map<String, SetObject> sets, Basis basis) {
        rels.forEach((key, relation) -> {
            if (vars.values().stream().noneMatch(relationType -> relationType.source.equals(relation.getSourceTerm())
            && relationType.target.equals(relation.getTargetTerm()))){
                try {
                    throw new TypingException("There was a typing exception");
                } catch (TypingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return t.execute(rels, sets, basis);
    }
}
