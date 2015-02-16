package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.coverageparserfactory;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;
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
        VsTestCoverageRegistry registry = new VsTestCoverageRegistry("C:\\users\\stevpet\\GitHub");
        List<String> modules = new ArrayList<String>();
        XmlParserSubject parser = factory.createCoverageParser(registry,modules);
        
        File file=TestUtils.getResource("mscoverage.xml");
        //Act
        parser.parseFile(file);
        //Assert
        assertEquals(8,registry.getSourceFileNameTable().values().size());
    }

}
