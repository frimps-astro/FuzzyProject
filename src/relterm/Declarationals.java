package relterm;

import xmlutils.XMLObject;

public abstract class Declarationals implements XMLObject{
    protected String filename;
    protected String declaration;

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }


    public String getName() {
        return filename;
    }

    public void setName(String filename) {
        this.filename = filename;
    }
}
