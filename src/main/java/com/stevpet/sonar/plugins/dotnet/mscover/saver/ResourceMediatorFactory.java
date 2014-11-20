package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public interface ResourceMediatorFactory {
    ResourceMediator create(SensorContext sensor, Project project);
    ResourceMediator createWithFilters(
            SensorContext sensor, Project project,TimeMachine timeMaching,
            MsCoverProperties msCoverProperties);
    ResourceMediator createWithEmptyFilters(SensorContext sensor, Project project);
}
