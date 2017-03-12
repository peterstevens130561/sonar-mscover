/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2017 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.utils.text.XmlWriter;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;

public class DefaultCoverageSerializationService implements CoverageSerializationService {


    @Override
    public void Serialize(XmlWriter xmlWriter, ProjectCoverageRepository repository) {

        xmlWriter.begin("coverage").prop("version", "1");
        repository.getValues().forEach(f -> writeFile(xmlWriter,f));
        xmlWriter.end();
    }

    private void writeFile(XmlWriter xmlWriter,SonarFileCoverage f) {
        String absolutePath=f.getAbsolutePath();
        xmlWriter.begin("file").prop("path", absolutePath);
        GetLinesToCover(f).forEach(l -> writeLine(xmlWriter,l));
        xmlWriter.end();
    }

    private void writeLine(XmlWriter xmlWriter,LineToCover line) {
        String isCovered=line.getCovered()?"true":"false";
        int lineNumber = line.getLine();
        int branchesToCover=line.getBranchesToCover();
        int coveredBranches=line.getCoveredBranches();
        xmlWriter.begin("lineToCover")
            .prop("lineNumber", lineNumber)
            .prop("covered", isCovered);
        if(branchesToCover>0) {
            xmlWriter.prop("branchesToCover", branchesToCover)
            .prop("coveredBranches", coveredBranches);
        }
        xmlWriter.end();
    }

    private List<LineToCover> GetLinesToCover(SonarFileCoverage fileCoverage) {
        List<LineToCover> result = new ArrayList<LineToCover>();
        fileCoverage.getLinePoints().getPoints().forEach(p -> {
            LineToCover line = new LineToCover(p.getLine(), p.getCovered() > 0);
            result.add(line);
        });
        int lineIndex = 0;
        for (CoverageLinePoint coveragePoint : fileCoverage.getBranchPoints().getPoints()) {
            while (lineIndex < result.size() && result.get(lineIndex).getLine() != coveragePoint.getLine()) {
                lineIndex++;
            }
            if (lineIndex == result.size()) {
                String msg = "Could not find matching line for branchpoint " + coveragePoint.getLine() + " in file "
                        + fileCoverage.getAbsolutePath();
                throw new IllegalStateException(msg);
            }
            LineToCover line = result.get(lineIndex);
            line.setBranchesToCover(coveragePoint.getToCover()).setCoveredBranches(coveragePoint.getCovered());

        }
        ;
        return result;
    }

}
