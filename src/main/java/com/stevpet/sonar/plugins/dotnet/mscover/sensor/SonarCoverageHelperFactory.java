package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.IntegrationTestLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.LineMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.UnitTestLineSaver;
@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class SonarCoverageHelperFactory implements BatchExtension,
        AbstractCoverageHelperFactory {

    private VSTestCoverageSaver coverageHelper ;

    public CoverageSaver createIntegrationTestCoverageHelper(
            MsCoverProperties propertiesHelper,
            FileSystem fileSystem,
            MeasureSaver measureSaver) {
        createCoverageHelper(propertiesHelper, fileSystem);
        LineMeasureSaver lineSaver=IntegrationTestLineSaver.create(measureSaver);
        injectSavers(lineSaver);
        return coverageHelper;
        
    }

    public CoverageSaver createUnitTestCoverageHelper(MsCoverProperties propertiesHelper,
            FileSystem fileSystem,
            MeasureSaver measureSaver) {
            createCoverageHelper(propertiesHelper, fileSystem);
            LineMeasureSaver lineSaver=UnitTestLineSaver.create(measureSaver);
            injectSavers(lineSaver);
            return coverageHelper;       
    }
    

   private void createCoverageHelper(MsCoverProperties propertiesHelper,
            FileSystem fileSystem) {
        coverageHelper = new VSTestCoverageSaver(propertiesHelper,fileSystem);
    }


    private void injectSavers(LineMeasureSaver lineSaver) {
        coverageHelper.setLineSaver(lineSaver);
    }

    public  ShouldExecuteHelper createShouldExecuteHelper(
            MsCoverProperties propertiesHelper) {
        return new SonarShouldExecuteHelper(propertiesHelper);
    }

}
