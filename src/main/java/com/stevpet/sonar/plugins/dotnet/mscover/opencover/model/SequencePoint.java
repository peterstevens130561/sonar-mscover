package com.stevpet.sonar.plugins.dotnet.mscover.opencover.model;

public interface SequencePoint {


    /**
     * @return the line
     */
    public abstract int getLine();

    /**
     * @return the offset
     */
    public abstract int getOffset();

    /**
     * @param string the offset to set
     */
    public abstract void setOffset(String string);

    public abstract void setStartLine(String string);

}