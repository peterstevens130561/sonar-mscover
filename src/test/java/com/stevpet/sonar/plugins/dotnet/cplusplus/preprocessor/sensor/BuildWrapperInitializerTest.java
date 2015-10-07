package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyString;
import static org.mockito.MockitoAnnotations.initMocks;

import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;

public class BuildWrapperInitializerTest {
    @Mock private Settings settings;
    @Mock private CommandLineExecutor commandLineExecutor;
    @Mock private BuildWrapperBuilder buildWrapperBuilder;
    @Mock private Project project;
    private BuildWrapperInitializerSpy buildWrapperInitializer;

    @Before
    public void before() {
        initMocks(this);
        BuildWrapperCache buildWrapperCache = new BuildWrapperCache();
        buildWrapperInitializer = new BuildWrapperInitializerSpy(settings, commandLineExecutor, buildWrapperBuilder,buildWrapperCache);
        when(buildWrapperBuilder.setInstallDir(anyString())).thenReturn(buildWrapperBuilder);
        when(buildWrapperBuilder.setMsBuildOptions(anyString())).thenReturn(buildWrapperBuilder);
        when(buildWrapperBuilder.setOutputPath(anyString())).thenReturn(buildWrapperBuilder);
        when(buildWrapperBuilder.setOutputPath(anyString())).thenReturn(buildWrapperBuilder);
        when(buildWrapperBuilder.setBuildWrapperPath(anyString())).thenReturn(buildWrapperBuilder);
    }
    
    @Test
    public void allSignsGreen_shouldExecute() {
        when(settings.getBoolean(BuildWrapperConstants.ENABLED_KEY)).thenReturn(true);
        when(project.isRoot()).thenReturn(false);
        
        boolean execute=buildWrapperInitializer.shouldExecuteOnProject(project);
        assertTrue("should execute on project, as all signs are green...",execute);
    }
    
    @Test
    public void disabled_shouldNotExecute() {
        when(settings.getBoolean(BuildWrapperConstants.ENABLED_KEY)).thenReturn(false);
        when(project.isRoot()).thenReturn(true);
        
        boolean execute=buildWrapperInitializer.shouldExecuteOnProject(project);
        assertFalse("should not execute on project, as it is disabled...",execute);
    }
    
    @Test
    public void notRoot_shouldExecute() {
        when(settings.getBoolean(BuildWrapperConstants.ENABLED_KEY)).thenReturn(true);
        when(project.isRoot()).thenReturn(false);
        
        boolean execute=buildWrapperInitializer.shouldExecuteOnProject(project);
        assertTrue("should not execute on project, as it is not root...",execute);
    } 
    
    @Test
    public void hasNoCppFiles_shouldExecute() {
        when(settings.getBoolean(BuildWrapperConstants.ENABLED_KEY)).thenReturn(true);
        when(project.isRoot()).thenReturn(false);
        buildWrapperInitializer.setHasCppFiles(true);
        
        boolean execute=buildWrapperInitializer.shouldExecuteOnProject(project);
        assertTrue("should execute on project, as it is not root...",execute);
    }    

    @Test
    public void execute_verifySettingsPassed() {
        when(settings.getString(BuildWrapperConstants.OUTDIR_KEY)).thenReturn("outdir");
        when(settings.getString(BuildWrapperConstants.INSTALLDIR_KEY)).thenReturn("installdir");
        when(settings.getString(BuildWrapperConstants.MSBUILD_OPTIONS_KEY)).thenReturn("option");
        
        //When
        buildWrapperInitializer.execute(project);
        
        //then
       String outputPath=new File(".outdir").getAbsolutePath().replaceAll("\\\\","/");
        verify(buildWrapperBuilder,times(1)).setOutputPath(outputPath);
        verify(buildWrapperBuilder,times(1)).setInstallDir("installdir");
        verify(buildWrapperBuilder,times(1)).setMsBuildOptions("option");
        verify(commandLineExecutor,times(1)).execute(buildWrapperBuilder);
        verify(settings,times(1)).setProperty(BuildWrapperConstants.CFAMILY_OUTPUT_KEY, outputPath);

    }
    
    @Test public void missingProperty_ExpectException() {
        when(settings.getString(BuildWrapperConstants.OUTDIR_KEY)).thenReturn(null);
        when(settings.getString(BuildWrapperConstants.INSTALLDIR_KEY)).thenReturn("installdir");
        when(settings.getString(BuildWrapperConstants.MSBUILD_OPTIONS_KEY)).thenReturn("option");
        
        try {
            buildWrapperInitializer.execute(project);           
        } catch ( SonarException e) {
            String msg=e.getMessage();
            assertTrue("should throw exception with reference to missing property",msg.contains("Property not set"));
            return;
        }
        fail("expected exception");
    }
    
    private class BuildWrapperInitializerSpy extends BuildWrapperInitializer  {
        
        private boolean hasCppFiles=true;
        public BuildWrapperInitializerSpy(Settings settings, CommandLineExecutor commandLineExecutor,
                BuildWrapperBuilder buildWrapperBuilder,BuildWrapperCache buildWrapperCache) {
            super(settings, commandLineExecutor, buildWrapperBuilder,buildWrapperCache);
        }

        public void setHasCppFiles(boolean value) {
            this.hasCppFiles=value;
        }
        @Override
        protected boolean hasCppFiles() {
            return hasCppFiles;
        }
    }
}
