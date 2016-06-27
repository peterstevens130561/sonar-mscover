package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.Settings;


import com.stevpet.sonar.plugins.dotnet.mscover.property.ConfigurationProperty;
import com.stevpet.sonar.plugins.dotnet.mscover.property.CoverageReaderThreadsProperty;
import com.stevpet.sonar.plugins.dotnet.mscover.property.CoverageReaderTimeoutProperty;
import com.stevpet.sonar.plugins.dotnet.mscover.property.CoverageRootProperty;
import com.stevpet.sonar.plugins.dotnet.mscover.property.ExcludeProjectsProperty;
import com.stevpet.sonar.plugins.dotnet.mscover.property.InvalidPropertyValueException;
import com.stevpet.sonar.plugins.dotnet.mscover.property.ProjectPatternProperty;
import com.stevpet.sonar.plugins.dotnet.mscover.property.ScheduleProperty;
import com.stevpet.sonar.plugins.dotnet.mscover.property.SpecflowTestsRootProperty;
import com.stevpet.sonar.plugins.dotnet.mscover.property.TestCaseFilterProperty;
import com.stevpet.sonar.plugins.dotnet.mscover.property.TestRunnerThreadsProperty;
import com.stevpet.sonar.plugins.dotnet.mscover.property.TestRunnerTimeoutProperty;

public class DefaultIntegrationTestsConfiguration implements IntegrationTestsConfiguration, BatchExtension {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultIntegrationTestsConfiguration.class);
    public static final String MSCOVER = "sonar.mscover.integrationtests.";
    private static final String MSCOVER_INTEGRATION_RESULTS = DefaultIntegrationTestsConfiguration.MSCOVER + "dir";
    private static final String MSCOVER_INTEGRATION_TOOL = MSCOVER + "tool";
    private static final String MSCOVER_INTEGRATION_MODE = MSCOVER + "mode";
    private PropertyBag propertyBag;


    private Settings settings;
    private FileSystem fileSystem;

    private final TestRunnerTimeoutProperty testRunnerTimeoutProperty;
    private final CoverageRootProperty coverageRootProperty;
    private final SpecflowTestsRootProperty specflowTestsRootProperty;
    private final CoverageReaderTimeoutProperty coverageReaderTimeoutProperty;
    private final CoverageReaderThreadsProperty coverageReaderThreadsProperty;
    private final TestCaseFilterProperty testcaseFilterProperty;
    private final ProjectPatternProperty projectPatternProperty;
    private final TestRunnerThreadsProperty testRunnerThreadsProperty;
    private final ScheduleProperty scheduleProperty;
    private final ExcludeProjectsProperty excludedProjectsProperty;

    public DefaultIntegrationTestsConfiguration(Settings settings, FileSystem fileSystem) {
        this(settings);
        this.settings = settings;
        this.fileSystem = fileSystem;;
    }

    public DefaultIntegrationTestsConfiguration(Settings settings) {
        propertyBag = new PropertyBag(settings);
        this.testcaseFilterProperty = propertyBag.create(TestCaseFilterProperty.class);
        this.projectPatternProperty = propertyBag.create(ProjectPatternProperty.class);
        this.testRunnerTimeoutProperty = propertyBag.create(TestRunnerTimeoutProperty.class);
        this.coverageRootProperty = propertyBag.create(CoverageRootProperty.class);
        this.specflowTestsRootProperty = propertyBag.create(SpecflowTestsRootProperty.class);
        this.coverageReaderTimeoutProperty = propertyBag.create(CoverageReaderTimeoutProperty.class);
        this.coverageReaderThreadsProperty  = propertyBag.create(CoverageReaderThreadsProperty.class);
        this.testRunnerThreadsProperty = propertyBag.create(TestRunnerThreadsProperty.class);
        this.excludedProjectsProperty = propertyBag.create(ExcludeProjectsProperty.class);

        this.scheduleProperty=propertyBag.create(ScheduleProperty.class);       
    }

    /**
     * Use in plugin only
     */
    public DefaultIntegrationTestsConfiguration() {
        this(null);
    }

    public Collection<PropertyDefinition> getProperties() {
        
        Collection<PropertyDefinition> properties = new ArrayList<>();
        int i=0;
        for(ConfigurationProperty configurationProperty : propertyBag.getProperties()) {
            properties.add(configurationProperty.getPropertyBuilder().index(i).build());
            ++i;
        }

        return properties;
    }


    /**
     * Fail Fast through calling this is early as possible
     */
    public void validate() {
        for(ConfigurationProperty configurationProperty : propertyBag.getProperties()) {
            configurationProperty.validate();
        }
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
        File rootFile = specflowTestsRootProperty.getValue();
        if(rootFile == null) {
            throw new InvalidPropertyValueException(specflowTestsRootProperty.getKey(),"required when auto mode selected");
        }
        String rootPath = rootFile.getAbsolutePath();
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
        return coverageRootProperty.getValue();
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
        if (projectPatternProperty.getValue()==null) {
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
        return testcaseFilterProperty.getValue();
    }

    @Override
    public Pattern getTestProjectPattern() {
        return  projectPatternProperty.getValue();
    }

    @Override
    public int getCoverageReaderTimeout() {
        return coverageReaderTimeoutProperty.getValue();
    }

    @Override
    public int getCoverageReaderThreads() {
        return coverageReaderThreadsProperty.getValue();
    }

    @Override
    public int getTestRunnerThreads() {
        return testRunnerThreadsProperty.getValue();
    }

    @Override
    public int getTestRunnerTimeout() {
        return testRunnerTimeoutProperty.getValue();
    }

    @Override
    public Pattern getSchedule() {
        return scheduleProperty.getValue();
    }
    
    public void validateSchedule() {
        getSchedule();
    }
    
    @Override
    public List<String> getExcludedProjects() {
        return excludedProjectsProperty.getValue();
    }
    private class PropertyBag {
        
        private Settings settings;
        private List<ConfigurationProperty> properties = new ArrayList<>();
        

        
       public PropertyBag(Settings settings) {
            this.settings = settings;
        }

    public <T> T create(Class clazz) {
            T instance;
            try {
                instance = (T) clazz.getDeclaredConstructor(Settings.class).newInstance(settings);
                properties.add((ConfigurationProperty) instance);
                return instance;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }

        }
       
       List<ConfigurationProperty> getProperties() {
           return properties;
       }
    }


}
