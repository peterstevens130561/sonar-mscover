package com.stevpet.sonar.plugins.dotnet.mscover.decorator;

import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

class CoverageDecoratorStub extends BaseDecorator {

    private int calls;
    public int getCalls() {
        return calls ;
    }
    protected CoverageDecoratorStub(Settings settings) {
        super(settings,null);
    }

    @Override
    public boolean shouldExecuteDecorator(Project project, Settings settings) {
        MsCoverProperties helper = PropertiesHelper.create(settings);
        return helper.isIntegrationTestsEnabled();
    }

    @Override
    protected void handleUncoveredResource(DecoratorContext context,
            double lines) {
           ++calls;
        
    }
    
}