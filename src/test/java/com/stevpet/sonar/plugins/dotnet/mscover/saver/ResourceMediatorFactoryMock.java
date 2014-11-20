package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
public class ResourceMediatorFactoryMock extends GenericClassMock<ResourceMediatorFactory> {
    public ResourceMediatorFactoryMock() {
        super(ResourceMediatorFactory.class);
    }

    public void onCreate(ResourceMediatorMock resourceMediatorMock) {
        // TODO Auto-generated method stub
        when(instance.createWithFilters(any(SensorContext.class),any(Project.class), any(TimeMachine.class), any(MsCoverProperties.class))).thenReturn(resourceMediatorMock.getMock());
    }
}
