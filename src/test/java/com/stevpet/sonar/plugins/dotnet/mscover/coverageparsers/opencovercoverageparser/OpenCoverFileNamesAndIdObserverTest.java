package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.api.parser.ParserSubject;
import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameRow;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;

public class OpenCoverFileNamesAndIdObserverTest {

	private final OpenCoverFileNamesAndIdObserver observer = new OpenCoverFileNamesAndIdObserver();
	private SourceFileNameTable registry = new SourceFileNameTable();
	
	@Before
	public void before() {
		XmlParserSubject parser = new XmlParserSubject();
		parser.registerObserver(observer);
		observer.setRegistry(registry);
		File xmlFile = TestUtils.getResource("observers/OpenCoverFileNamesAndIdObserver.xml");
		parser.parseFile(xmlFile);
	}
	
	@Test
	public void checkFileId() {
		assertEquals("FileId",84,observer.getUid());	
	}
	
	@Test
	public void checkFileName() {
		assertEquals("FileName","c:\\Development\\bogus.cs",observer.getFileName());
	}
	
	@Test
	public void checkRegistry() {
		SourceFileNameRow row = registry.get(84);
		assertNotNull(row);
		assertEquals("c:\\Development\\bogus.cs",row.getSourceFileName());
		assertEquals(84,row.getSourceFileID());
	}
}
