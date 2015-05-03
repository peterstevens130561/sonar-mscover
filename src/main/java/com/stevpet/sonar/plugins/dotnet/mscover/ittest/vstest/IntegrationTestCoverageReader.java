package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.CoverageToXmlConverter;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser.FilteringCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageReaderStep;

public class IntegrationTestCoverageReader implements CoverageReaderStep {
	private final static Logger LOG = LoggerFactory.getLogger(IntegrationTestCoverageReader.class);
	private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
	private MsCoverProperties msCoverProperties;
	private FilteringCoverageParser coverageParser;
	private CoverageToXmlConverter coverageToXmlConverter;
	public IntegrationTestCoverageReader(MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			MsCoverProperties msCoverProperties,
			FilteringCoverageParser coverageParser,
			CoverageToXmlConverter coverageToXmlConverter) {
		this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
		this.msCoverProperties=msCoverProperties;
		this.coverageParser = coverageParser;
				this.coverageToXmlConverter =coverageToXmlConverter;
	}
	/**
	 * @param file may be either a single file to parse, or a directory which holds files
	 */
	@Override
	public void read(SonarCoverage registry, File file) {

		String integrationTestsPath=msCoverProperties.getIntegrationTestsDir();
		String coveragePath = msCoverProperties.getIntegrationTestsPath();
		if(StringUtils.isNotEmpty(integrationTestsPath)) {
			File integrationTestsDir=new File(integrationTestsPath);
			readFilesFromDir(registry,integrationTestsDir);
		} else if(StringUtils.isNotEmpty(coveragePath)) {
			File coverageFile=new File(coveragePath);
			readFile(registry,coverageFile);
		}
		logInfo("Done");
	}
	
	private void readFile(SonarCoverage registry,File coverageFile) {
		File xmlFile= getCoverageXmlFile(coverageFile);
		coverageParser.parse(registry, xmlFile);
	}

	private void readFilesFromDir(SonarCoverage registry,File integrationTestsDir) {
		List<String> artifactNames= microsoftWindowsEnvironment.getArtifactNames();
		coverageParser.setModulesToParse(artifactNames);
		List<File> coverageFiles=convertVsTestCoverageFilesToXml(integrationTestsDir);
		for(File coverageFile:coverageFiles) { 
			SonarCoverage currentsolutionCoverage=new SonarCoverage();
			coverageParser.parse(currentsolutionCoverage,coverageFile);
			registry.merge(currentsolutionCoverage);
		}
	}

	private List<File> convertVsTestCoverageFilesToXml(File integrationTestsDir) {
		List<File> xmlFiles = new ArrayList<File>();
		Collection<File> files=FileUtils.listFiles(integrationTestsDir,new String[] {"coverage"} ,true);
		for(File coverageFile:files) {
			File xmlFile=getCoverageXmlFile(coverageFile);
			xmlFiles.add(xmlFile);
		}
		return xmlFiles;
	}

	private File getCoverageXmlFile(File coverageFile) {
		File xmlFile;
		String coveragePath=coverageFile.getName();
		if (coveragePath.endsWith(".coverage")) {
			xmlFile = coverageToXmlConverter.convertIfNeeded(coverageFile);

		} else if (coveragePath.endsWith(".xml")) {
			xmlFile = new File(coveragePath);
		} else {
			throw new SonarException("Invalid coverage format " + coveragePath);
		}
		return xmlFile;
	}

	private void logInfo(String string) {
		LOG.info("IntegrationTestCoverSensor: " + string);
	}

}
