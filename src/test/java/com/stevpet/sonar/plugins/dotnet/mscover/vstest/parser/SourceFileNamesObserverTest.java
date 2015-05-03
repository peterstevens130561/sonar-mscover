package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import static org.junit.Assert.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;

public class SourceFileNamesObserverTest extends ObserverTest {
	private VsTestCoverageObserver observer;
	private SonarCoverage registry;
	private XmlParserSubject parser;

	@Before
	public void before() {
		observer = new FileNamesObserver();
		registry = new SonarCoverage();
		observer.setVsTestRegistry(registry);
		parser = new CoverageParserSubject();
		parser.registerObserver(observer);
	}
	
	@Test
	public void noFileNames() {
		createNewDoc();
		String coverageDoc=docToString();
		parser.parseString(coverageDoc);
		assertNotNull(registry.getValues());
		assertEquals("no elements expected",0,registry.getValues().size());
	}
	@Test
	public void oneFileName() throws ParserConfigurationException, TransformerException {
		createNewDoc();
		createFileName("first","1");
		String coverageDoc=docToString();
		//filenames will only be registered if the lines have been loaded
		registry.getCoveredFile("1");
		parser.parseString(coverageDoc);
		assertNotNull(registry.getValues());
		assertEquals("one element expected",1,registry.getValues().size());	
		assertEquals("expect file","first",registry.getCoveredFile("1").getAbsolutePath());
	}
	
	@Test
	public void twoFileNames() throws ParserConfigurationException, TransformerException {
		createNewDoc();
		createFileName("first","1");
		createFileName("second","10");
		String coverageDoc=docToString();
		// filenames will only be registered if the lines have been loaded
		registry.getCoveredFile("1");
		registry.getCoveredFile("10");
		parser.parseString(coverageDoc);
		assertNotNull(registry.getValues());
		assertEquals("two elements expected",2,registry.getValues().size());
		assertEquals("expect file","first",registry.getCoveredFile("1").getAbsolutePath());
		assertEquals("expect file","second",registry.getCoveredFile("10").getAbsolutePath());
	}
	
}

