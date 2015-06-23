package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import java.io.File;
import java.util.List;








import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.LineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class DefaultCoverageSaver implements CoverageSaver {

	private final static Logger LOG = LoggerFactory.getLogger(DefaultCoverageSaver.class);
    private BranchFileCoverageSaver branchCoverageSaver;
    private LineFileCoverageSaver lineCoverageSaver;
	private List<File> testFiles;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    
    public DefaultCoverageSaver(
    		BranchFileCoverageSaver branchCoverageSaver, LineFileCoverageSaver lineCoverageSaver,MicrosoftWindowsEnvironment microsoftWindowsEnvironment){
    	this.branchCoverageSaver = branchCoverageSaver;
    	this.lineCoverageSaver = lineCoverageSaver;
    	this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
    }
    
	
	@Override
	public void save(SonarCoverage sonarCoverage) {
		LOG.info("Invoked");
		this.testFiles=microsoftWindowsEnvironment.getUnitTestSourceFiles();
        for(SonarFileCoverage fileCoverage:sonarCoverage.getValues()) {
            saveFileResults(fileCoverage);
        }
	}
	
	@Deprecated
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
