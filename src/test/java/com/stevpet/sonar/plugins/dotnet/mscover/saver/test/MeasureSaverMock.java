package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class MeasureSaverMock extends GenericClassMock<MeasureSaver> {

    public MeasureSaverMock() {
        super(MeasureSaver.class);
    }

    public void verifySaveProjectSummary(Metric tests, double value) {
        verify(instance,times(1)).saveSummaryMeasure(tests, value);
    }

    public void verifySaveFileMeasure(int frequency,Measure measure) {
        verify(instance,times(frequency)).saveFileMeasure(measure);
    }

    public void verifyInvokedSaveFileMeasure(int i, Metric metric) {
        verify(instance,times(i)).saveFileMeasure(eq(metric), anyDouble());
    }
    

}
