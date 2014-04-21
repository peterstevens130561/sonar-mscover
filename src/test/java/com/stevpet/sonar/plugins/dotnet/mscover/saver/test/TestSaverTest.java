package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.BaseSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testutils.DummyFileSystem;

public class TestSaverTest {
    
    SensorContext context ;
    Project project ;
    @Before
    public void before() {
        context = mock(SensorContext.class);
        project = mock(Project.class);
        when(project.getFileSystem()).thenReturn(new DummyFileSystem());
    }
    @Test
    public void create_ShouldWork() {
        BaseSaver testSaver = new TestSaver(context,project) ;
        Assert.assertNotNull(testSaver);
}
    @Test
    public void emptyRegistry_NoInvocation() {
        TestSaver testSaver = new TestSaver(context,project) ;   
        UnitTestFilesResultRegistry unitTestFilesResultRegistry = new UnitTestFilesResultRegistry();

        testSaver.setUnitTestFilesResultRegistry(unitTestFilesResultRegistry);
        testSaver.save();
        /* nothing should happen */
    }
    
    public void simpleRegistry_ShouldSave() {
        TestSaver testSaver = new TestSaver(context,project) ;   
        UnitTestFilesResultRegistry unitTestFilesResultRegistry = new UnitTestFilesResultRegistry();
        UnitTestResultRegistry unitTestRegistry = new UnitTestResultRegistry();
        UnitTestResultModel unitTestResult = new UnitTestResultModel();
        unitTestRegistry.add(unitTestResult);
        MethodToSourceFileIdMap map = new MethodToSourceFileIdMap();
        unitTestFilesResultRegistry.mapResults(unitTestRegistry, map);
        testSaver.setUnitTestFilesResultRegistry(unitTestFilesResultRegistry);
        testSaver.save();
    }
}
