package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.injectors.ConstructorInjection;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowDirector;

public abstract class WorkflowSensor implements Sensor {
    private DefaultPicoContainer container;
    private MsCoverProperties msCoverProperties;
    
    public WorkflowSensor(VsTestEnvironment vsTestEnvironment,
            MsCoverProperties msCoverProperties, FileSystem fileSystem,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            PathResolver pathResolver, WorkflowDirector workflowDirector) {
        container = new DefaultPicoContainer(new ConstructorInjection());
        container.addComponent(vsTestEnvironment).addComponent(fileSystem)
                .addComponent(microsoftWindowsEnvironment)
                .addComponent(msCoverProperties)
                .addComponent(pathResolver)
                .addComponent(workflowDirector);
        this.msCoverProperties=msCoverProperties;
    }
    public boolean shouldExecuteOnProject(Project project) {
            return project.isRoot() && msCoverProperties.getRunMode() != RunMode.SKIP && shouldExecuteWorkflow();
    }        

    /**
     * check to performed by the workflowsensor as to whether it actually should run
     * @return
     */
    public abstract boolean shouldExecuteWorkflow();
  
    //public abstract void analyse(Project project, SensorContext context);
    
    public DefaultPicoContainer getContainer() {
        return container;
    }
    protected DefaultPicoContainer prepareChildContainer(SensorContext context) {
        DefaultPicoContainer childContainer = new DefaultPicoContainer(container);
        childContainer.addComponent(context).addComponent(DefaultResourceResolver.class);
        return childContainer;
    }

}