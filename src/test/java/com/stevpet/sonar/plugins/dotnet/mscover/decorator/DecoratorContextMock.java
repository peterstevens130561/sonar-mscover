package com.stevpet.sonar.plugins.dotnet.mscover.decorator;

import static org.mockito.Mockito.when;

import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class DecoratorContextMock extends GenericClassMock<DecoratorContext> {
    public DecoratorContextMock() {
        super(DecoratorContext.class);
    }

    /**
     * create the measure to return when getMeasure is invoked
     * @param metric of the measure
     * @param value of the measure
     */
    public void givenMeasure(Metric metric, double value) {
        Measure measure = new Measure(metric);
        measure.setValue(value);
        givenMeasure(metric,measure);
    }

    public void givenMeasure(Metric metric, Measure measure) {
        when(instance.getMeasure(metric)).thenReturn(measure);  
    } 

}
