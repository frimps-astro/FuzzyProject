package ui;

import relations.Relation;
import storage.BasisStorage;
import storage.HeytingAlgebraStorage;
import storage.RelationStorage;
import storage.SetObjectStorage;
import ui.utils.UIComponents;
import ui.utils.UIDialogs;
import ui.utils.UIMethods;

import javax.swing.*;
import java.awt.*;

import static ui.utils.UIConstants.*;

public class ProjectUI extends JFrame{

    JSplitPane jSplitPane = new JSplitPane();

    public ProjectUI(){
        setLocation(SCREENSIZE.width/5, SCREENSIZE.height/4);
        setTitle("PROJECT: "+PROJECTNAME.toUpperCase());
        jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        jSplitPane.setResizeWeight(0.6);

        jSplitPane.setLeftComponent(UIComponents.getInstance().projectUILeftPane());
        jSplitPane.setRightComponent(UIComponents.getInstance().projectUIRightPane(this));

        add(jSplitPane);
        setVisible(true);
        setSize(1000, 600);
        addWindowListener(UIMethods.getInstance().windowEvent());
    }
}
