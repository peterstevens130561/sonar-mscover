package com.stevpet.sonar.plugins.dotnet.mscover.vstest.testesultsbuilder;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.VsTestCoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser.ObserverTest;


public class VsTestMethodObserverTest extends ObserverTest {
	private static final String CLASS_NAME = "class";
	private static final String NAMESPACE_NAME = "namespace";
	private static final String MODULE_NAME = "module.dll";
	private static final String FILEID = "8";
	private VsTestMethodObserver observer;
	private XmlParserSubject parser;
	private MethodToSourceFileIdMap methodToSourceFileIdMap;
	@Before
	public void before() {
		observer = new VsTestMethodObserver();
		methodToSourceFileIdMap = new MethodToSourceFileIdMap();
		observer.setRegistry(methodToSourceFileIdMap);
		parser = new VsTestCoverageParserSubject();
		parser.registerObserver(observer);		
	}
	
	@Test
	public void emptyCoverageFile_EmptyMap() {
		this.createNewDoc();
		parser.parseString(docToString());
		assertEquals("expect empty map",0,methodToSourceFileIdMap.size());
	}
	
	@Test
	public void oneSimpleMethod_InMap() {
		this.createNewDoc();
		createModule(MODULE_NAME);
		createNamespaceTable(NAMESPACE_NAME);
		createClass(CLASS_NAME);
		createMethod("method(int32,string)");
		addLine(FILEID,"37","1");
		String doc=docToString();
		parser.parseString(doc);
		assertEquals("element in map",1,methodToSourceFileIdMap.size());
	}
	
	@Test
	public void oneSimpleMethod_CheckMapping() {
		this.createNewDoc();
		createModule(MODULE_NAME);
		createNamespaceTable(NAMESPACE_NAME);
		createClass(CLASS_NAME);
		createMethod("method(int32,string)");
		addLine(FILEID,"37","1");
		String doc=docToString();
		parser.parseString(doc);
		MethodId expectedId=new MethodId(MODULE_NAME,NAMESPACE_NAME,CLASS_NAME,"method");
		String actualFileId=methodToSourceFileIdMap.get(expectedId);
		assertEquals("fileId of method",FILEID,actualFileId);
	}
	
	@Test
	public void oneSimpleMethodTwoLines_OneMethod() {
		this.createNewDoc();
		createModule(MODULE_NAME);
		createNamespaceTable(NAMESPACE_NAME);
		createClass(CLASS_NAME);
		createMethod("method(int32,string)");
		addLine(FILEID,"37","1");
		addLine(FILEID,"38","1");
		String doc=docToString();
		parser.parseString(doc);
		assertEquals("element in map",1,methodToSourceFileIdMap.size());
	}
}
