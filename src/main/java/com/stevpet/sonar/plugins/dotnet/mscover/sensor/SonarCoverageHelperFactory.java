package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BaseBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BlockMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.IntegrationTestBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.UnitTestBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.IntegrationTestLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.LineMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.UnitTestLineSaver;
@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class SonarCoverageHelperFactory implements BatchExtension,
        AbstractCoverageHelperFactory {

    private VSTestCoverageSaver coverageHelper ;
    private LineMeasureSaver lineSaver;
    public CoverageSaver createIntegrationTestCoverageHelper(
            MsCoverProperties propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            MeasureSaver measureSaver) {
        createCoverageHelper(propertiesHelper, microsoftWindowsEnvironment);
        LineMeasureSaver lineSaver=IntegrationTestLineSaver.create(measureSaver);
        BlockMeasureSaver blockMeasureSaver = IntegrationTestBlockSaver.create(measureSaver);
        injectSavers(lineSaver, blockMeasureSaver);
        return coverageHelper;
        
    }

    public CoverageSaver createUnitTestCoverageHelper(MsCoverProperties propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            MeasureSaver measureSaver) {
            createCoverageHelper(propertiesHelper, microsoftWindowsEnvironment);
            LineMeasureSaver lineSaver=UnitTestLineSaver.create(measureSaver);
            BlockMeasureSaver blockMeasureSaver = UnitTestBlockSaver.create(measureSaver);
            injectSavers(lineSaver, blockMeasureSaver);
            return coverageHelper;       
    }
    

   private void createCoverageHelper(MsCoverProperties propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        coverageHelper = new VSTestCoverageSaver(propertiesHelper,microsoftWindowsEnvironment);
    }


    private void injectSavers(LineMeasureSaver lineSaver,
            BlockMeasureSaver blockMeasureSaver) {
        coverageHelper.setBlockSaver(new BaseBlockSaver(blockMeasureSaver));
        coverageHelper.setLineSaver(lineSaver);
    }

    public  ShouldExecuteHelper createShouldExecuteHelper(
            MsCoverProperties propertiesHelper) {
        // TODO Auto-generated method stub
        return new SonarShouldExecuteHelper(propertiesHelper);
    }

}
