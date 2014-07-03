package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;


/**
 * Coverage info per file
 */
public class SonarFileCoverage {
    String absolutePath ;

    private CoverageLinePoints linePoints = new SonarLinePoints();
    private CoverageLinePoints branchPoints = new SonarBranchPoints();
    
    public void setAbsolutePath(String absolutePath) {  
        this.absolutePath=absolutePath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }
    
    public CoverageLinePoints getLinePoints() {
        return linePoints;
    }
    
    public CoverageLinePoints getBranchPoints() {
        return branchPoints;
    }

    public CoveragePoint addLinePoint(int line, boolean covered) {
        return getLinePoints().addPoint(line,covered);
    }

    public CoveragePoint getLastLinePoint() {
        return getLinePoints().getLast();
    }

    public void addBranchPoint(int line, boolean covered) {
        getBranchPoints().addPoint(line, covered);
    }

}
