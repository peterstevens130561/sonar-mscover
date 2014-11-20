package com.stevpet.sonar.plugins.dotnet.mscover.resourcemediator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.DefaultResourceMediatorFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.testutils.DummyFileSystem;

public class CreateWithFilters {
    private ResourceMediatorFactory resourceMediatorFactory = new DefaultResourceMediatorFactory();

    @Test
    public void createShouldWork() {
        SensorContext sensorContext = mock(SensorContext.class);
        Project project = mock(Project.class);
        when(project.getFileSystem()).thenReturn(new DummyFileSystem());
        TimeMachine timeMachine = mock(TimeMachine.class);
        Settings settings = mock(Settings.class);
        MsCoverProperties propertiesHelper = PropertiesHelper.create(settings);
        ResourceMediator resourceMediator = resourceMediatorFactory.createWithFilters(sensorContext, project, timeMachine, propertiesHelper);
        assertNotNull(resourceMediator);
    }

}
