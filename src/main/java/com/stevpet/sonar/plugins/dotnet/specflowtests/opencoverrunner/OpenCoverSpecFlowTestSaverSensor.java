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

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Mode;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Tool;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.OpenCoverIntegrationTestCoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.DefaultCoverageSaverFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.OpenCoverModuleSaver;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;


public class OpenCoverSpecFlowTestSaverSensor implements Sensor {

	private static Logger Log = LoggerFactory.getLogger(OpenCoverSpecFlowTestSaverSensor.class);
	private final OpenCoverIntegrationTestCoverageReader reader;
	private final CoverageSaver saver;
	private final OpenCoverModuleSaver openCoverModuleSaver;
	private final IntegrationTestsConfiguration integrationTestsConfiguration;
	private final IntegrationTestSensorHelper integrationTestSensorHelper;
    private MsCoverConfiguration msCoverConfiguration;
	
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
			Settings settings,
			IntegrationTestsConfiguration integrationTestsConfiguration) {
		this(
				new OpenCoverIntegrationTestCoverageReader(microsoftWindowsEnvironment, msCoverConfiguration, fileSystem, integrationTestsConfiguration),
				new DefaultCoverageSaverFactory(microsoftWindowsEnvironment, pathResolver, fileSystem).createOpenCoverIntegrationTestCoverageSaver(), 
				new OpenCoverModuleSaver(),integrationTestsConfiguration,
				new IntegrationTestSensorHelper(microsoftWindowsEnvironment,integrationTestsConfiguration),msCoverConfiguration);
		        
	}

	
	/**
	 * Instantiate with all dependencies
	 * @param coverageReader
	 * @param coverageSaver
	 * @param openCoverModuleSaver
	 * @param integrationTestsConfiguration
	 * @param msCoverConfiguration 
	 */
	public OpenCoverSpecFlowTestSaverSensor(
			OpenCoverIntegrationTestCoverageReader coverageReader,
			CoverageSaver coverageSaver,
			OpenCoverModuleSaver openCoverModuleSaver, 
			IntegrationTestsConfiguration integrationTestsConfiguration,
			IntegrationTestSensorHelper integrationTestSensorHelper, MsCoverConfiguration msCoverConfiguration) {
		this.reader=coverageReader;
		this.saver=coverageSaver;
		this.openCoverModuleSaver=openCoverModuleSaver;
		this.integrationTestsConfiguration=integrationTestsConfiguration;
		this.integrationTestSensorHelper=integrationTestSensorHelper;
		this.msCoverConfiguration=msCoverConfiguration;
	}
	
	@Override
	public boolean shouldExecuteOnProject(Project project) {
	        return project.isModule() && integrationTestsConfiguration.matches(Tool.OPENCOVER,Mode.READ);
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
        reader.setMsCoverConfiguration(msCoverConfiguration);
        reader.read(sonarCoverage,artifactDir);
        saver.save(context,sonarCoverage);
    } 
}
