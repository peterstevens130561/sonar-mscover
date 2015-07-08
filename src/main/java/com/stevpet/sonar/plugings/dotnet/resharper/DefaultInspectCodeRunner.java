package com.stevpet.sonar.plugings.dotnet.resharper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.SonarException;
import com.stevpet.sonar.plugings.dotnet.resharper.inspectcode.ReSharperCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

public class DefaultInspectCodeRunner implements InspectCodeRunner {
    private Logger Log = LoggerFactory.getLogger(DefaultInspectCodeRunner.class);
    private static long MINUTES_TO_MILLISECONDS = 60000;
    private static final String RESHARPER_EXECUTABLE = "inspectcode.exe";
    private Settings settings;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private FileSystem fileSystem;
    private ReSharperCommandBuilder resharperCommandBuilder;
    private CommandLineExecutor commandLineExecutor;

    public DefaultInspectCodeRunner(Settings settings,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            FileSystem fileSystem,
            ReSharperCommandBuilder reSharperCommandBuilder,
            CommandLineExecutor commandLineExecutor) {
        this.settings = settings;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.fileSystem = fileSystem;
        this.resharperCommandBuilder=reSharperCommandBuilder;
        this.commandLineExecutor=commandLineExecutor;

    }

    /**
     * executes inspectcode with settings found in the current working dir.
     * 
     * @return the reportfile
     */
    public File inspectCode() {
        File reportFile;

        VisualStudioSolution vsSolution = microsoftWindowsEnvironment
                .getCurrentSolution();
        List<String> properties = getProperties();

        resharperCommandBuilder.setSolution(vsSolution);
        resharperCommandBuilder.setProperties(properties);

        File inspectCodeFile = getInspectCodeFile();
        resharperCommandBuilder.setExecutable(inspectCodeFile);
        reportFile = new File(fileSystem.workDir(),
                ReSharperConstants.REPORT_FILENAME);
        resharperCommandBuilder.setReportFile(reportFile);

        String additionalArguments = settings
                .getString(ReSharperConstants.INSPECTCODE_PROPERTIES);
        resharperCommandBuilder.setProperties(additionalArguments);
        String cachesHome = settings
                .getString(ReSharperConstants.CACHES_HOME);
        resharperCommandBuilder.setCachesHome(cachesHome);

        String profile = settings
                .getString(ReSharperConstants.INSPECTCODE_PROFILE);
        resharperCommandBuilder.setProfile(profile);

        int timeout = settings
                .getInt(ReSharperConstants.TIMEOUT_MINUTES_KEY);
        if (timeout == 0) {
            timeout = 60;
        }
        int exitCode=commandLineExecutor.execute(resharperCommandBuilder,timeout);
        if (exitCode != 0) { // && exitCode != 512) { -- Why 512? Magic numbers
                             // are evil
            throw new ReSharperException("ReSharper execution failed with return code '" + exitCode
                    + "'. Check ReSharper documentation for more information.");
        }
        return reportFile;
    }

    private File getInspectCodeFile() {
        String dir = settings.getString(ReSharperConstants.INSTALL_DIR_KEY);
        if (StringUtils.isEmpty(dir)) {
            dir = ReSharperConstants.INSTALL_DIR_DEFVALUE;
        }
        File inspectCodeFile = new File(dir, RESHARPER_EXECUTABLE);
        if (!inspectCodeFile.exists()) {
            String message = "inspectcode not found in" + inspectCodeFile.getAbsolutePath() + "check property " + ReSharperConstants.INSTALL_DIR_KEY ;
            Log.error(message);
            throw new SonarException(message);
        }
        return inspectCodeFile;
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
