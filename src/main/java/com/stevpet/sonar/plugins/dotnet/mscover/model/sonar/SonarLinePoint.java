package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

public class SonarLinePoint implements CoveragePoint, CoverageLine {
    private int line ;
    private int covered;
    
    public int getLine() {
        return line;
    }
    public void setLine(int line) {
        this.line = line;
    }
    public boolean isVisited() {
        return covered>0;
    }
    public void setCovered(boolean visited) {
        covered= visited?1:0;
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoveragePoint#getToCover()
     */
    public int getToCover() {
        return 1;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoveragePoint#getCovered()
     */
    public int getCovered() {
        return covered;
    }
    
   
}
