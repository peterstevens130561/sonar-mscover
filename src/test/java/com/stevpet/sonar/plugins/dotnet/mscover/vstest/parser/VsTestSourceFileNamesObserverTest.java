package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import static org.junit.Assert.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;

public class VsTestSourceFileNamesObserverTest extends ObserverTest {
	private VsTestCoverageObserver observer;
	private SonarCoverage registry;
	private XmlParserSubject parser;
	private Element rootElement;

	@Before
	public void before() {
		observer = new FileNamesObserver();
		registry = new SonarCoverage();
		observer.setVsTestRegistry(registry);
		parser = new CoverageParserSubject();
		parser.registerObserver(observer);
	}
	
	@Test
	public void noFileNames() throws ParserConfigurationException, TransformerException {
		createCoverage();
		String coverageDoc=docToString();
		parser.parseString(coverageDoc);
		assertNotNull(registry.getValues());
		assertEquals("no elements expected",0,registry.getValues().size());
	}
	@Test
	public void oneFileName() throws ParserConfigurationException, TransformerException {
		createCoverage();
		createFileName("first","1");
		String coverageDoc=docToString();
		parser.parseString(coverageDoc);
		assertNotNull(registry.getValues());
		assertEquals("one element expected",1,registry.getValues().size());	
		assertEquals("expect file","first",registry.getCoveredFile("1").getAbsolutePath());
	}
	
	@Test
	public void twoFileNames() throws ParserConfigurationException, TransformerException {
		createCoverage();
		createFileName("first","1");
		createFileName("second","10");
		String coverageDoc=docToString();
		parser.parseString(coverageDoc);
		assertNotNull(registry.getValues());
		assertEquals("two elements expected",2,registry.getValues().size());
		assertEquals("expect file","first",registry.getCoveredFile("1").getAbsolutePath());
		assertEquals("expect file","second",registry.getCoveredFile("10").getAbsolutePath());
	}
	
	private void createCoverage() throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.newDocument();
		rootElement = doc.createElement("CoverageDSPriv");
		doc.appendChild(rootElement);
	}
	
	private void createFileName(String name, String id) {
		Element fileNamesElement = doc.createElement("SourceFileNames");
		rootElement.appendChild(fileNamesElement);
		Element sourceFileIdElement = doc.createElement("SourceFileID");
		sourceFileIdElement.setTextContent(id);
		fileNamesElement.appendChild(sourceFileIdElement);
		Element sourceFileNameElement =doc.createElement("SourceFileName");
		sourceFileNameElement.setTextContent(name);
		fileNamesElement.appendChild(sourceFileNameElement);
	}
}

