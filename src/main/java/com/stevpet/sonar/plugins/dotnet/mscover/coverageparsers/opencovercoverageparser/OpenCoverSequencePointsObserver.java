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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.api.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver.Event;
import com.stevpet.sonar.plugins.common.parser.ObserverRegistrar;
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
        private int branchLine;
        private int branchPath;
        private int visited;
        
        
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
        
        @Override
        public void registerObservers(ObserverRegistrar registrar) {
            registrar.onElement(this::classMatcher, FULL_NAME)
            .onAttribute(FILE_REF + "/uid", this::fileRefMatcher)
            .onAttribute(SEQUENCE_POINT + "/vc", this::visitedCountMatcher)
            .onAttribute(SEQUENCE_POINT + "/offset", this::offsetMatcher)
            .onAttribute(SEQUENCE_POINT + "/sl", this::startLineMatcher)
            .onAttribute(SEQUENCE_POINT + "/fileid",this::fileIdMatcher)
            .onAttribute(BRANCH_POINT + "/vc",this::visitedBranchCountMatcher)
            .onAttribute(BRANCH_POINT + "/sl",this::lineBranchPoint)
            .onAttribute(BRANCH_POINT + "/path",this::pathBranchPoint)
            .onExit(this::branchPointExit, BRANCHPOINT_PATH)
            .onEntry(this::sequencePointEntry, SEQUENCEPOINT_PATH)
            .onExit(this::sequencePointExit, SEQUENCEPOINT_PATH);
        }
        
        public void classMatcher(String text) {
            offsetToLineMapper.init();
            coveredFile = new SonarFileCoverage(); // make sure we have somewhere to register the data, maybe there is no FileRef
        }
        /**
         * FilerRef has uid attribute, which referes to the file in the Files element which has that uid
         * The 
         * @param attributeValue
         */
        public void fileRefMatcher(String attributeValue) {
           coveredFile=registry.getCoveredFile(attributeValue);
           offsetToLineMapper.init();
        }
        
        public void visitedCountMatcher(String attributeValue) {
           lineVisited = !"0".equals(attributeValue);

        }
        
        public void offsetMatcher(String attributeValue) {
            sequencePoint.setOffset(attributeValue);
        }
        

        public void startLineMatcher(String attributeValue) {

            sequencePoint.setStartLine(attributeValue);
            line=Integer.parseInt(attributeValue);
        }
  
        public void fileIdMatcher(String attributeValue) {
               fileId=attributeValue;

        }

        private boolean isPragmaFile(String attributeValue) {
            return "0".equals(attributeValue);
        }
        

        public void visitedBranchCountMatcher(String attributeValue) {
            branchVisited = !isPragmaFile(attributeValue);

        }
        

        public void lineBranchPoint(String attributeValue) {
            branchLine=Integer.parseInt(attributeValue);
        }
        
        public void pathBranchPoint(String attributeValue) {
            branchPath=Integer.parseInt(attributeValue);
        }
        

        public void branchPointExit() { 

            if(isPragmaFile(fileId)) {
                return;
            }
            coveredFile.addBranchPoint(branchLine,branchPath,branchVisited);
        }
        
        public void sequencePointEntry() {
            sequencePoint=new OpenCoverSequencePoint();
        }
        
        public void sequencePointExit() { 
            if(isPragmaFile(fileId)) {
                return;
            }
            offsetToLineMapper.addSequencePoint(sequencePoint);
            coveredFile.addLinePoint(line,lineVisited);
        }


        
}
        
