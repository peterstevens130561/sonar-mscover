package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;


import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Mode;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Tool;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.OpenCoverIntegrationTestCoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.DefaultCoverageSaverFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.integrationtests.IntegrationTestsCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.OpenCoverModuleSaver;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;


public class OpenCoverSpecFlowTestSaverSensor implements Sensor {

	private static Logger Log = LoggerFactory.getLogger(OpenCoverSpecFlowTestSaverSensor.class);
	private CoverageReader reader;
	private CoverageSaver saver;
	private OpenCoverModuleSaver openCoverModuleSaver;
	private IntegrationTestsConfiguration integrationTestsConfiguration;
	
	/**
	 * Instantiate with default dependencies, from Plugin
	 * @param msCoverConfiguration
	 * @param microsoftWindowsEnvironment
	 * @param pathResolver
	 * @param fileSystem
	 * @param settings
	 */
	public OpenCoverSpecFlowTestSaverSensor(
			MsCoverConfiguration msCoverConfiguration,
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			PathResolver pathResolver,
			FileSystem fileSystem,
			Settings settings) {
		this(
				new OpenCoverIntegrationTestCoverageReader(microsoftWindowsEnvironment, msCoverConfiguration, fileSystem),
				new DefaultCoverageSaverFactory(microsoftWindowsEnvironment, pathResolver, fileSystem).createOpenCoverIntegrationTestCoverageSaver(), 
				new OpenCoverModuleSaver(), 
				new DefaultIntegrationTestsConfiguration(settings, fileSystem));
	}

	
	/**
	 * Instantiate with all dependencies
	 * @param coverageReader
	 * @param coverageSaver
	 * @param openCoverModuleSaver
	 * @param integrationTestsConfiguration
	 */
	public OpenCoverSpecFlowTestSaverSensor(
			CoverageReader coverageReader,
			CoverageSaver coverageSaver,
			OpenCoverModuleSaver openCoverModuleSaver, 
			IntegrationTestsConfiguration integrationTestsConfiguration) {
		this.reader=coverageReader;
		this.saver=coverageSaver;
		this.openCoverModuleSaver=openCoverModuleSaver;
		this.integrationTestsConfiguration=integrationTestsConfiguration;
	}
	
	@Override
	public boolean shouldExecuteOnProject(Project project) {
		return integrationTestsConfiguration.matches(Tool.OPENCOVER,Mode.READ);
	}

	@Override
	public void analyse(Project module, SensorContext context) {

	    File coverageDir=integrationTestsConfiguration.getDirectory();	
        File artifactFile=openCoverModuleSaver.setProject(module.getName()).setRoot(coverageDir).getCoverageFile(module.getName());
        File artifactDir=artifactFile.getParentFile();
        if(!artifactDir.exists()) {
        	Log.warn("No coverage file available for project {} in dir {}",module.getName(),artifactDir.getAbsolutePath());
        	return;
        }
        SonarCoverage sonarCoverage = new SonarCoverage();
        reader.read(sonarCoverage,artifactDir);
        saver.save(context,sonarCoverage);
    } 
}
