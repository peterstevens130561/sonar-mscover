package com.stevpet.sonar.plugins.dotnet.mscover.failfast;


import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

/**
 * The purpose of this class is to verify all properties at the beginning of the sensor, so that we're not running for an hour
 * to find that some simple property is wrong.
 * 
 * @author stevpet
 *
 */
public class FailFastSensor implements Sensor {

    private Settings settings;
    private FileSystem fileSystem;

    public FailFastSensor(Settings settings,FileSystem fileSystem)  {
        this.settings = settings;
        this.fileSystem= fileSystem;
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return true;
    }

    @Override
    public void analyse(Project module, SensorContext context) {
        new DefaultIntegrationTestsConfiguration(settings, fileSystem).validate();
        
    }

}
