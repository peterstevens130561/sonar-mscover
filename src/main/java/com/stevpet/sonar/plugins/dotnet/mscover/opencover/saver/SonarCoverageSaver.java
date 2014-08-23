package com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver;

import java.io.File;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.line.LineMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class SonarCoverageSaver {

    private SonarCoverage sonarCoverageRegistry;
    private MeasureSaver measureSaver;
    private FileCoverageSaver sonarBranchSaver;
    private FileCoverageSaver sonarLineSaver;
    public SonarCoverageSaver(SensorContext sensorContext, Project project,MeasureSaver measureSaver) {

        this.measureSaver=measureSaver;
        
    }

    public void setCoverageRegistry(SonarCoverage sonarCoverageRegistry) {
        this.sonarCoverageRegistry=sonarCoverageRegistry;
    }
    
    public void save() {
        sonarBranchSaver = SonarBranchSaver.create(measureSaver);
        sonarLineSaver = SonarLineSaver.create(measureSaver);
        for(SonarFileCoverage fileCoverage:sonarCoverageRegistry.getValues()) {
            saveFileResults(fileCoverage);
        }
    }

    private void saveFileResults(SonarFileCoverage fileCoverage) {
        File file=new File(fileCoverage.getAbsolutePath());
        sonarLineSaver.saveMeasures(fileCoverage.getLinePoints(), file);
        sonarBranchSaver.saveMeasures(fileCoverage.getBranchPoints(), file);
    }

}
