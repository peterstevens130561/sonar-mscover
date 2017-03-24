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


import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
/**
 * Serializes to the generic coverage format of SonarQube introduced in 5.6
 * <br>
 * <pre>
 * {@code <coverage version="1">
 * <file path="sources/hello/NoConditions.xoo">
 * <lineToCover lineNumber="6" covered="true"/>
 * <lineToCover lineNumber="7" covered="false"/>  
 * </file>
 * <file path="xources/hello/WithConditions.xoo">
 *   <lineToCover lineNumber="3" covered="true" branchesToCover="2" coveredBranches="1"/>
 * </file>
 * </coverage> }
 * </pre>
 */

public class DefaultCoverageSerializationService implements CoverageSerializationService {

    @Override
    public void Serialize(XMLStreamWriter xmlStreamWriter, ProjectCoverageRepository repository) {
        try {
            writeDocumentStart(xmlStreamWriter);
            for (SonarFileCoverage fileCoverage : repository.getValues()) {
                writeFile(xmlStreamWriter, fileCoverage);
            }
            writeDocumentEnd(xmlStreamWriter);
        } catch (XMLStreamException e) {
            throw new IllegalStateException(e);
        }

    }


    private void writeFile(XMLStreamWriter xmlStreamWriter, SonarFileCoverage f) throws XMLStreamException {
        String absolutePath = f.getAbsolutePath();
        writeFileElement(xmlStreamWriter, absolutePath);
        for (LineToCover lineToCover : getCoveragePerLineToCover(f)) {
            writeLine(xmlStreamWriter, lineToCover);
        }
        writeEndElement(xmlStreamWriter);
    }

    private void writeLine(XMLStreamWriter xmlStreamWriter, LineToCover line) throws XMLStreamException {
        String isCovered = line.getCovered() ? "true" : "false";
        Integer lineNumber = line.getLine();
        Integer branchesToCover = line.getBranchesToCover();
        Integer coveredBranches = line.getCoveredBranches();
        xmlStreamWriter.writeCharacters("\t");
        xmlStreamWriter.writeStartElement("lineToCover");
        xmlStreamWriter.writeAttribute("lineNumber", lineNumber.toString());
        xmlStreamWriter.writeAttribute("covered", isCovered);
        if (branchesToCover > 0) {
            xmlStreamWriter.writeAttribute("branchesToCover", branchesToCover.toString());
            xmlStreamWriter.writeAttribute("coveredBranches", coveredBranches.toString());
        }
        writeEndElement(xmlStreamWriter);
    }

    private List<LineToCover> getCoveragePerLineToCover(SonarFileCoverage fileCoverage) {
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

    private void writeDocumentEnd(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        writeEndElement(xmlStreamWriter);
        xmlStreamWriter.writeEndDocument();
    }

    private void writeDocumentStart(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeStartDocument("1.0");
        xmlStreamWriter.writeCharacters("\n");

        xmlStreamWriter.writeStartElement("coverage");
        xmlStreamWriter.writeAttribute("version", "1");
    }
    
    private void writeEndElement(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeCharacters("\n");
    }

    private void writeFileElement(XMLStreamWriter xmlStreamWriter, String absolutePath) throws XMLStreamException {
        xmlStreamWriter.writeStartElement("file");
        xmlStreamWriter.writeAttribute("path", absolutePath);
        xmlStreamWriter.writeCharacters("\n");
    }
}
