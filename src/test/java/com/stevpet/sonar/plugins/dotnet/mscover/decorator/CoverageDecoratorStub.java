package com.stevpet.sonar.plugins.dotnet.mscover.decorator;

import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

class CoverageDecoratorStub extends CoverageDecorator {

    private int calls;
    public int getCalls() {
        return calls ;
    }
    protected CoverageDecoratorStub(Settings settings) {
        super(settings,null);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean shouldExecuteDecorator(Project project, Settings settings) {
        // TODO Auto-generated method stub
        PropertiesHelper helper = new PropertiesHelper(settings);
        return helper.isIntegrationTestsEnabled();
    }

    @Override
    protected void handleUncoveredResource(DecoratorContext context,
            double lines) {
           ++calls;
        
    }
    
}