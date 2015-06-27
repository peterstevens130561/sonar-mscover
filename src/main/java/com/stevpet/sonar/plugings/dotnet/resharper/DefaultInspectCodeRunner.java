package com.stevpet.sonar.plugings.dotnet.resharper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugings.dotnet.resharper.inspectcode.ReSharperCommandBuilder;
import com.stevpet.sonar.plugings.dotnet.resharper.inspectcode.ReSharperRunner;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

public class DefaultInspectCodeRunner implements InspectCodeRunner {
    private Settings settings;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private FileSystem fileSystem;
    public DefaultInspectCodeRunner(Settings settings, 
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            FileSystem fileSystem) {
        this.settings = settings;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.fileSystem=fileSystem;
        
    }
    
    /**
     * executes inspectcode with settings found in the current working dir.
     * @return the reportfile
     */
    public Collection<File> inspectCode(Project project) {
        File reportFile;
        try {
            ReSharperRunner runner = ReSharperRunner.create(settings
                    .getString(ReSharperConstants.INSTALL_DIR_KEY));
            VisualStudioSolution vsSolution = microsoftWindowsEnvironment
                    .getCurrentSolution();
            List<String> properties = getProperties();
            ReSharperCommandBuilder builder = runner.createCommandBuilder(
                    vsSolution, properties);
            reportFile = new File(fileSystem.workDir(),
                    ReSharperConstants.REPORT_FILENAME);
            builder.setReportFile(reportFile);

            String additionalArguments = settings
                    .getString(ReSharperConstants.INSPECTCODE_PROPERTIES);
            builder.setProperties(additionalArguments);
            String cachesHome = settings
                    .getString(ReSharperConstants.CACHES_HOME);
            builder.setCachesHome(cachesHome);

            String profile = settings
                    .getString(ReSharperConstants.INSPECTCODE_PROFILE);
            builder.setProfile(profile);

            int timeout = settings
                    .getInt(ReSharperConstants.TIMEOUT_MINUTES_KEY);
            if(timeout==0) {
                timeout=60;
            }
            runner.execute(builder, timeout);
        } catch (ReSharperException e) {
            throw new SonarException("ReSharper execution failed."
                    + e.getMessage(), e);
        }
        Collection<File> reportFiles = Collections.singleton(reportFile);
        return reportFiles;
    }
    
    private List<String> getProperties() {
        List<String> properties = new ArrayList<String>();
        addPropertyIfDefined(properties, "Platform",
                "sonar.dotnet.buildPlatform");
        addPropertyIfDefined(properties, "Configuration",
                "sonar.dotnet.buildConfiguration");
        return properties;
    }
    private void addPropertyIfDefined(List<String> properties,
            String msBuildPropertyName, String sonarPropertyName) {
        String value = settings.getString(sonarPropertyName);
        if (!StringUtils.isEmpty(value)) {
            value = value.replace(" ", "");
            properties.add(msBuildPropertyName + "=" + value);
        }
    }
}
