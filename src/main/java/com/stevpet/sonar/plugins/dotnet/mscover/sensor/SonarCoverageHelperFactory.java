package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BaseBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BlockMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.IntegrationTestBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.UnitTestBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.IntegrationTestLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.LineMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.UnitTestLineSaver;

public class SonarCoverageHelperFactory implements
        AbstractCoverageHelperFactory {

    private CoverageHelper coverageHelper ;
    private LineMeasureSaver lineSaver;
    public CoverageHelper createIntegrationTestCoverageHelper(
            PropertiesHelper propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            MeasureSaver measureSaver) {
        createCoverageHelper(propertiesHelper, microsoftWindowsEnvironment);
        LineMeasureSaver lineSaver=IntegrationTestLineSaver.create(measureSaver);
        BlockMeasureSaver blockMeasureSaver = IntegrationTestBlockSaver.create(measureSaver);
        injectSavers(lineSaver, blockMeasureSaver);
        return coverageHelper;
        
    }

    public CoverageHelper createUnitTestCoverageHelper(PropertiesHelper propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            MeasureSaver measureSaver) {
            createCoverageHelper(propertiesHelper, microsoftWindowsEnvironment);
            LineMeasureSaver lineSaver=UnitTestLineSaver.create(measureSaver);
            BlockMeasureSaver blockMeasureSaver = UnitTestBlockSaver.create(measureSaver);
            injectSavers(lineSaver, blockMeasureSaver);
            return coverageHelper;       
    }
    

   private void createCoverageHelper(PropertiesHelper propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        coverageHelper = new CoverageHelper(propertiesHelper,microsoftWindowsEnvironment);
    }


    private void injectSavers(LineMeasureSaver lineSaver,
            BlockMeasureSaver blockMeasureSaver) {
        coverageHelper.setBlockSaver(new BaseBlockSaver(blockMeasureSaver));
        coverageHelper.setLineSaver(lineSaver);
    }

    public  ShouldExecuteHelper createShouldExecuteHelper(
            PropertiesHelper propertiesHelper) {
        // TODO Auto-generated method stub
        return new SonarShouldExecuteHelper(propertiesHelper);
    }

}
