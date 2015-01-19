package com.stevpet.sonar.plugins.dotnet.mscover.resourcemediator;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.DefaultResourceMediatorFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;

public class CreateWithFiltersTest {
    private ResourceMediatorFactory resourceMediatorFactory = new DefaultResourceMediatorFactory();

    @Test
    public void createShouldWork() {
        SensorContext sensorContext = mock(SensorContext.class);
        Project project = mock(Project.class);
        FileSystemMock fileSystemMock=new FileSystemMock();
        TimeMachine timeMachine = mock(TimeMachine.class);
        Settings settings = mock(Settings.class);
        MsCoverProperties propertiesHelper = PropertiesHelper.create(settings);
        
        fileSystemMock.givenDefaultEncoding();
        ResourceMediator resourceMediator = resourceMediatorFactory.createWithFilters(sensorContext, project, timeMachine, propertiesHelper,fileSystemMock.getMock());
        assertNotNull(resourceMediator);
    }

}
