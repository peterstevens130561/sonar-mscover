package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import static org.junit.Assert.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.VsTestLinesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.VsTestCoverageObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.VsTestCoverageParserSubject;

public class VsTestLinesObserverTest extends VsTestObserverTest {
	private VsTestCoverageObserver observer;
	private SonarCoverage registry;
	private XmlParserSubject parser;
	@Before
	public void before() {
		observer = new VsTestLinesObserver();
		registry = new SonarCoverage();
		observer.setVsTestRegistry(registry);
		parser = new VsTestCoverageParserSubject();
		parser.registerObserver(observer);
	}
	
	@Test
	public void parseOneCoveredLine() throws ParserConfigurationException, TransformerException {
		createCoverage();
		addLine("8","37","0");
		String coverageDoc=docToString();
		parser.parseString(coverageDoc);
		SonarFileCoverage fileCoverage=registry.getCoveredFile("8");
		CoverageLinePoints linePoints=fileCoverage.getLinePoints();
		assertEquals(1,linePoints.size());
		assertEquals(37,linePoints.getPoints().get(0).getLine());
		assertEquals(1,linePoints.getPoints().get(0).getCovered());
	}
	
	@Test
	public void parseOneUnCoveredLine() throws ParserConfigurationException, TransformerException {
		createCoverage();
		addLine("8","37","1");
		String coverageDoc=docToString();
		parser.parseString(coverageDoc);
		SonarFileCoverage fileCoverage=registry.getCoveredFile("8");
		CoverageLinePoints linePoints=fileCoverage.getLinePoints();
		assertEquals(1,linePoints.size());
		assertEquals(37,linePoints.getPoints().get(0).getLine());
		assertEquals(0,linePoints.getPoints().get(0).getCovered());
	}
	
	@Test
	public void parseTwoLines() throws ParserConfigurationException, TransformerException {
		createCoverage();
		addLine("8","37","1");
		addLine("8","38","0");
		String coverageDoc=docToString();
		parser.parseString(coverageDoc);
		SonarFileCoverage fileCoverage=registry.getCoveredFile("8");
		CoverageLinePoints linePoints=fileCoverage.getLinePoints();
		assertEquals(2,linePoints.size());
	}
				
	private void createCoverage() throws ParserConfigurationException, TransformerException {
			createNewDoc();
			createModuleToMethod();
	}
			
}