package typeterm;

import java.util.Map;

public class RelationType {
    
    public Typeterm source;
    public Typeterm target;
    
    public RelationType(Typeterm source, Typeterm target) {
        this.source = source;
        this.target = target;
    }
    public void substitute(Map<String, Typeterm> subst){
        source = source.substitute(subst);
        target = target.substitute(subst);
    }
    @Override
    public String toString() {
        return source + "\u2192" + target;
    }
}
