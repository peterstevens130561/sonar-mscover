package com.stevpet.sonar.plugins.dotnet.mscover.sonarseams;

import java.io.File;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;

public class InjectedMeasureSaver implements BatchExtension, MeasureSaver {

    private MeasureSaver measureSaver;
    public InjectedMeasureSaver(ResourceMediator resourceMediator) {
        measureSaver =SonarMeasureSaver.create(resourceMediator);
    }
    @Override
    public void setFile(File file) {
        measureSaver.setFile(file);

    }

    @Override
    public void saveFileMeasure(Measure measure) {
        measureSaver.saveFileMeasure(measure);
    }

    @Override
    public void saveFileMeasure(Metric metric, double value) {
        measureSaver.saveFileMeasure(metric, value);
    }

    @Override
    public void saveSummaryMeasure(Metric tests, double executedTests) {
        measureSaver.saveSummaryMeasure(tests, executedTests);
    }

    @Override
    public void setIgnoreTwiceSameMeasure() {
        measureSaver.setIgnoreTwiceSameMeasure();
    }

    @Override
    public void setExceptionOnTwiceSameMeasure() {
        measureSaver.setExceptionOnTwiceSameMeasure();
    }
    @Override
    public void setProjectAndContext(Project project,
            SensorContext sensorContext) {
        measureSaver.setProjectAndContext(project, sensorContext);
    }

}
