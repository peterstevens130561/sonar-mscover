package com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.DefaultCoverageSaverFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

@DependsUpon( value={"IntegrationTestCoverageSaved","UnitTestCoverageSaved"})
public class OverallCoverageSensor implements Sensor {
    private static Logger LOG = LoggerFactory.getLogger(OverallCoverageSensor.class);
    private CoverageCache coverageCache;
    private CoverageSaver coverageSaver;
    public OverallCoverageSensor(CoverageCache coverageCache, CoverageSaver coverageSaver) {
        this.coverageCache=coverageCache;
        this.coverageSaver=coverageSaver;
    }
    
    public OverallCoverageSensor(CoverageCache coverageCache, MicrosoftWindowsEnvironment microsoftWindowsEnvironment, PathResolver pathResolver, FileSystem fileSystem) {
        this(coverageCache,
                new DefaultCoverageSaverFactory(
                        microsoftWindowsEnvironment, 
                        pathResolver, 
                        fileSystem)
                .createOverallTestCoverageSaver()
                );
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return project.isModule();
    }

    @Override
    public void analyse(Project module, SensorContext context) {
        LOG.info("OveralCoverageSensor invoked");
        SonarCoverage sonarCoverage = coverageCache.get(module.getName());
        if(sonarCoverage==null) {
            return;
        }
        coverageSaver.save(context, sonarCoverage);
    }

}
