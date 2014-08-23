package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.model.OpenCoverSequencePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.model.SequencePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.BaseParserObserver;

public class OpenCoverSequencePointsObserver extends OpenCoverObserver {

        private SonarCoverage registry ;
        private SequencePoint sequencePoint;
        private String fileUid ;
        private SonarFileCoverage coveredFile;
        private boolean lineVisited;
        private BranchOffsetToLineMapper offsetToLineMapper = new BranchOffsetToLineMapper();
        private boolean branchVisited;
        public OpenCoverSequencePointsObserver() {
            setPattern("Modules/Module/Classes/Class/Methods/Method/FileRef" +
                     "|Modules/Module/Classes/Class/Methods/Method/SequencePoints/SequencePoint" +
                     "|Modules/Module/Classes/Class/Methods/Method/BranchPoints/BranchPoint" +
                     ""
                     );
        }

        public void setRegistry(SonarCoverage registry) {
            this.registry = registry;
        }
        
        @AttributeMatcher(attributeName="uid",elementName="FileRef")
        public void fileRefMatcher(String attributeValue) {
           fileUid=attributeValue;
           coveredFile=registry.getCoveredFile(fileUid);
           offsetToLineMapper.start();
        }
        
        @AttributeMatcher(attributeName = "vc", elementName = "SequencePoint")
        public void visitedCountMatcher(String attributeValue) {
            sequencePoint=new OpenCoverSequencePoint();
           lineVisited = !"0".equals(attributeValue);

        }
        
        @AttributeMatcher(attributeName="offset",elementName="SequencePoint")
        public void offsetMatcher(String attributeValue) {
            sequencePoint.setOffset(attributeValue);
            offsetToLineMapper.addSequencePoint(sequencePoint);
        }
        
        @AttributeMatcher(attributeName="sl",elementName="SequencePoint")
        public void startLineMatcher(String attributeValue) {

            sequencePoint.setStartLine(attributeValue);
            offsetToLineMapper.addSequencePoint(sequencePoint);
  
            int line=Integer.parseInt(attributeValue);
            coveredFile.addLinePoint(line, lineVisited);
        }
        
        @AttributeMatcher(attributeName = "vc", elementName = "BranchPoint")
        public void visitedBranchCountMatcher(String attributeValue) {
            branchVisited = !"0".equals(attributeValue);

        }
        
        @AttributeMatcher(attributeName = "offset", elementName = "BranchPoint")
        public void offsetBranchPointMatcher(String attributeValue) {
            int offset=Integer.parseInt(attributeValue);
            int branchLine=offsetToLineMapper.mapOffsetToLine(offset);
            coveredFile.addBranchPoint(branchLine,branchVisited);
        }
        

}
        
