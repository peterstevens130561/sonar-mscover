/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.model.OpenCoverSequencePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.model.SequencePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.AttributeMatcher;

public class OpenCoverSequencePointsObserver extends OpenCoverObserver {

        private SonarCoverage registry ;
        private SequencePoint sequencePoint;

        private SonarFileCoverage coveredFile;
        private boolean lineVisited;
        private BranchOffsetToLineMapper offsetToLineMapper = new BranchOffsetToLineMapper();
        private boolean branchVisited;
        
        public OpenCoverSequencePointsObserver(SonarCoverage registry) {
            this.registry = registry;
        }
        
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
           coveredFile=registry.getCoveredFile(attributeValue);
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
        
        @AttributeMatcher(attributeName ="vc", elementName = "BranchPoint")
        public void visitedBranchCountMatcher(String attributeValue) {
            branchVisited = !"0".equals(attributeValue);

        }
        
        @AttributeMatcher(attributeName = "offset", elementName = "BranchPoint")
        public void offsetBranchPointMatcher(String attributeValue) {
            int offset=Integer.parseInt(attributeValue);
            int branchLine=offsetToLineMapper.mapOffsetToLine(offset);
            if(branchLine != -1) {
                coveredFile.addBranchPoint(branchLine,branchVisited);
            }

        }
        

}
        
