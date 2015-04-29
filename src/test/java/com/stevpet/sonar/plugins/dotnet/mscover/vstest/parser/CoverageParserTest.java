package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.codehaus.plexus.util.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageReaderStep;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.DefaultCoverageReader;

public class CoverageParserTest extends ObserverTest {

	private SonarCoverage sonarCoverage;
	private CoverageParser coverageParser ;
	private File coverageFile;
	
	@Before
	public void before() {
		coverageParser = new VsTestCoverageParser();
		sonarCoverage = new SonarCoverage();
		String tmpPath=System.getenv("TMP");
		File parentDir=new File(tmpPath);
		coverageFile=FileUtils.createTempFile("coverage", ".xml", parentDir);
	}
	
	@After
	public void after() {
		FileUtils.fileDelete(coverageFile.getAbsolutePath());
	}
	
	@Test
	public void emptyCoverageFile_EmptyCoverage() throws TransformerConfigurationException, IOException, TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException {
		createCoverage();
		FileUtils.fileAppend(coverageFile.getAbsolutePath(), docToString());
		coverageParser.parser(sonarCoverage, coverageFile);
		// should be empty
		assertEquals("no coverage info expected",0,sonarCoverage.getValues().size());
	}
	
	
	
	@Test
	public void OneLiner_ExpectInCoverage() throws TransformerConfigurationException, IOException, TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException {
		createOneFileCoverage();
		FileUtils.fileAppend(coverageFile.getAbsolutePath(), docToString());
		coverageParser.parser(sonarCoverage, coverageFile);
		// should be empty
		assertEquals("one file expected",1,sonarCoverage.getValues().size());
		SonarFileCoverage fileCoverage=sonarCoverage.getCoveredFile("1");
		assertEquals("filename","file1",fileCoverage.getAbsolutePath());
		CoverageLinePoint linePoint=fileCoverage.getLinePoints().getPoints().get(0);
		assertEquals("line",20,linePoint.getLine());
		
	}
	private void createCoverage() {
		createNewDoc();
	}
	
	private void createOneFileCoverage()  {
		createNewDoc();

		createModuleToMethod();
		addLine("1","20","0");
		createFileName("file1","1");

		
	}
}
