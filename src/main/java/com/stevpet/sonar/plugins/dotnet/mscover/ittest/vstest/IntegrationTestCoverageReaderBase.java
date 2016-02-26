package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.commandexecutor.ProcessLock;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.FilteringCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class IntegrationTestCoverageReaderBase implements
		CoverageReader {
	private final static Logger LOG = LoggerFactory
			.getLogger(IntegrationTestCoverageReaderBase.class);
	
	private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
	private FilteringCoverageParser coverageParser;
	private ProcessLock processLock;
	private List<ParserThread> threads = new ArrayList<>();
    private MsCoverConfiguration msCoverConfiguration;

	public IntegrationTestCoverageReaderBase(
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			FilteringCoverageParser coverageParser,
			ProcessLock processLock) {
		this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
		this.coverageParser=coverageParser;
		this.processLock = processLock;
	}

	/**
	 * 
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest.
	 * IntegrationTestsCoverageReader
	 * #read(com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage)
	 */

	@Override
	public void read(@Nonnull SonarCoverage registry,
			@Nonnull File coverageRoot) {
		processLock.lock("SonarCoverage");
		try {
			if (coverageRoot.isDirectory()) {
				readFilesFromDir(registry, coverageRoot);
			} else {
				coverageParser.parse(registry, coverageRoot);
			}
		} finally {
			processLock.release();
		}
	}

	private void readFilesFromDir(SonarCoverage registry,
			File integrationTestsDir) {

		List<String> artifactNames = microsoftWindowsEnvironment
				.getArtifactNames();
		coverageParser.setModulesToParse(artifactNames);
		Collection<File> coverageFiles=FileUtils.listFiles(integrationTestsDir,  new String[]{"xml"}, true);
		for (File coverageFile : coverageFiles)  {
			parseFile(registry,coverageFile);
		}
		for(ParserThread t : threads) {
		    try {
                t.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		    LOG.info("Joined",t.getName());
		}

	}
	

	public void setMsCoverConfiguration(MsCoverConfiguration msCoverConfiguration) {
	    this.msCoverConfiguration=msCoverConfiguration;
	}

	
    public void parseFile(SonarCoverage registry,File coverageFile) {
        SonarCoverage sonarCoverage = new SonarCoverage();
        CoverageFileParser coverageFileParser = new CoverageFileParser(msCoverConfiguration);
        coverageFileParser.setCoverage(sonarCoverage);
        coverageFileParser.setCoverageFile(coverageFile);
        coverageFileParser.setMergeDestination(registry);
        String threadName="CoverageFileParser" + coverageFile.getName();
        LOG.info("Started " + threadName);
        ParserThread t = new ParserThread(coverageFileParser,threadName);
        threads.add(t);
        t.start();
    }

}
