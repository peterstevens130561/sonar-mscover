package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import java.io.File;
import java.util.List;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.FileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.SonarBranchSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.SonarLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageSaver;

public class DefaultCoverageSaver implements CoverageSaver {

    private MeasureSaver measureSaver;
    private SonarBranchSaver branchCoverageSaver;
    private SonarLineSaver lineCoverageSaver;
	private List<File> testFiles;
    
    public DefaultCoverageSaver(MeasureSaver measureSaver,
    		SonarBranchSaver branchCoverageSaver, SonarLineSaver lineCoverageSaver){
    	this.measureSaver = measureSaver;
    	this.branchCoverageSaver = branchCoverageSaver;
    	this.lineCoverageSaver = lineCoverageSaver;
    	
    }
	
	@Override
	public void save(Project project, SensorContext sonarContext,
			SonarCoverage sonarCoverage) {
        for(SonarFileCoverage fileCoverage:sonarCoverage.getValues()) {
            saveFileResults(fileCoverage);
        }
	}

	
    public void setExcludeSourceFiles(List<File> testFiles) {
        this.testFiles=testFiles;
    }
	private void saveFileResults(SonarFileCoverage fileCoverage) {
        File file=new File(fileCoverage.getAbsolutePath());

        if(testFiles !=null && !testFiles.contains(file)) {	
        	for(FileCoverageSaver fileCoverageSaver : fileCoverageSavers) {
                lineCoverageSaver.saveMeasures(fileCoverage.getLinePoints(), file);
                branchCoverageSaver.saveMeasures(fileCoverage.getBranchPoints(), file);
        	}
        }
	}

}
