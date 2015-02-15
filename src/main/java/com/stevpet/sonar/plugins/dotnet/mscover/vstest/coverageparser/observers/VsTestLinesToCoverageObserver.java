package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;



import com.stevpet.sonar.plugins.dotnet.mscover.model.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SolutionLineCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;

public class VsTestLinesToCoverageObserver extends VsTestCoverageObserver {
    private SolutionLineCoverage coverageRegistry;
    private CoveragePoint coveragePoint;
    private boolean covered;

    public VsTestLinesToCoverageObserver() {
        setPattern("Module/NamespaceTable/Class/Method/Lines/(LnStart|LnEnd|Coverage|SourceFileID)");
    }

    public void setRegistry(SolutionLineCoverage coverageRegistry) {
        this.coverageRegistry=coverageRegistry;     
    }
    
    @Override
    public void setVsTestRegistry(VsTestCoverageRegistry vsTestRegistry) {
        this.coverageRegistry=vsTestRegistry.getCoverageRegistry();      
    }
    
    @ElementMatcher(elementName="LnStart")
    public void lnStartMatcher(String lnStart) {
        coveragePoint = new CoveragePoint();
        coveragePoint.setStartLine(Integer.parseInt(lnStart));
    }
    
    @ElementMatcher(elementName="LnEnd")
    public void lnEndMatcher(String lnEnd) {
        coveragePoint.setEndLine(Integer.parseInt(lnEnd));
    }
    
    @ElementMatcher(elementName="Coverage" )
    public void coverageMatcher(String coverage) {
        covered = "0".equals(coverage);
        int visits= covered?1:0;
        coveragePoint.setCountVisits(visits);
    }
    
    @ElementMatcher(elementName="SourceFileID") 
    public void sourceFileIdMatcher(String value) {
        int sourceFileID=Integer.parseInt(value);
        if(covered) {
            coverageRegistry.addCoveredFileLine(sourceFileID,coveragePoint);
        } else {
            coverageRegistry.addUnCoveredFileLine(sourceFileID,coveragePoint);
        }
    }


    
}
