package relterm;

import exceptions.TypingException;
import exceptions.UnificationException;
import main.Basis;
import main.VariableGenerator;
import relations.Relation;
import relterm.Relterm;
import sets.SetObject;
import typeterm.RelationType;
import typeterm.Typeterm;
import typeterm.UnitTerm;
import java.util.*;

public class Declaration extends Declarationals {


    String name;
    RelationType resultType;
    LinkedHashMap<String, RelationType> vars;
    Relterm t;

    public Declaration() {
    }

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

            // on it
        } catch (UnificationException e) {
            throw new RuntimeException(e);
        }

        return t.getType();
    }

    public Relation execute(List<Relation> rels, Basis basis) throws UnificationException {
        Map<String, SetObject> newParams = new HashMap<>();
        Map<String, Relation> relsMap = new HashMap<>();

        int index = 0;
        for(Map.Entry<String,RelationType> varEntry : vars.sequencedEntrySet()) {
            Relation r = rels.get(index);
            RelationType rt = varEntry.getValue();
            relsMap.put(varEntry.getKey(), r);
            Map<String, Typeterm> res1 = rt.source.matchTo(r.getSourceTerm());
            Map<String, Typeterm> res2 = rt.target.matchTo(r.getTargetTerm());  
            for(String var : res2.keySet()) {
                Typeterm other = res2.get(var);
                if (res1.containsKey(var)) {
                    if (!res1.get(var).equals(other)) {
                        throw new UnificationException("Multiple terms for same variable.");
                    }
                } else {
                    res1.put(var,other);
                }
            }
            t.substituteInType(res1);
            newParams.putAll(r.getParams());
        }     
        return t.execute(relsMap, newParams, basis);
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

    public LinkedHashMap<String, RelationType> getVars() {
        return vars;
    }

    @Override
    public String toXMLString() {
        StringBuilder declarationBuilder = new StringBuilder();
        declarationBuilder.append("<Declaration>\n");

        declarationBuilder.append("\t"+this.declaration);

        declarationBuilder.append("</Declaration>");

        return declarationBuilder.toString();
    }
}
