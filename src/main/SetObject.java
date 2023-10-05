package main;

import xmlutils.XMLObject;

public abstract class SetObject implements XMLObject {
    
    protected String name;
    protected String[] elementNames;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public int getNumElements() {
        return elementNames.length;
    }

    public String[] getElementNames() {
        return elementNames;
    }

    public String getElementNames(Integer integer) {
        return elementNames[integer];
    }
}
