package com.stevpet.sonar.plugins.dotnet.mscover.seams.resources;

import org.sonar.api.measures.Metric;
import static org.mockito.Mockito.verify;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class ResourceSeamMock extends GenericClassMock<ResourceSeam> {

    public ResourceSeamMock() {
        super(ResourceSeam.class);
    }

    public void verifySaveMetricValue(Metric metric, double value) {
        verify(instance).saveMetricValue(metric, value);
    }

}
