package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;


import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependedUpon;
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
import com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor.CoverageCache;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;


@DependedUpon(value="IntegrationTestCoverageSaved")
public class OpenCoverSpecFlowTestSaverSensor implements Sensor {

	private static Logger LOG = LoggerFactory.getLogger(OpenCoverSpecFlowTestSaverSensor.class);
	private final OpenCoverIntegrationTestCoverageReader reader;
	private final CoverageSaver saver;
	private final OpenCoverModuleSaver openCoverModuleSaver;
	private final IntegrationTestsConfiguration integrationTestsConfiguration;
    private MsCoverConfiguration msCoverConfiguration;
    private final CoverageCache overallCoverageCache;
	
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
			IntegrationTestsConfiguration integrationTestsConfiguration,
			CoverageCache overallCoverageCache) {
		this(
				new OpenCoverIntegrationTestCoverageReader(microsoftWindowsEnvironment, msCoverConfiguration, fileSystem, integrationTestsConfiguration),
				new DefaultCoverageSaverFactory(microsoftWindowsEnvironment, pathResolver, fileSystem).createOpenCoverIntegrationTestCoverageSaver(), 
				new OpenCoverModuleSaver(),integrationTestsConfiguration,
				new IntegrationTestSensorHelper(microsoftWindowsEnvironment,integrationTestsConfiguration),msCoverConfiguration,
				overallCoverageCache);
		        
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
			IntegrationTestSensorHelper integrationTestSensorHelper, MsCoverConfiguration msCoverConfiguration,
			CoverageCache overallCoverageCache) {
		this.reader=coverageReader;
		this.saver=coverageSaver;
		this.openCoverModuleSaver=openCoverModuleSaver;
		this.integrationTestsConfiguration=integrationTestsConfiguration;
		this.msCoverConfiguration=msCoverConfiguration;
		this.overallCoverageCache = overallCoverageCache;
	}
	
	@Override
	public boolean shouldExecuteOnProject(Project project) {
	        return project.isModule() && integrationTestsConfiguration.matches(Tool.OPENCOVER,Mode.READ);
	}

	@Override
	public void analyse(Project module, SensorContext context) {
	    LOG.info("OpenCoverSpecFlowTestSaverSensor invoked");
	    File integrationTestCoverageDir = getModuleIntegrationTestCoverageDir(module);
        if(!integrationTestCoverageDir.exists()) {
        	LOG.warn("No coverage file available for project {} in dir {}",module.getName(),integrationTestCoverageDir.getAbsolutePath());
        	return;
        }
        SonarCoverage sonarCoverage = new SonarCoverage();
        reader.setMsCoverConfiguration(msCoverConfiguration);
        reader.read(sonarCoverage,integrationTestCoverageDir);
        saver.save(context,sonarCoverage);
        overallCoverageCache.merge(sonarCoverage, module.getName());
    }


	/**
	 * Directory that has the integration test coverage files for this module
	 * @param module
	 * @return
	 */
    private File getModuleIntegrationTestCoverageDir(Project module) {
        File coverageDir=integrationTestsConfiguration.getDirectory();	
        File artifactFile=openCoverModuleSaver.setProject(module.getName()).setRoot(coverageDir).getCoverageFile(module.getName());
        File artifactDir=artifactFile.getParentFile();
        return artifactDir;
    } 
}
