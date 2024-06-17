package classutils;

import java.util.Arrays;
import java.util.List;

public class StringArrayXML {
    private static StringArrayXML STRINGARRAY = null;

    public static StringArrayXML getInstance() {
        if (STRINGARRAY == null) STRINGARRAY = new StringArrayXML();
        return STRINGARRAY;
    }

    public StringBuilder arrayToString(int[][] data) {
        StringBuilder dataStr = new StringBuilder();
        List<int[]> elementLists = Arrays.stream(data).toList();

        for (int j = 0; j < data.length; j++) {
            dataStr.append(Arrays.toString(elementLists.get(j))).append(",");
            dataStr.append("\n\t\t");
        }

        return dataStr;
    }
}
