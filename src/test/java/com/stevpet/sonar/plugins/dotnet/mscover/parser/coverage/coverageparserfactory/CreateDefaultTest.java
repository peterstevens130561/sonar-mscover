package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.coverageparserfactory;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileCoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.ConcreteVsTestParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.VsTestParserFactory;

public class CreateDefaultTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        //Arrange
        VsTestParserFactory factory = new ConcreteVsTestParserFactory();
        VsTestRegistry registry = new VsTestRegistry("C:\\users\\stevpet\\GitHub");
        XmlParserSubject parser = factory.createCoverageParser(registry);
        
        File file=TestUtils.getResource("mscoverage.xml");
        //Act
        parser.parseFile(file);
        //Assert
        assertEquals(8,registry.getFileBlocksRegistry().values().size());
        assertEquals(8,registry.getSourceFileNamesRegistry().values().size());
    }

}
