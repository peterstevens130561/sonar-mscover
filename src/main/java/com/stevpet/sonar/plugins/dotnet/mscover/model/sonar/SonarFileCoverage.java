package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;


/**
 * Coverage info per file
 */
public class SonarFileCoverage {
    String absolutePath ;

    private SonarLinePoints linePoints = new SonarLinePoints();
    private SonarBranchPoints branchPoints = new SonarBranchPoints();
    
    public void setAbsolutePath(String absolutePath) {  
        this.absolutePath=absolutePath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }
    
    public SonarLinePoints getLinePoints() {
        return linePoints;
    }
    
    public SonarBranchPoints getBranchPoints() {
        return branchPoints;
    }

    public SonarLinePoint addLinePoint(int line, boolean covered) {
        return getLinePoints().addPoint(line,covered);
    }

    public SonarLinePoint getLastLinePoint() {
        return getLinePoints().getLast();
    }

    public void addBranchPoint(int line, boolean covered) {
        getBranchPoints().addPoint(line, covered);
    }

}
