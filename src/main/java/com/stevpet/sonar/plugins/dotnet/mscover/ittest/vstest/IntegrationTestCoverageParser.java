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
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageParserStep;

public class IntegrationTestCoverageParser implements CoverageParserStep {
	private final static Logger LOG = LoggerFactory.getLogger(IntegrationTestCoverageParser.class);
	private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
	private MsCoverProperties msCoverProperties;
	private FileSystem fileSystem;
	private CommandLineExecutor commandLineExecutor;
	private CodeCoverageCommand convertCoverageToXmlCommand;
	public IntegrationTestCoverageParser(MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			MsCoverProperties msCoverProperties,
			FileSystem fileSystem,
			CommandLineExecutor commandLineExecutor,
			CodeCoverageCommand codeCoverageCommand) {
		this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
		this.msCoverProperties=msCoverProperties;
		this.fileSystem=fileSystem;
		this.commandLineExecutor = commandLineExecutor;
		this.convertCoverageToXmlCommand = codeCoverageCommand;
	}
	/**
	 * @param file may be either a single file to parse, or a directory which holds files
	 */
	@Override
	public void parse(SonarCoverage registry, File file) {
	     List<String> artifactNames = microsoftWindowsEnvironment.getArtifactNames();
		String integrationTestsDir=msCoverProperties.getIntegrationTestsDir();
		if(StringUtils.isNotEmpty(integrationTestsDir)) {
			logInfo("will take coverage data from directory:" + integrationTestsDir);
			List<File> xmlFiles=convertVsTestCoverageFilesToXml(integrationTestsDir);
			//coverageHelper.analyse(project,xmlFiles,artifactNames);
		} else {
			String xmlPath = getCoverageXmlPath();
			//coverageHelper.analyse(project, xmlPath,artifactNames); 
			return;
		}
		logInfo("Done");

	}
	
	 private void tryAnalyse(String coveragePath)
	            throws XMLStreamException, IOException {
	        LOG.info("MsCoverPlugin : name=" + project.getName());
	        String projectDirectory = fileSystem.baseDir().getAbsolutePath();
	        LOG.info("MsCoverPlugin : directory=" + projectDirectory);
	        File file = getCoverageFile(coveragePath);
	        List<File> coverageFiles = new ArrayList<File>();
	        coverageFiles.add(file);
	        tryAnalyseFiles(coverageFiles);     
	    }
	    private void tryAnalyseFiles(List<File> coverageFiles)
	            throws XMLStreamException, IOException {
	        String projectDirectory = fileSystem.baseDir().getAbsolutePath();
	        VsTestCoverageRegistry aggregatedSolutionCoverage=new VsTestCoverageRegistry(projectDirectory);
	        for(File coverageFile:coverageFiles) { 
	            VsTestCoverageRegistry currentsolutionCoverage=new VsTestCoverageRegistry(projectDirectory);
	            invokeParserSubject(currentsolutionCoverage,coverageFile);
	            aggregatedSolutionCoverage.merge(currentsolutionCoverage);
	        }
	        
	        saveLineMeasures(aggregatedSolutionCoverage.getSolutionLineCoverageData());
	        
	    }
	   private List<File> convertVsTestCoverageFilesToXml(String integrationTestsDir) {
	        List<File> xmlFiles = new ArrayList<File>();
	        Collection<File> files=FileUtils.listFiles(new File(integrationTestsDir),new String[] {"coverage"} ,true);
	        for(File file:files) {
	            String xmlPath=transformIfNeeded(file.getAbsolutePath());
	            xmlFiles.add(new File(xmlPath));
	        }
	        return xmlFiles;
	    }

	    private String getCoverageXmlPath() {
	        String coveragePath = msCoverProperties.getIntegrationTestsPath();
	        String xmlPath;
	        if (coveragePath.endsWith(".coverage")) {
	            xmlPath = transformIfNeeded(coveragePath);
	        } else if (coveragePath.endsWith(".xml")) {
	            xmlPath = coveragePath;
	        } else {
	            throw new SonarException("Invalid coverage format " + coveragePath);
	        }
	        return xmlPath;
	    }


	    private String transformIfNeeded(String coveragePath) {
	        String xmlPath;
	        xmlPath = coveragePath.replace(".coverage", ".xml");
	        if (transformationNeeded(xmlPath, coveragePath)) {
	            convertCoverageToXmlCommand.setSonarPath(fileSystem.workDir().getAbsolutePath());
	            convertCoverageToXmlCommand.setCoveragePath(coveragePath);
	            convertCoverageToXmlCommand.setOutputPath(xmlPath);
	            convertCoverageToXmlCommand.install();
	            LOG.info("IntegrationCoverSensor: creating .xml file");
	            int exitCode = commandLineExecutor.execute(convertCoverageToXmlCommand);
	            if (exitCode != 0) {
	                throw new SonarException("failed");
	            }
	        } else {
	            LOG.info("Reusing xml file, as it is newer than the .coverage file");
	        }
	        return xmlPath;
	    }


	    private boolean transformationNeeded(String xmlPath,String coveragePath) {
	        File xmlFile = new File(xmlPath);
	        File coverageFile = new File(coveragePath);
	        return !xmlFile.exists() || FileUtils.isFileNewer(coverageFile, xmlFile);

	    }
	    
	    private void logInfo(String string) {
	        LOG.info("IntegrationTestCoverSensor: " + string);
	    }


}
