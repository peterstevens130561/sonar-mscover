package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

public class SonarBranchPoint implements CoverageLinePoint{
    private int line;
    private int branchesVisited ;
    private int branchesToVisit ;
    /**
     * @return the line
     */
    public int getLine() {
        return line;
    }
    /**
     * @param line the line to set
     */
    public void setLine(int line) {
        this.line = line;
    }
    /**
     * @return the branchesVisited
     */
    public int getCovered() {
        return branchesVisited;
    }

    /**
     * @return the branchesToVisit
     */
    public int getToCover() {
        return branchesToVisit;
    }

    public void incrementBranchesToVisit() {
        this.branchesToVisit +=1;
    }
    public void incrementVisitedBranches() {
        this.branchesVisited +=1;
    }
}
