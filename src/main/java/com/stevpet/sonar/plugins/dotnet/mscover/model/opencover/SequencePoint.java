package com.stevpet.sonar.plugins.dotnet.mscover.model.opencover;

public interface SequencePoint {

    /**
     * @return the visitedCount
     */
    public abstract int getVisitedCount();

    /**
     * @param string the visitedCount to set
     */
    public abstract void setVisitedCount(String string);

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