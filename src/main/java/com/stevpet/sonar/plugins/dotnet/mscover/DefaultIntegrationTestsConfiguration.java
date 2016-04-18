package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.PropertyType;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.config.PropertyDefinition.Builder;

import com.google.common.base.Preconditions;

public class DefaultIntegrationTestsConfiguration implements IntegrationTestsConfiguration, BatchExtension {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultIntegrationTestsConfiguration.class);
    private static final String MSCOVER = "sonar.mscover.integrationtests.";
    private static final String MSCOVER_INTEGRATION_RESULTS = MSCOVER + "dir";
    private static final String MSCOVER_INTEGRATION_TOOL = MSCOVER + "tool";
    private static final String MSCOVER_INTEGRATION_MODE = MSCOVER + "mode";
    static final String MSCOVER_SPECFLOWTESTS_ROOT = DefaultIntegrationTestsConfiguration.MSCOVER + "root";
    private static final String MSCOVER_INTEGRATION_TESTCASEFILTER = DefaultIntegrationTestsConfiguration.MSCOVER
            + "testcasefilter";
    private static final String MSCOVER_INTEGRATION_PROJECTPATTERN = DefaultIntegrationTestsConfiguration.MSCOVER
            + "projectpattern";
    private static final String MSCOVER_INTEGRATION_COVERAGEREADER_TIMEOUT = DefaultIntegrationTestsConfiguration.MSCOVER
            + "coveragereader.timeout";
    private static final String MSCOVER_INTEGRATION_COVERAGEREADER_THREADS = DefaultIntegrationTestsConfiguration.MSCOVER
            + "coveragereader.threads";
    private static final String MSCOVER_INTEGRATION_TESTRUNNER_THREADS = DefaultIntegrationTestsConfiguration.MSCOVER
            + "testrunner.threads";
    private static final String MSCOVER_INTEGRATION_TESTRUNNER_TIMEOUT = DefaultIntegrationTestsConfiguration.MSCOVER
            + "testrunner.timeout";
    private static final String MSCOVER_INTEGRATION_SCHEDULE = DefaultIntegrationTestsConfiguration.MSCOVER + "schedule";
    private Settings settings;
    private FileSystem fileSystem;

    private SettingsHelper settingsHelper;

    public DefaultIntegrationTestsConfiguration(Settings settings, FileSystem fileSystem) {
        this.settings = settings;
        this.fileSystem = fileSystem;
        this.settingsHelper = new SettingsHelper(settings);

    }

    public static Collection<PropertyDefinition> getProperties() {
        Collection<PropertyDefinition> properties = new ArrayList<>();
        properties.add(createProperty(MSCOVER_SPECFLOWTESTS_ROOT, PropertyType.STRING)
                .name("root directory of integration test projects")
                .description("used in auto mode to determine whether to execute integration tests or save coverage data")
                .index(0)
                .build());
        properties.add(createProperty(MSCOVER_INTEGRATION_TESTCASEFILTER, PropertyType.STRING)
                .name("TestCase filter")
                .description("filter to apply to VSTEST to run only those tests that pass the filter")
                .index(1)
                .build());
        properties
                .add(createProperty(MSCOVER_INTEGRATION_PROJECTPATTERN, PropertyType.REGULAR_EXPRESSION)
                        .name("Pattern for integration test projects")
                        .description(
                                "Regular expression to determine which projects are integration test projects, should also be included in overall test project pattern")
                        .index(2)
                        .build());
        properties.add(createProperty(MSCOVER_INTEGRATION_COVERAGEREADER_THREADS, PropertyType.INTEGER)
                .name("Number of threads for coverage reader")
                .description(
                        "Specified number of threads that the coverage reader uses when reading the coverage data for a project")
                .defaultValue("5")
                .index(3)
                .build());
        properties.add(createProperty(MSCOVER_INTEGRATION_COVERAGEREADER_TIMEOUT, PropertyType.INTEGER)
                .name("Timeout for coverage reader")
                .description("Specifies max time that the coveragereader may take to parse all coverage data of a project")
                .defaultValue("4")
                .index(4)
                .build());
        properties
                .add(createProperty(MSCOVER_INTEGRATION_TESTRUNNER_THREADS, PropertyType.INTEGER)
                        .name("Number of threads for integrationtestrunner")
                        .description(
                                "Specified number of threads that the integrationtestrunner uses when reading the coverage data for a project")
                        .defaultValue("5")
                        .index(3)
                        .build());
        properties
                .add(createProperty(MSCOVER_INTEGRATION_TESTRUNNER_TIMEOUT, PropertyType.INTEGER)
                        .name("Timeout for coverage reader")
                        .description(
                                "Specifies max time in minutes that the testrunner may take to run all integrationtests in a solution")
                        .defaultValue("120")
                        .index(6)
                        .build());
        properties.add(createProperty(MSCOVER_INTEGRATION_SCHEDULE, PropertyType.REGULAR_EXPRESSION)
                .name("Pattern to specify schedule")
                .description("Regular expression to specify the day on which integration tests are run\n" +
                        "1=MONDAY, 2=Tuesday i.e. [6-7] = SATURDAY and SUNDAY")
                .defaultValue(".*")
                .index(7)
                .build());
        return properties;
    }

    private static Builder createProperty(String key, PropertyType propertyType) {
        return PropertyDefinition.builder(key).type(propertyType).subCategory("Integration tests");

    }

    /**
     * Fail Fast through calling this is early as possible
     */
    public void validate() {
        validateMode();
        validateDirectory();
        validateSchedule();
        validateCoverageTool();
        validateTestRunnerThreads();
        validateTestRunnerTimeout();
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration
     * #getMode()
     */
    @Override
    public Mode getMode() {
        String modeValue = settings.getString(MSCOVER_INTEGRATION_MODE);
        if (StringUtils.isEmpty(modeValue)) {
            return Mode.DISABLED;
        }
        Mode mode;
        try {
            mode = Enum.valueOf(Mode.class, modeValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            LOG.error("invalid property setting '{}={}'. Leave empty or set to one of: disabled run read",
                    MSCOVER_INTEGRATION_MODE, modeValue);
            throw e;
        }
        if (mode == Mode.AUTO) {
            mode = isModulePathChildOfRootPath() ? Mode.RUN : Mode.READ;
        }
        return mode;
    }

    private void validateMode() {
        getMode();
    }
    private boolean isModulePathChildOfRootPath() {
        String modulePath = fileSystem.baseDir().getAbsolutePath();
        String rootPath = settings.getString(MSCOVER_SPECFLOWTESTS_ROOT);
        Preconditions.checkNotNull(rootPath, "property not set: " + MSCOVER_SPECFLOWTESTS_ROOT);
        String windowsPath = rootPath.replaceAll("/", "\\\\");
        return modulePath.contains(windowsPath);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration
     * #getCoverageDir()
     */
    @Override
    public File getDirectory() {
        String coverageDir = settings.getString(MSCOVER_INTEGRATION_RESULTS);
        if (StringUtils.isEmpty(coverageDir)) {
            LOG.error("property {} must be set to directoy where coverage results should be stored", MSCOVER_INTEGRATION_RESULTS);
        }
        return new File(coverageDir);
    }

    private void validateDirectory() {
        String coveragePath = settings.getString(MSCOVER_INTEGRATION_RESULTS);
        if(coveragePath == null) {
            throw new InvalidPropertyValueException(MSCOVER_INTEGRATION_RESULTS, "property is required, and parent must exist");
        }
        File coverageDir = new File(coveragePath);
        File parentDir=coverageDir.getParentFile();
        if(parentDir==null) {
            throw new InvalidPropertyValueException(MSCOVER_INTEGRATION_RESULTS, coveragePath,"must have parent");
        }
        if(!parentDir.exists()) {
            throw new InvalidPropertyValueException(MSCOVER_INTEGRATION_RESULTS, coveragePath,"parent must exist");            
        }
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration
     * #getTool()
     */
    @Override
    public Tool getTool() {
        String settingValue = settings.getString(MSCOVER_INTEGRATION_TOOL);
        if (StringUtils.isEmpty(settingValue)) {
            return Tool.OPENCOVER;
        }
        Tool tool;
        try {
            tool = Enum.valueOf(Tool.class, settingValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            LOG.error("invalid property setting '{}={}'. Leave empty or set to one of: disabled run read",
                    MSCOVER_INTEGRATION_MODE, settingValue);
            throw e;
        }
        return tool;
    }

    private void validateCoverageTool() {
        getTool();
    }
    /**
     * Used by sensors in shouldExecute() to see if they should run.
     * 
     * @param tool
     * @param mode
     * @return
     */
    @Override
    public boolean matches(Tool tool, Mode mode) {
        if (settings.getString(MSCOVER_INTEGRATION_PROJECTPATTERN) == null) {
            return false;
        }
        if (!(getMode() == mode)) {
            return false;
        }
        if (getMode() == Mode.DISABLED) {
            return false;
        }

        return getTool() == tool;

    }

    @Override
    public String getTestCaseFilter() {
        return settings.getString(MSCOVER_INTEGRATION_TESTCASEFILTER);
    }

    @Override
    public Pattern getTestProjectPattern() {
        Pattern pattern = settingsHelper.getPattern(MSCOVER_INTEGRATION_PROJECTPATTERN);
        return pattern;
    }

    @Override
    public int getCoverageReaderTimeout() {
        int timeout = settings.getInt(MSCOVER_INTEGRATION_COVERAGEREADER_TIMEOUT);
        if (timeout == 0) {
            timeout = 5;
        }
        return timeout;
    }

    @Override
    public int getCoverageReaderThreads() {
        int threads = settings.getInt(MSCOVER_INTEGRATION_COVERAGEREADER_THREADS);
        if (threads <= 0) {
            threads = 1;
        }
        return threads;
    }

    @Override
    public int getTestRunnerThreads() {
        int threads = settings.getInt(MSCOVER_INTEGRATION_TESTRUNNER_THREADS);
        if (threads <= 0) {
            threads = TESTRUNNER_THREADS_DEFAULT;
        }
        return threads;
    }

    private void validateTestRunnerThreads() {
        int threads = settings.getInt(MSCOVER_INTEGRATION_TESTRUNNER_THREADS);
        if(threads <0 || threads > 10) {
            throw new InvalidPropertyValueException(MSCOVER_INTEGRATION_TESTRUNNER_THREADS, threads, ">0 and <=10");
        }
    }
    @Override
    public int getTestRunnerTimeout() {
        int timeout = settings.getInt(MSCOVER_INTEGRATION_TESTRUNNER_TIMEOUT);
        if (timeout <= 0) {
            timeout = TESTRUNNER_TIMEOUT_DEFAULT;
        }
        return timeout;
    }
    
    private void validateTestRunnerTimeout() {
        int timeout = settings.getInt(MSCOVER_INTEGRATION_TESTRUNNER_THREADS);
        if(timeout < 0 || timeout > 120) {
            throw new InvalidPropertyValueException(MSCOVER_INTEGRATION_TESTRUNNER_THREADS, timeout, ">0 and <=120 minutes");           
        }
    }

    @Override
    public Pattern getSchedule() {
        String schedule = settings.getString(MSCOVER_INTEGRATION_SCHEDULE);
        if ("*".equals(schedule)) {
            schedule = ".*";
        }
        try {
            Pattern p = Pattern.compile(schedule);
            return p;
        } catch (PatternSyntaxException e) {
            throw new InvalidPropertyValueException(MSCOVER_INTEGRATION_SCHEDULE, schedule, "valid regular expression", e);
        }
    }
    
    public void validateSchedule() {
        getSchedule();
    }
}
