package com.stevpet.sonar.plugins.dotnet.resharper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.SonarException;
import org.sonar.api.utils.command.CommandException;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.resharper.exceptions.InspectCodeRunnerException;
import com.stevpet.sonar.plugins.dotnet.resharper.exceptions.ReSharperException;
import com.stevpet.sonar.plugins.dotnet.resharper.inspectcode.ReSharperCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;
public class DefaultInspectCodeRunner implements InspectCodeRunner {
    
    private Logger Log = LoggerFactory.getLogger(DefaultInspectCodeRunner.class);
    private static final String RESHARPER_EXECUTABLE = "inspectcode.exe";
    private Settings settings;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private FileSystem fileSystem;
    private ReSharperCommandBuilder resharperCommandBuilder;
    private CommandLineExecutor commandLineExecutor;
    private ReSharperConfiguration reSharperConfiguration;
    private InspectCodeBatchData inspectCodeBatchData;

    public DefaultInspectCodeRunner(Settings settings,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            FileSystem fileSystem,
            ReSharperCommandBuilder reSharperCommandBuilder,
            CommandLineExecutor commandLineExecutor, ReSharperConfiguration reSharperConfiguration,
            InspectCodeBatchData inspectCodeBatchData) {
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.fileSystem = fileSystem;
        this.resharperCommandBuilder=reSharperCommandBuilder;
        this.commandLineExecutor=commandLineExecutor;
        this.reSharperConfiguration=reSharperConfiguration;
        this.inspectCodeBatchData=inspectCodeBatchData;
        this.settings=settings;
    }

    /**
     * executes inspectcode with settings found in the current working dir.
     * 
     * @return the reportfile
     */
    public File inspectCode() {
        File reportFile;
        if(hasRun()){
            return getReportFile();
        }
        VisualStudioSolution vsSolution = microsoftWindowsEnvironment
                .getCurrentSolution();
        List<String> properties = getProperties();

        resharperCommandBuilder.setSolution(vsSolution);
        resharperCommandBuilder.setProperties(properties);

        File inspectCodeFile = getInspectCodeFile();
        resharperCommandBuilder.setExecutable(inspectCodeFile);
        defineReportFile();
        reportFile = getReportFile();
        resharperCommandBuilder.setReportFile(reportFile);

        String additionalArguments = reSharperConfiguration.getMSBuildProperties();
        resharperCommandBuilder.setProperties(additionalArguments);
        String cachesHome;
        if(reSharperConfiguration.useCache()) {
            cachesHome = reSharperConfiguration.getCachesHome();
        } else {
            File cachesDir=new File(fileSystem.workDir(),ReSharperConfiguration.DEFAULT_CACHEDIR);
            cachesHome=cachesDir.getAbsolutePath();
            
        }
        resharperCommandBuilder.setCachesHome(cachesHome);

        String profile = createLocalProjectProfile();
        resharperCommandBuilder.setProfile(profile);

        int timeout = reSharperConfiguration.getTimeOutMinutes();
        int exitCode=0;
        try {
            exitCode=commandLineExecutor.execute(resharperCommandBuilder,timeout);
        } catch (CommandException e ) {
            if(e.getMessage().contains("Timeout exceeded") && reportFile.exists()) {
                Log.warn("InspectCode terminated by timeout, but reportFile exists, so ignoring");
                return reportFile;
            }
            throw e;
        }
        
        if (exitCode != 0) { // && exitCode != 512) { -- Why 512? Magic numbers
                             // are evil
            throw new ReSharperException("ReSharper execution failed with return code '" + exitCode
                    + "'. Check ReSharper documentation for more information.");
        }
        return reportFile;
    }

    public String createLocalProjectProfile() {
        String profile=reSharperConfiguration.getProfile();
        if(profile==null) {
            return null;
        }
        File srcFile=new File(profile);
        File destFile = new File(fileSystem.workDir(),"Global.DotSettings");
        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            String msg = "could not copy from " + srcFile.getAbsolutePath() + " to " + destFile.getAbsolutePath();
            Log.error(msg);
            throw new InspectCodeRunnerException(msg,e);
        }
        return destFile.getAbsolutePath();
    }

    @Override
    public boolean hasRun() {
        File reportFile = getReportFile();
        boolean exists = reportFile!=null && reportFile.exists();
        return exists;
    }
    
    public File getReportFile() {
        File report = inspectCodeBatchData.getReport();
        return report;
    }

    public void defineReportFile() {
        File report = new File(fileSystem.workDir(),ReSharperConfiguration.REPORT_FILENAME);
        inspectCodeBatchData.setReport(report);
    }

    private File getInspectCodeFile() {
        String dir = reSharperConfiguration.getInspectCodeInstallDir();
        if (StringUtils.isEmpty(dir)) {
            dir = ReSharperConfiguration.INSTALL_DIR_DEFVALUE;
        }
        File inspectCodeFile = new File(dir, RESHARPER_EXECUTABLE);
        if (!inspectCodeFile.exists()) {
            String message = "inspectcode not found in" + inspectCodeFile.getAbsolutePath() + "check property " + ReSharperConfiguration.INSTALL_DIR_KEY ;
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

    @Override
    public void dropCache() {
        inspectCodeBatchData.init();
    }
}
