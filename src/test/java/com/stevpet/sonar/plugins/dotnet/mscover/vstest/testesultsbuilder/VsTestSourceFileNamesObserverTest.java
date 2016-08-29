package com.stevpet.sonar.plugins.dotnet.mscover.vstest.testesultsbuilder;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.VsTestCoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser.VsTestObserverTest;

public class VsTestSourceFileNamesObserverTest extends VsTestObserverTest {
	private XmlParserSubject parser;
	private SourceFileNameTable sourceFileNameTable;
	
	@Before
	public void before() {
		VsTestSourceFileNamesObserver observer = new VsTestSourceFileNamesObserver();
		parser = new XmlParserSubject();
		parser.registerObserver(observer);
		sourceFileNameTable = new SourceFileNameTable();
		observer.setRegistry(sourceFileNameTable);
	}
	
	@Test
	public void emptyCoverage_NoElementsInTable() {
		createNewDoc();
		String doc = docToString();
		parser.parseString(doc);
		assertEquals("elements",0,sourceFileNameTable.size());
	}
	
	@Test
	public void oneFile_OneElementInTable() {
		createOneFile();
		assertEquals("elements",1,sourceFileNameTable.size());		
	}
	
	@Test
	public void twoFiles_TwoElementsInTable() {
		createNewDoc();
		createFileName("file1", "1");
		createFileName("file2", "10");
		String doc = docToString();
		parser.parseString(doc);
		assertEquals("elements",2,sourceFileNameTable.size());		
	}
	@Test
	public void oneFile_ShouldBeRightElement() {
		createOneFile();
		assertEquals("expect name","file1",sourceFileNameTable.getSourceFileName("1"));
	}

	private void createOneFile() {
		createNewDoc();
		createFileName("file1", "1");
		String doc = docToString();
		parser.parseString(doc);
	}
}
