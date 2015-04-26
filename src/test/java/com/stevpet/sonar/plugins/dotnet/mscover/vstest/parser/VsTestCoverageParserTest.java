package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.codehaus.plexus.util.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageParserStep;

public class VsTestCoverageParserTest extends ObserverTest {

	private SonarCoverage sonarCoverage;
	private CoverageParserStep coverageParser ;
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
		coverageParser.parse(sonarCoverage, coverageFile);
		// should be empty
		assertEquals("no coverage info expected",0,sonarCoverage.getValues().size());
	}
	
	
	
	@Test
	public void OneLiner_ExpectInCoverage() throws TransformerConfigurationException, IOException, TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException {
		createOneFileCoverage();
		FileUtils.fileAppend(coverageFile.getAbsolutePath(), docToString());
		coverageParser.parse(sonarCoverage, coverageFile);
		// should be empty
		assertEquals("one file expected",0,sonarCoverage.getValues().size());
	}
	private void createCoverage() throws ParserConfigurationException, TransformerException {
		createNewDoc();
		createFileName("file1","1");

	}


	
	private void createOneFileCoverage() {
		createNewDoc();		
	}
}
