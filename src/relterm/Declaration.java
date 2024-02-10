package relterm;

import exceptions.TypingException;
import exceptions.UnificationException;
import main.Basis;
import main.VariableGenerator;
import relations.Relation;
import sets.SetObject;
import sets.SumSetObject;
import sets.Unit;
import typeterm.RelationType;
import typeterm.Typeterm;
import typeterm.UnitTerm;

import java.util.*;
import java.util.stream.Stream;

public class Declaration {
    String name;

    public Declaration(String name, RelationType resultType, LinkedHashMap<String, RelationType> vars, Relterm t) {
        this.name = name;
        this.resultType = resultType;
        this.vars = vars;
        this.t = t;
    }

    public Declaration() {
    }

    RelationType resultType;
    LinkedHashMap<String, RelationType> vars;
    Relterm t;

    public RelationType type() throws TypingException {
        VariableGenerator gen = new VariableGenerator();
        List<RelationType> cons = new ArrayList<>();
        t.type(gen, vars, cons);
        RelationType tType = t.getType();

        try {
            Map<String, Typeterm> substS = tType.source.matchTo(resultType.source);
            Map<String, Typeterm> substT = tType.target.substitute(substS).matchTo(resultType.target.substitute(substS));
            substS.putAll(substT);
            vars.forEach((var,type) -> {
                type.substitute(substS);
            });
            cons.forEach(type -> {
                type.substitute(substS);
            });

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

    public Relation execute(LinkedHashMap<String, Relation> rels, Map<String, SetObject> sets, Basis basis) {
        LinkedHashMap<String, SetObject> newParams = new LinkedHashMap<>();
        var relationsList = rels.values().stream().toList();
        var varsList = vars.values().stream().toList();

        for (int i = 0; i < relationsList.size(); i++) {
            Relation r = relationsList.get(i);
            RelationType rt = varsList.get(i);

            try {
                Map<String, Typeterm> res1 = rt.source.matchTo(r.getSourceTerm());
                Map<String, Typeterm> res2 = rt.target.substitute(res1).matchTo(r.getTargetTerm().substitute(res1));
                res1.putAll(res2);
                t.substituteInType(res1);

                newParams.putAll(r.getParams());

            } catch (UnificationException e) {
                throw new RuntimeException(e);
            }
        }

        return t.execute(rels, newParams, basis);
    }

    @Override
    public String toString() {
        return "Declaration{" +
                "name='" + name + '\'' +
                ", resultType=" + resultType +
                ", vars=" + vars +
                ", t=" + t +
                '}';
    }
}
