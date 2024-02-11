package ui.utils;

import storage.SetObjectStorage;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import static ui.utils.UIConstants.INTERFACES;


public class UIMethods {
    public static UIMethods UIMETHODS = null;

    public static UIMethods getInstance() {
        if (UIMETHODS == null) UIMETHODS = new UIMethods();
        return UIMETHODS;
    }

    public void createSetObject(int selectedIndex, String component1, String component2) {
        if (component1 != null && component2 != null){
            switch (selectedIndex){
                case 1 -> SetObjectStorage.getInstance()
                        .createSumSetObject(component1+"+"+component2, component1, component2);
                case 2 -> SetObjectStorage.getInstance()
                        .createProductSetObject(component1+"x"+component2, component1, component2);
                case 3 -> SetObjectStorage.getInstance()
                        .createPowerSetObject(component1+"^"+component2, component2, component1);
            }
        }
    }

    public String[] readDataList(String path){
        File file = new File(path);
        String[] projects = file.list((current, name) -> new File(current, name).isDirectory());

        return projects;
    }

    public WindowAdapter windowEvent() {
       return new WindowAdapter() {
           @Override
           public void windowOpened(WindowEvent e) {
               var window = e.getWindow().getClass();
               System.out.println("You opened: "+ window.getSimpleName());
               INTERFACES.put(window.getSimpleName(), (JFrame) e.getComponent());
               super.windowOpened(e);
           }

            @Override
            public void windowClosing(WindowEvent e) {
                String window = e.getWindow().getClass().getSimpleName();
                System.out.println("You closed: "+window);
                INTERFACES.remove(window);
                super.windowClosed(e);
            }
        };
    }
}
