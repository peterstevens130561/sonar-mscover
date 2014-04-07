package com.stevpet.sonar.plugins.dotnet.mscover.model;

public abstract class Model {
    /**
     * Invoke for each field starting at the first element up to, and including the last Element
     * any unwanted element should be ignored
     * @param name of current element
     * @param text in current element
     */
    public abstract void setField(String name, String text);
    
    /**
     * get the local name of the first element
     */
    public abstract String getFirstElement();
    
    /**
     * get the local name of the last element
     * @return
     */
    public abstract String getLastElement();
}
