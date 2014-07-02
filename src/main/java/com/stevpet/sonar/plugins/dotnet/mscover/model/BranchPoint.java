package com.stevpet.sonar.plugins.dotnet.mscover.model;

public class BranchPoint {
    private int visitedCount ;
    private int offset ;
    private int line ;
    private int path ;
    /**
     * @return the visitedCount
     */
    int getVisitedCount() {
        return visitedCount;
    }
    /**
     * @param visitedCount the visitedCount to set
     */
    void setVisitedCount(int visitedCount) {
        this.visitedCount = visitedCount;
    }
    /**
     * @return the offset
     */
    int getOffset() {
        return offset;
    }
    /**
     * @param offset the offset to set
     */
    void setOffset(int offset) {
        this.offset = offset;
    }
    /**
     * @return the line
     */
    int getLine() {
        return line;
    }
    /**
     * @param line the line to set
     */
    void setLine(int line) {
        this.line = line;
    }
    /**
     * @return the path
     */
    int getPath() {
        return path;
    }
    /**
     * @param path the path to set
     */
    void setPath(int path) {
        this.path = path;
    }

}
