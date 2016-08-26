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
        private int sequencePointLine;
        private String fileId;
        private int branchLine;
        private int branchPath;
        private int visited;
        private String uid;
        
        
        private static final String SEQUENCE_POINT = "SequencePoint";
        private static final String SEQUENCEPOINT_PATH = "Modules/Module/Classes/Class/Methods/Method/SequencePoints/" + SEQUENCE_POINT;
        
        private static final String FILE_REF = "FileRef";
        private static final String FILEREF_PATH = "Modules/Module/Classes/Class/Methods/Method/" + FILE_REF;
        
        private static final String BRANCH_POINT = "BranchPoint";
        private static final String BRANCHPOINT_PATH = "Modules/Module/Classes/Class/Methods/Method/BranchPoints/" + BRANCH_POINT ;
        
        private static final String FULLNAME_PATH = "Modules/Module/Classes/Class/" + FULL_NAME;


        public void setRegistry(SonarCoverage registry) {
            this.registry = registry;
        }
        
        @Override
        public void registerObservers(ObserverRegistrar registrar) {
            registrar.inPath("Modules/Module/Classes/Class", module -> module.onElement(FULL_NAME, this::classMatcher));
            registrar.inPath("Modules/Module/Classes/Class/Methods/Method",method -> method
            .onAttribute(FILE_REF + "/uid", this::fileRefMatcher));
            
            registrar.inPath("Modules/Module/Classes/Class/Methods/Method/SequencePoints", method -> method.inElement("SequencePoint", sequencePointB -> sequencePointB
            .onAttribute("vc", this::visitedCountMatcher)
            .onAttribute("offset", this::offsetMatcher)
            .onAttribute("sl", this::startLineMatcher)
            .onAttribute("fileid",this::fileIdMatcher)));
            
            registrar.inPath("Modules/Module/Classes/Class/Methods/Method/BranchPoints", branchPoints -> branchPoints.inElement("BranchPoint", branchPoint -> branchPoint
            .onAttribute("vc",this::visitedBranchCountMatcher)
            .onAttribute("sl",this::lineBranchPoint)
            .onAttribute("path",this::pathBranchPoint)));
            
            //TODO mind the events
            registrar.inPath("Modules/Module/Classes/Class/Methods/Method/BranchPoints", branchPoints -> branchPoints
            .onExit(BRANCH_POINT, this::branchPointExit));
            
            registrar.inPath("Modules/Module/Classes/Class/Methods/Method/SequencePoints", method -> method
            .onEntry(SEQUENCE_POINT, this::sequencePointEntry)
            .onExit(SEQUENCE_POINT, this::sequencePointExit));
        }
        
        public void classMatcher(String text) {
            offsetToLineMapper.init();
            coveredFile = new SonarFileCoverage(); // make sure we have somewhere to register the data, maybe there is no FileRef
        }
        /**
         * FilerRef has uid attribute, which referes to the file in the Files element which has that uid
         * The 
         * @param uid
         */
        public void fileRefMatcher(String uid) {
           this.uid= uid;
           coveredFile=registry.getCoveredFile(uid);
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
            sequencePointLine=Integer.parseInt(attributeValue);
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
            coveredFile.addLinePoint(sequencePointLine,lineVisited);
        }

        SequencePoint getSequencePoint() {
            return sequencePoint;
        }
        
        public int getBranchLine() {
            return branchLine;
        }
        
        public int getBranchPath() {
            return branchPath ;
        }
        
        public boolean getBranchVisited() {
            return branchVisited ;
        }
        
        public int getSequencePointLine() {
            return  sequencePointLine ;
        }
        
        public boolean getSequencePointVisited() {
            return lineVisited;
        }
        
        public String getMethodUid() {
            return uid;
        }
  

        
}
        
