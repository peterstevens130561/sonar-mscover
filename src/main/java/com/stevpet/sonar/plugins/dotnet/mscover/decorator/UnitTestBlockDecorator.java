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
import com.stevpet.sonar.plugins.dotnet.mscover.plugin.Extension;

@Extension
public class UnitTestBlockDecorator extends BaseDecorator {
    //TODO: Add work to support unit test block decorator 30min
    public UnitTestBlockDecorator(Settings settings, TimeMachine timeMachine) {
        super(settings, timeMachine);
        this.executionMode="active";
        this.testMetric = CoreMetrics.BRANCH_COVERAGE;
    }

    @Override
    public boolean shouldExecuteDecorator(Project project, Settings settings) {
        PropertiesHelper helper = new PropertiesHelper(settings);
        return helper.isUnitTestsEnabled();
    }

    @Override
    protected void handleUncoveredResource(DecoratorContext context, double blocks) {
      context.saveMeasure(CoreMetrics.BRANCH_COVERAGE, 0.0);
      context.saveMeasure(CoreMetrics.UNCOVERED_CONDITIONS, blocks);
      context.saveMeasure(CoreMetrics.CONDITIONS_TO_COVER, blocks);
    }
    
    @DependedUpon
    public List<Metric> generatesCoverageMetrics() {
      return Arrays.asList(CoreMetrics.BRANCH_COVERAGE,CoreMetrics.UNCOVERED_CONDITIONS,CoreMetrics.CONDITIONS_TO_COVER, CoreMetrics.COVERED_CONDITIONS_BY_LINE,CoreMetrics.COVERED_CONDITIONS_BY_LINE);
    }

}
