package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.nullsaver.NullBranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class DefaultCoverageSaverFactory implements CoverageSaverFactory {

    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private DefaultResourceResolver resourceResolver;

    public DefaultCoverageSaverFactory(MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            PathResolver pathResolver,
            FileSystem fileSystem) {
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.resourceResolver = new DefaultResourceResolver(pathResolver,
                fileSystem);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.CoverageSaverFactory#createVsTestUnitTestCoverageSaver()
     */
    @Override
    public CoverageSaver createVsTestUnitTestCoverageSaver() {
        return new CoverageSaverBase(new NullBranchFileCoverageSaver(),
                new UnitTestLineFileCoverageSaver(resourceResolver),
                microsoftWindowsEnvironment);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.CoverageSaverFactory#createOpenCoverUnitTestCoverageSaver(com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment, org.sonar.api.scan.filesystem.PathResolver, org.sonar.api.batch.fs.FileSystem)
     */
    @Override
    public CoverageSaver createOpenCoverUnitTestCoverageSaver() {
        return new CoverageSaverBase(
                new DefaultBranchCoverageSaverFactory(resourceResolver).createUnitTestSaver(), 
                new DefaultLineCoverageSaverFactory(resourceResolver).createUnitTestSaver(), 
                microsoftWindowsEnvironment);

    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.CoverageSaverFactory#createOpenCoverIntegrationTestCoverageSaver(com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment, org.sonar.api.scan.filesystem.PathResolver, org.sonar.api.batch.fs.FileSystem)
     */
    @Override
    public CoverageSaver createOpenCoverIntegrationTestCoverageSaver() {
        return new CoverageSaverBase(
                new DefaultBranchCoverageSaverFactory(resourceResolver).createIntegrationTestSaver(),
                new DefaultLineCoverageSaverFactory(resourceResolver).createIntegrationTestSaver(),
                microsoftWindowsEnvironment);
    }

    public CoverageSaver createOverallTestCoverageSaver() {
        return new CoverageSaverBase(
                new DefaultBranchCoverageSaverFactory(resourceResolver).createOverallSaver(),
                new DefaultLineCoverageSaverFactory(resourceResolver).createOverallSaver(),
                microsoftWindowsEnvironment);
    }

}
