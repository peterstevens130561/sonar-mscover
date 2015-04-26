package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import static org.junit.Assert.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;

public class VsTestLinesObserverTest extends ObserverTest {
	private VsTestCoverageObserver observer;
	private SonarCoverage registry;
	private XmlParserSubject parser;
	private Element methodElement;
	@Before
	public void before() {
		observer = new LinesObserver();
		registry = new SonarCoverage();
		observer.setVsTestRegistry(registry);
		parser = new CoverageParserSubject();
		parser.registerObserver(observer);
	}
	
	@Test
	public void parseOneCoveredLine() throws ParserConfigurationException, TransformerException {
		createCoverage();
		addLine("37","0");
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
		addLine("37","1");
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
		addLine("37","1");
		addLine("38","0");
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

	private void createModuleToMethod() {
		Element moduleElement = doc.createElement("Module");
		rootElement.appendChild(moduleElement);
		Element namespaceTable = doc.createElement("NamespaceTable");
		moduleElement.appendChild( namespaceTable);
		Element classElement = doc.createElement("Class");
		namespaceTable.appendChild(classElement);
		methodElement = doc.createElement("Method");
		classElement.appendChild(methodElement);
	}

	private void addLine(String line,String coverage) {
		Element linesElement = doc.createElement("Lines");
		addLineElement(linesElement,"LnStart",line);
		addLineElement(linesElement,"ColStart","9");
		addLineElement(linesElement,"LnEnd","37");
		addLineElement(linesElement,"ColEnd","10");
		addLineElement(linesElement,"Coverage",coverage);
		addLineElement(linesElement,"SourceFileID","8");
		addLineElement(linesElement,"FileID","10");
		methodElement.appendChild(linesElement);
	}

	private void addLineElement(Element linesElement,String name, String value) {
		Element lineElement = doc.createElement(name);
		lineElement.setTextContent(value);
		linesElement.appendChild(lineElement);
	}
			
}
