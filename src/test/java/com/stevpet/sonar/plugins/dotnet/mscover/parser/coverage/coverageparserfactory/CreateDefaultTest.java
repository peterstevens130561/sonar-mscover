package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.coverageparserfactory;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.ConcreteParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.ParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.FileBlocksRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;

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
        ParserFactory factory = new ConcreteParserFactory();
        FileBlocksRegistry fileBlocksRegistry = new FileBlocksRegistry();
        SourceFileNamesRegistry sourceFileNamesRegistry = new SourceFileNamesRegistry();
        ParserSubject parser = factory.createCoverageParser(fileBlocksRegistry, sourceFileNamesRegistry);
        
        File file=TestUtils.getResource("mscoverage.xml");
        //Act
        parser.parseFile(file);
        //Assert
        assertEquals(8,fileBlocksRegistry.values().size());
        assertEquals(8,sourceFileNamesRegistry.values().size());
    }

}
