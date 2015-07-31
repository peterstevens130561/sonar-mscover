package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.test.TestUtils;

import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverFileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

/**
 * Integration test!
 * @author stevpet
 *
 */
public class OpenCoverCoverageParserTest {
    private OpenCoverCoverageParser coverageParser ;
    private OpenCoverFileNamesParser fileNamesParser;
    @Mock private MsCoverConfiguration configuration;
    private SonarCoverage registry;
    
    @Before
    public void before() {
        initMocks(this);
        coverageParser = new OpenCoverCoverageParser(configuration);
        fileNamesParser = new OpenCoverFileNamesParser();
        registry = new SonarCoverage();
        
    }
    
    @Test
    public void readCoverageMixedCSharpCppFile() {
        File mixedFile=TestUtils.getResource("OpenCoverCoverageParser/coverage-report.xml");
        assertNotNull("file is in resources",mixedFile);
        coverageParser.parse(registry, mixedFile);
    }
    
    @Test
    public void readFileNAmesMixedCSharpCppFile() {
        File mixedFile=TestUtils.getResource("OpenCoverCoverageParser/coverage-report.xml");
        assertNotNull("file is in resources",mixedFile);
        fileNamesParser.parse(mixedFile);
        SourceFileNameTable table = fileNamesParser.getSourceFileNamesTable();
        MethodToSourceFileIdMap map = fileNamesParser.getMethodToSourceFileIdMap();
        assertNotNull(table);
        assertNotNull(map);
        
    }
}
