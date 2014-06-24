package com.stevpet.sonar.plugins.dotnet.mscover.sensor.vstestopencover;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.dotnet.api.DotNetConfiguration;
import org.sonar.plugins.dotnet.api.DotNetConstants;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;
import org.sonar.plugins.dotnet.api.sensor.AbstractDotNetSensor;
import org.sonar.plugins.dotnet.api.utils.FileFinder;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
@DependsUpon(DotNetConstants.CORE_PLUGIN_EXECUTED)
public class VsTestOpenCoverCoverageSensor extends AbstractDotNetSensor {

    private static String WONT_EXECUTE = "VsTest.Console using OpenCover.Console.Exe won't execute as ";
    private static final Logger LOG = LoggerFactory.getLogger(VsTestOpenCoverCoverageSensor.class);
    private VisualStudioSolution solution;
    private File workDir;
    private PropertiesHelper propertiesHelper ;
    private final Settings settings;
    protected VsTestOpenCoverCoverageSensor(Settings settings, 
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        super(microsoftWindowsEnvironment, "OpenCover", PropertiesHelper.MSCOVER_MODE);
        propertiesHelper = PropertiesHelper.create(settings);
        this.settings = settings;
    }

    @Override
    public String[] getSupportedLanguages() {
        // TODO Auto-generated method stub
        return new String[] {"cs"};
    }


    /**
     * {@inheritDoc}
     */
    public boolean shouldExecuteOnProject(Project project) {
      if (MODE_REUSE_REPORT.equals(getExecutionMode())) {
        logReasonToNotExecute("it is set to 'reuseReport' mode.");
        return false;
      }
      if (getMicrosoftWindowsEnvironment().isTestExecutionDone()) {
        logReasonToNotExecute("test execution has already been done.");
        return false;
      }
      if (getMicrosoftWindowsEnvironment().getCurrentSolution() != null
        && getMicrosoftWindowsEnvironment().getCurrentSolution().getUnitTestProjects().isEmpty()) {
        logReasonToNotExecute(" as there are no test projects.");
        return false;
      }

      return super.shouldExecuteOnProject(project);
    }
    private void logReasonToNotExecute(String string) {
        LOG.info(WONT_EXECUTE + string);
    }

    @Override
    public void analyse(Project project, SensorContext context) {
        getSolution();
        ensureWorkDirExists();
        buildVsTestOpenCoverRunner();
        executeVsTestOpenCoverRunner();
        // tell that tests were executed so that no other project tries to launch them a second time
        getMicrosoftWindowsEnvironment().setTestExecutionDone();
     
    }

    private void executeVsTestOpenCoverRunner() {
       throw new NotImplementedException();
        
    }

    private void buildVsTestOpenCoverRunner() {
        throw new NotImplementedException();      
    }

    private void getSolution() {
        solution = getMicrosoftWindowsEnvironment().getCurrentSolution();
        if (solution == null) {
            throw new SonarException("No .NET solution or project has been given to the Gallio command builder.");
        }
    }

    private void ensureWorkDirExists() {
        workDir = new File(solution.getSolutionDir(), getMicrosoftWindowsEnvironment().getWorkingDirectory());
        if (!workDir.exists()) {
          workDir.mkdirs();
        }
    }
    
    private List<File> findTestAssemblies() {
        Set<File> assemblyFiles = Sets.newHashSet();

        Collection<VisualStudioProject> testProjects = solution.getUnitTestProjects();
        for (VisualStudioProject visualStudioProject : testProjects) {
            addAssembly(assemblyFiles, visualStudioProject);
        }

        return Lists.newArrayList(assemblyFiles);
    }
      
      private void addAssembly(Collection<File> assemblyFileList, VisualStudioProject visualStudioProject) {
          String buildConfiguration = settings.getString(DotNetConstants.BUILD_CONFIGURATION_KEY);
          String buildPlatform = settings.getString(DotNetConstants.BUILD_PLATFORM_KEY);
          File assembly = visualStudioProject.getArtifact(buildConfiguration, buildPlatform);
          if (assembly != null && assembly.isFile()) {
            assemblyFileList.add(assembly);
          } else {
            LOG.warn("Test assembly not found at the following location: {}"
              + "\n, using the following configuration:\n  - csproj file: {}\n  - build configuration: {}\n  - platform: {}",
                new Object[] {assembly, visualStudioProject.getProjectFile(), buildConfiguration, buildPlatform});
          }
        }

}
