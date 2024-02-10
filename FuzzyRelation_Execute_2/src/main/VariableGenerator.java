package main;

import typeterm.TypeVariable;

public class VariableGenerator {
    private int count = 0;

    public String newVarName() {
        String result = Character.toString((char) count % 26 + 97);
        if (count / 26 != 0) result += count / 26 - 1;
        count++;
        return result;
    }
}
