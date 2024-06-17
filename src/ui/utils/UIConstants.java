package ui.utils;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class UIConstants {
    public static Dimension SCREENSIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static int BUTTONHEIGHT = 35;
    public static int BUTTONWIDTH = 150;
    public static int RELBUTTONWIDTH = 80;
    public static String PROJECTNAME = null;

    //COLORS
    public static Color BGCOLOR = Color.WHITE;
    public static Color DANGERBUTTONCOLOR = new Color(255, 0, 7);
    public static Color OKBUTTONCOLOR = new Color(0, 0, 238);
    public static Color LISTBGCOLOR = new Color(180, 255, 255);
    public static Color EXPANDBUTTONCOLOR = new Color(255, 0, 186);


    //PROJECT DETAILS
    public static String STUDENT = "Clement Frimpong Osei";
    public static String SUPERVISOR = "Michael Winter";
    public static String SCHOOL = "Brock University";

    public static HashMap<String, JFrame> INTERFACES = new HashMap<>();
    public static HashMap<String, Boolean> DATAUPDATED = new HashMap<>();
    public static HashMap<String, String> DATAITEMS = new HashMap<>();


}
