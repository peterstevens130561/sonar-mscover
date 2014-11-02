package com.stevpet.sonar.plugins.dotnet.mscover.seams.resources;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.PersistenceMode;

import static org.mockito.Mockito.verify;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class ResourceSeamMock extends GenericClassMock<ResourceSeam> {

    public ResourceSeamMock() {
        super(ResourceSeam.class);
    }

    public void verifySaveMetricValue(Metric metric, double value) {
        verify(instance).saveMetricValue(metric, value);
    }

    public void verifySaveMeasure(Metric metric, String data) {
       Measure measure = new Measure(metric, data);
       measure.setPersistenceMode(PersistenceMode.DATABASE);
       verify(instance).saveMeasure(measure);
    }

}
