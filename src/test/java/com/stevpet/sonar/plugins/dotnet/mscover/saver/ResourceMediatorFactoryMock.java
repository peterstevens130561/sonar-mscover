package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import org.sonar.api.batch.TimeMachine;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
public class ResourceMediatorFactoryMock extends GenericClassMock<ResourceMediatorFactory> {
    public ResourceMediatorFactoryMock() {
        super(ResourceMediatorFactory.class);
    }

    public void onCreate(ResourceMediatorMock resourceMediatorMock) {
        when(instance.createWithFilters(any(TimeMachine.class), any(MsCoverProperties.class))).thenReturn(resourceMediatorMock.getMock());
    }
}
