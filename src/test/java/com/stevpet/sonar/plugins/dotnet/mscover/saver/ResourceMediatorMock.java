package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.File;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;

public class ResourceMediatorMock extends GenericClassMock<DefaultResourceMediator> {
    public ResourceMediatorMock() {
        super(DefaultResourceMediator.class);
    }

    public void givenSonarResource(ResourceSeam seam) {
        when(instance.getSonarResource(any(SensorContext.class),any(Project.class),any(File.class))).thenReturn(seam);
    }
}
