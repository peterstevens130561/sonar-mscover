package com.stevpet.sonar.plugins.dotnet.mscover.decorator;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

public class IntegrationTestBlockDecorator extends BaseDecorator {
    public IntegrationTestBlockDecorator(Settings settings,
            TimeMachine timeMachine) {
        super(settings, timeMachine);
        this.executionMode="active";
        this.testMetric = CoreMetrics.IT_BRANCH_COVERAGE;
    }

    //TODO: Add work to support unit test block decorator 60min
    @Override
    public boolean shouldExecuteDecorator(Project project, Settings settings) {
        PropertiesHelper helper = new PropertiesHelper(settings);
        return helper.isIntegrationTestsEnabled();
    }

    @Override
    protected void handleUncoveredResource(DecoratorContext context, double blocks) {
      context.saveMeasure(CoreMetrics.IT_BRANCH_COVERAGE, 0.0);
      context.saveMeasure(CoreMetrics.IT_UNCOVERED_CONDITIONS, blocks);
      context.saveMeasure(CoreMetrics.IT_CONDITIONS_TO_COVER, blocks);
    }
    
    @DependedUpon
    public List<Metric> generatesCoverageMetrics() {
      return Arrays.asList(CoreMetrics.IT_BRANCH_COVERAGE,CoreMetrics.IT_UNCOVERED_CONDITIONS,CoreMetrics.IT_CONDITIONS_TO_COVER, CoreMetrics.IT_COVERED_CONDITIONS_BY_LINE,CoreMetrics.IT_COVERED_CONDITIONS_BY_LINE);
    }

}
