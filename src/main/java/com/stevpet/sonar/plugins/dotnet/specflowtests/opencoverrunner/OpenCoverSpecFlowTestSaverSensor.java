/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
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
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.CoverageFileLocator;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.CoverageHashes;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.DefaultCoverageFileLocator;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.OpenCoverModuleSplitter;
import com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor.OverallCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;


@DependedUpon(value="IntegrationTestCoverageSaved")
public class OpenCoverSpecFlowTestSaverSensor implements Sensor {

	private static Logger LOG = LoggerFactory.getLogger(OpenCoverSpecFlowTestSaverSensor.class);
	private final OpenCoverIntegrationTestCoverageReader reader;
	private final CoverageSaver saver;
	private final OpenCoverModuleSplitter openCoverModuleSaver;
	private final IntegrationTestsConfiguration integrationTestsConfiguration;
    private MsCoverConfiguration msCoverConfiguration;
    private final OverallCoverageRepository overallCoverageCache;
    private final CoverageFileLocator coverageFileLocator = new DefaultCoverageFileLocator();
	
	/**
	 * Instantiate with default dependencies, from Plugin
	 * @param msCoverConfiguration
	 * @param microsoftWindowsEnvironment
	 * @param pathResolver
	 * @param fileSystem
	 * @param settings
	 * @param coverageHashes 
	 */
	public OpenCoverSpecFlowTestSaverSensor(
			MsCoverConfiguration msCoverConfiguration,
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			PathResolver pathResolver,
			FileSystem fileSystem,
			Settings settings,
			IntegrationTestsConfiguration integrationTestsConfiguration,
			OverallCoverageRepository overallCoverageCache) {
		this(
				new OpenCoverIntegrationTestCoverageReader(microsoftWindowsEnvironment, msCoverConfiguration, fileSystem, integrationTestsConfiguration),
				new DefaultCoverageSaverFactory(microsoftWindowsEnvironment, pathResolver, fileSystem).createOpenCoverIntegrationTestCoverageSaver(), 
				new OpenCoverModuleSplitter(new CoverageHashes()),integrationTestsConfiguration,
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
			OpenCoverModuleSplitter openCoverModuleSaver, 
			IntegrationTestsConfiguration integrationTestsConfiguration,
			IntegrationTestSensorHelper integrationTestSensorHelper, MsCoverConfiguration msCoverConfiguration,
			OverallCoverageRepository overallCoverageCache) {
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
        DefaultProjectCoverageRepository sonarCoverage = new DefaultProjectCoverageRepository();
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
        //File artifactFile=openCoverModuleSaver.setProject(module.getName()).setRoot(coverageDir).getCoverageFile(module.getName());
        String projectName=module.getName();
        File artifactFile=coverageFileLocator.getFile(coverageDir, projectName, projectName);
        File artifactDir=artifactFile.getParentFile();
        return artifactDir;
    } 
}
