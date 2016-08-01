package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.LineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.nullsaver.NullBranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class CoverageSaverBase implements CoverageSaver {

    private final static Logger LOG = LoggerFactory.getLogger(CoverageSaverBase.class);
    private BranchFileCoverageSaver branchCoverageSaver;
    private LineFileCoverageSaver lineCoverageSaver;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;

    public CoverageSaverBase(
            BranchFileCoverageSaver branchCoverageSaver, LineFileCoverageSaver lineCoverageSaver,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.branchCoverageSaver = branchCoverageSaver;
        this.lineCoverageSaver = lineCoverageSaver;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
    }
    
    public static CoverageSaver  createVsTestUnitTestCoverageSaver(
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			PathResolver pathResolver, FileSystem fileSystem) {
		return new CoverageSaverBase(new NullBranchFileCoverageSaver(),
				new UnitTestLineFileCoverageSaver(new DefaultResourceResolver(
						pathResolver, fileSystem)), microsoftWindowsEnvironment);
	}
    

    /**
     * @deprecated use save(sensorContext,sonarCoverage) instead
     */
    @Override
    public void save(SonarCoverage sonarCoverage) {
        List<File> testFiles = microsoftWindowsEnvironment.getUnitTestSourceFiles();
        if (testFiles == null || testFiles.size()==0) {
            LOG.warn("solution has no testfiles to exclude from results");
        }
        for (SonarFileCoverage fileCoverage : sonarCoverage.getValues()) {
            saveFileResults(fileCoverage,testFiles);
        }
    }

    private void saveFileResults(SonarFileCoverage fileCoverage,List<File> testFiles) {
        File file = new File(fileCoverage.getAbsolutePath());

        if (testFiles != null && testFiles.contains(file)) {
            LOG.debug("excluding test file from coverage " + file.getAbsolutePath());
        } else {
            lineCoverageSaver.saveMeasures(fileCoverage.getLinePoints(), file);
            branchCoverageSaver.saveMeasures(fileCoverage.getBranchPoints(), file);
        }
    }

	@Override
	public void save(SensorContext sensorContext, SonarCoverage sonarCoverage) {
		branchCoverageSaver.setSensorContext(sensorContext);
		lineCoverageSaver.setSensorContext(sensorContext);
		save(sonarCoverage);
	}

}
