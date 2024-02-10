package ui.utils;

import storage.SetObjectStorage;

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
}
