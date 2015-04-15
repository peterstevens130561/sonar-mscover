package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import java.io.File;
import java.util.List;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;

public class DefaultCoverageSaver implements CoverageSaver {

	private final static Logger LOG = LoggerFactory.getLogger(DefaultCoverageSaver.class);
    private BranchFileCoverageSaver branchCoverageSaver;
    private LineFileCoverageSaver lineCoverageSaver;
	private List<File> testFiles;
    
    public DefaultCoverageSaver(
    		BranchFileCoverageSaver branchCoverageSaver, LineFileCoverageSaver lineCoverageSaver){
    	this.branchCoverageSaver = branchCoverageSaver;
    	this.lineCoverageSaver = lineCoverageSaver;
    }
    
	
	@Override
	public void save(SonarCoverage sonarCoverage) {
		LOG.info("Invoked");
        for(SonarFileCoverage fileCoverage:sonarCoverage.getValues()) {
            saveFileResults(fileCoverage);
        }
	}
	
    public void setExcludeSourceFiles(List<File> testFiles) {
        this.testFiles=testFiles;
    }
	private void saveFileResults(SonarFileCoverage fileCoverage) {
        File file=new File(fileCoverage.getAbsolutePath());

        if(testFiles ==null || !testFiles.contains(file)) {	
                lineCoverageSaver.saveMeasures(fileCoverage.getLinePoints(), file);
                branchCoverageSaver.saveMeasures(fileCoverage.getBranchPoints(), file);
        }
	}

}
