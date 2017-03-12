package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.sonar.api.utils.text.XmlWriter;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;

public class DefaultCoverageSerializationService implements CoverageSerializationService {
    private XmlWriter xmlWriter;

    public DefaultCoverageSerializationService(XmlWriter xmlWriter) {
        this.xmlWriter = xmlWriter;
    }

    @Override
    public void Serialize(File file, ProjectCoverageRepository repository) {
        xmlWriter.begin("coverage").prop("version", "1");

        repository.getValues().forEach(f -> writeFile(f));
        xmlWriter.end();

    }

    private void writeFile(SonarFileCoverage f) {
        xmlWriter.begin("file").prop("path", f.getAbsolutePath());
        GetLinesToCover(f).forEach(l -> writeLine(l));
        xmlWriter.end();
    }

    private void writeLine(LineToCover line) {
        xmlWriter.begin("lineToCover");
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
