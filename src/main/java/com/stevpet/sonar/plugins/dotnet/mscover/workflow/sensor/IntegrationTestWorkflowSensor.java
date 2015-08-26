package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import org.picocontainer.DefaultPicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.VsTestFilteringCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.DefaultCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.nullsaver.NullBranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.VsTestCoverageToXmlConverter;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest.IntegrationTestCoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest.IntegrationTestLineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.IntegrationTestResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;


public class IntegrationTestWorkflowSensor implements Sensor {
    private static final Logger LOG = LoggerFactory
            .getLogger(UnitTestWorkflowSensor.class);
    
    private static final String LOGPREFIX = "IntegrationTestWorkflowSensor : ";
    private DefaultPicoContainer container ;

    private MsCoverConfiguration msCoverProperties;
    public IntegrationTestWorkflowSensor(MsCoverConfiguration msCoverProperties,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment, 
            FileSystem fileSystem,
            IntegrationTestResourceResolver resourceResolver,
            PathResolver pathResolver,
            IntegrationTestCache integrationTestCache) {
        
        container = new DefaultPicoContainer();
        container.addComponent(msCoverProperties)
            .addComponent(microsoftWindowsEnvironment)
            .addComponent(fileSystem)
            .addComponent(resourceResolver)
            .addComponent(pathResolver)
            .addComponent(integrationTestCache);
        this.msCoverProperties=msCoverProperties;
    }

    @Override
    public void analyse(Project project, SensorContext context) {
        LogInfo("Starting");
        LogChanger.setPattern();
        getComponents(context);
        IntegrationTestCache cache= container.getComponent(IntegrationTestCache.class);
        IntegrationTestCoverageReader reader = container.getComponent(IntegrationTestCoverageReader.class);
        CoverageSaver saver = container.getComponent(CoverageSaver.class);
        
        SonarCoverage sonarCoverage;
        if(cache.didRun()) {
            sonarCoverage=cache.getCoverage();
        } else {
            sonarCoverage = new SonarCoverage();
            reader.read(sonarCoverage);
            cache.didRun();
            cache.setCoverage(sonarCoverage);
        } 

        saver.save(sonarCoverage);
        LogInfo("Done");
    }
    
    

    private void LogInfo(String msg, Object... objects) {
        LOG.info(LOGPREFIX + msg, objects);
    }


    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return msCoverProperties.isIntegrationTestsEnabled();
    }
    

    private void  getComponents(SensorContext context) {
        container
            .addComponent(context)
            .addComponent(IntegrationTestLineFileCoverageSaver.class)
            .addComponent(NullBranchFileCoverageSaver.class)
            .addComponent(IntegrationTestCoverageReader.class)
            .addComponent(DefaultCoverageSaver.class)
            .addComponent(WindowsCommandLineExecutor.class)
            .addComponent(VsTestFilteringCoverageParser.class)
            .addComponent(WindowsCodeCoverageCommand.class)
            .addComponent(VsTestCoverageToXmlConverter.class);
    }
}
