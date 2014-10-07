package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results.resultssensor;

import static org.mockito.Mockito.mock ;
import static org.mockito.Mockito.when ;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test ;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesStub;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestUnitTestResultsSensor;
public class ShouldExecuteTest {
    
    private MsCoverPropertiesStub propertiesHelper;
    private Project project ;
    private VsTestUnitTestResultsSensor resultsSensor;
    
    @Before
    public void before() {
        propertiesHelper = new MsCoverPropertiesStub();
        project = mock(Project.class);
        resultsSensor = new VsTestUnitTestResultsSensor(null, propertiesHelper, null, null);
    }
    
    @Test
    public void RunModeIsSkipNotExecuteRootNotProjectIsRootPathEmpty_False() {
        testSkipModeAlwaysFalse(false,false,StringUtils.EMPTY);
  
    }
    @Test
    public void RunModeIsSkipExecuteRootProjectIsRootPathEmpty_False() {
        testSkipModeAlwaysFalse(true,true,StringUtils.EMPTY);
  
    }
     
    @Test
    public void RunModeIsSkipExecuteRootProjectIsRootPathOk_False() {
        testSkipModeAlwaysFalse(true,true,"a/b");
  
    }
    
    @Test
    public void RunModeIsSkipExecuteRootProjectIsNotRootPath_False() {
        testSkipModeAlwaysFalse(true,false,"a/b");
  
    }

    @Test
    public void RunModeIsReUseExecuteRootProjectIsRootPathOk_False() {
        testReuseModeTrue(true,true,"a/b");
    }
    
    @Test
    public void RunModeIsVsTestRootPojectIsRoot() {
        test(true,RunMode.RUNVSTEST,true,true,null);
    }
   
    @Test
    public void RunModeIsVsTestRootProjectIsNotRoot() {
        test(false,RunMode.RUNVSTEST,true,false,null);
    }
    
    @Test
    public void RunModeIsVsTestNotRootProjectIsRoot() {
        test(false,RunMode.RUNVSTEST,false,true,null);
    }
    public void testReuseModeTrue(boolean executeRoot,boolean isRoot,String path) {
        test(true,RunMode.REUSE,executeRoot,isRoot,path);       
    }
    public void testSkipModeAlwaysFalse(boolean executeRoot,boolean isRoot,String path) {
        test(false,RunMode.SKIP,executeRoot,isRoot,path);       
    }
    
    private void test(boolean expected,RunMode runMode,boolean executeRoot,boolean isRoot,String unitTestResultsPath) {
        String runModeValue=runMode.toString();
        propertiesHelper.setRunVsTest(true);
        propertiesHelper.setExecuteRoot(executeRoot);
        propertiesHelper.setUnitTestResultsPath(unitTestResultsPath);
        propertiesHelper.setRunMode(runMode);
        /*
        when(settings.getString(PropertiesHelper.MSCOVER_COVERAGETOOL)).thenReturn("vstest");
        when(settings.getString(PropertiesHelper.MSCOVER_MODE)).thenReturn(runModeValue);

        when(settings.getBoolean(PropertiesHelper.MSCOVER_EXECUTEROOT)).thenReturn(executeRoot);

        
        when(propertiesHelper.excuteRoot()).thenReturn(executeRoot);
        when(propertiesHelper.getMode()).thenReturn(runModeValue);
        when(propertiesHelper.getUnitTestResultsPath()).thenReturn(path);
        */
                when(project.isRoot()).thenReturn(isRoot);
        boolean shouldExecute = resultsSensor.shouldExecuteOnProject(project);
        Assert.assertEquals(expected,shouldExecute);      
    }
    
}
