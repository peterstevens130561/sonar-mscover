package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import java.io.File;

import junit.framework.Assert;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.Helper;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BaseBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.Saver;

public class TestCoverSensor extends BaseCoverageSensor {

    private Saver saver ;
    
    public Saver getCoverageSaver() {
        return saver;
    }
    public TestCoverSensor(Settings settings,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            TimeMachine timeMachine) {
        super(settings, microsoftWindowsEnvironment,timeMachine);
    }


    protected String getCoveragePath() {
        File file = Helper.getResource("mscoverage.xml");
        Assert.assertTrue(file.exists());
        return file.getAbsolutePath();
    }

    protected boolean shouldExecuteSensor(PropertiesHelper helper) {
        return true;
    }
    @Override
    protected BaseBlockSaver createBlockSaver(Project project,
            SensorContext sensorContext) {
        return new TestBlockSaver(sensorContext,project);
    }
    @Override
    protected Saver createLineSaver(Project project,
            SensorContext sensorContext, CoverageRegistry registry) {
        saver= new TestsCoverageLineSaver(sensorContext,project,registry);
        return saver;
    }

}
