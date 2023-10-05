package main;

import xmlutils.XMLObject;

public abstract class Relationals implements XMLObject{
        protected String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
}
