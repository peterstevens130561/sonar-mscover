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
package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import com.stevpet.sonar.plugins.common.api.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver.Event;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.model.OpenCoverSequencePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.model.SequencePoint;
public class OpenCoverSequencePointsObserver extends OpenCoverObserver {



        private static final String FULL_NAME = "FullName";
        private SonarCoverage registry ;
        private SequencePoint sequencePoint;

        private SonarFileCoverage coveredFile;
        private boolean lineVisited;
        private BranchOffsetToLineMapper offsetToLineMapper = new BranchOffsetToLineMapper();
        private boolean branchVisited;
        private int line;
        private String fileId;
        
        
        private static final String SEQUENCE_POINT = "SequencePoint";
        private static final String SEQUENCEPOINT_PATH = "Modules/Module/Classes/Class/Methods/Method/SequencePoints/" + SEQUENCE_POINT;
        
        private static final String FILE_REF = "FileRef";
        private static final String FILEREF_PATH = "Modules/Module/Classes/Class/Methods/Method/" + FILE_REF;
        
        private static final String BRANCH_POINT = "BranchPoint";
        private static final String BRANCHPOINT_PATH = "Modules/Module/Classes/Class/Methods/Method/BranchPoints/" + BRANCH_POINT ;
        
        private static final String FULLNAME_PATH = "Modules/Module/Classes/Class/" + FULL_NAME;
        public OpenCoverSequencePointsObserver() {

            setPattern(FULLNAME_PATH +
                    "|" + FILEREF_PATH +
                     "|" + SEQUENCEPOINT_PATH +
                     "|" + BRANCHPOINT_PATH +
                     ""
                     );
        }

        public void setRegistry(SonarCoverage registry) {
            this.registry = registry;
        }
        @ElementMatcher(elementName=FULL_NAME)
        public void classMatcher(String text) {
            offsetToLineMapper.init();
            coveredFile = new SonarFileCoverage(); // make sure we have somewhere to register the data, maybe there is no FileRef
        }
        /**
         * FilerRef has uid attribute, which referes to the file in the Files element which has that uid
         * The 
         * @param attributeValue
         */
        @AttributeMatcher(attributeName="uid",elementName=FILE_REF)
        public void fileRefMatcher(String attributeValue) {
           coveredFile=registry.getCoveredFile(attributeValue);
           offsetToLineMapper.init();
        }
        
        @AttributeMatcher(attributeName = "vc", elementName = SEQUENCE_POINT)
        public void visitedCountMatcher(String attributeValue) {
           lineVisited = !"0".equals(attributeValue);

        }
        
        @AttributeMatcher(attributeName="offset",elementName=SEQUENCE_POINT)
        public void offsetMatcher(String attributeValue) {
            sequencePoint.setOffset(attributeValue);
        }
        
        @AttributeMatcher(attributeName="sl",elementName=SEQUENCE_POINT)
        public void startLineMatcher(String attributeValue) {

            sequencePoint.setStartLine(attributeValue);
            line=Integer.parseInt(attributeValue);
        }
  
        @AttributeMatcher(attributeName="fileid",elementName=SEQUENCE_POINT)
        public void fileIdMatcher(String attributeValue) {
               fileId=attributeValue;

        }

        private boolean isPragmaFile(String attributeValue) {
            return "0".equals(attributeValue);
        }
        
        @AttributeMatcher(attributeName ="vc", elementName = BRANCH_POINT)
        public void visitedBranchCountMatcher(String attributeValue) {
            branchVisited = !isPragmaFile(attributeValue);

        }
        
        @AttributeMatcher(attributeName = "offset", elementName = BRANCH_POINT)
        public void offsetBranchPointMatcher(String attributeValue) {
            int offset=Integer.parseInt(attributeValue);
            int branchLine=offsetToLineMapper.mapOffsetToLine(offset);
            if(branchLine != -1) {
                coveredFile.addBranchPoint(branchLine,branchVisited);
            }

        }
        
        @ElementObserver(path=SEQUENCEPOINT_PATH,event=Event.ENTRY)
        public void sequencePointEntry() {
            sequencePoint=new OpenCoverSequencePoint();
        }
        
        @ElementObserver(path=SEQUENCEPOINT_PATH, event=Event.EXIT)
        public void sequencePointExit() {
            if(isPragmaFile(fileId)) {
                return;
            }
            offsetToLineMapper.addSequencePoint(sequencePoint);
            coveredFile.addLinePoint(line,lineVisited);
        }
        
}
        
