package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.test.TestUtils;

import static org.mockito.MockitoAnnotations.initMocks;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.FilteringCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.VsTestFilteringCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.CodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.CoverageToXmlConverter;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.VsTestCoverageToXmlConverter;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class IntegrationTestSerializationTests {

    private SonarCoverage coverageData;
    @Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    @Mock private MsCoverProperties msCoverProperties;
    private FilteringCoverageParser coverageParser;
    private CoverageToXmlConverter coverageToXmlConverter;
    private IntegrationTestCoverageReader coverageReader;
    @Mock private FileSystem fileSystem;
    private CodeCoverageCommand codeCoverageCommand;
    private CommandLineExecutor commandLineExecutor;
    private Serializer serializer;
    private File workDir;
    private File testDir;


    @Before
    public void before() {
        initMocks(this);
        coverageParser= new VsTestFilteringCoverageParser();
        commandLineExecutor = new WindowsCommandLineExecutor();
        codeCoverageCommand = new WindowsCodeCoverageCommand();
        coverageToXmlConverter = new VsTestCoverageToXmlConverter(fileSystem, codeCoverageCommand, commandLineExecutor);
        serializer = new Serializer();
    }
    
    @Test
    public void basicTest() throws IOException {
        givenCoverageFilesInDirectoryRead();
        whenSerializingTheData();
        thenDeserializedDataEquals(coverageData);
    }

    private void thenDeserializedDataEquals(SonarCoverage coverageData) {
        // TODO Auto-generated method stub
        
    }

    private void whenSerializingTheData() throws IOException {
        File serializationFile=new File(workDir,"serialization.ser");
        if(serializationFile.exists()) {
            FileUtils.forceDelete(serializationFile);
        }
        serializer.serialize(serializationFile, coverageData);
        assertTrue("serialization file should exist",serializationFile.exists());
        
    }

    private void whenSerializingTheDataInto(String string) {
        // TODO Auto-generated method stub
        
    }

    private void givenCoverageFilesInDirectoryRead() {
        File testFile=TestUtils.getResource("IntegrationTestSerializationTests/coveragedata/coverage.xml");
        File dummysln=TestUtils.getResource("IntegrationTestSerializationTests/solution/dummy.sln");
        File solutionDir=dummysln.getParentFile();

        workDir = new File(solutionDir,".sonar");
        try {
            FileUtils.forceMkdir(workDir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail("could not create directory");
        }
        assertTrue(".sonar dir should exist",workDir.exists());
        when(fileSystem.workDir()).thenReturn(workDir);
        assertNotNull("could not find test resource",testFile);
        testDir = testFile.getParentFile();
        assertNotNull("could not get parent",testDir);
        coverageData=new SonarCoverage();
        coverageReader=new IntegrationTestCoverageReader(microsoftWindowsEnvironment, msCoverProperties, coverageParser, coverageToXmlConverter);
        when(msCoverProperties.getIntegrationTestsDir()).thenReturn(testDir.getAbsolutePath());
        coverageReader.read(coverageData, testDir);
        assertEquals("expect some files to be read",2,coverageData.getValues().size());
    }
}
