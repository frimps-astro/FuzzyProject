package ui;

import ui.utils.UIComponents;
import ui.utils.UIMethods;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static ui.utils.UIConstants.*;

public class ProjectUI extends JFrame{

    JSplitPane jSplitPane = new JSplitPane();

    public ProjectUI(){
        JMenuBar menuBar;
        JMenu menu;
        menuBar = new JMenuBar();
        menu = new JMenu("Project");
        menu.getAccessibleContext().setAccessibleDescription(
                "Load or Create a New Project");

        JMenuItem load = new JMenuItem("Load/New");
        load.addActionListener(e->{
            new UserInterface();
        });
        menu.add(load);
        menuBar.add(menu);


        setJMenuBar(menuBar);
        setLocation(SCREENSIZE.width/6, SCREENSIZE.height/5);
        setTitle(STR."PROJECT: \{PROJECTNAME.toUpperCase()}");
        jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        jSplitPane.setResizeWeight(0.6);

        jSplitPane.setLeftComponent(UIComponents.getInstance().projectUILeftPane());
        jSplitPane.setRightComponent(UIComponents.getInstance().projectUIRightPane(this));

        add(jSplitPane);
        setVisible(true);
        setSize(1090, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
