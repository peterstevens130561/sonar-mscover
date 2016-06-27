package com.stevpet.sonar.plugins.dotnet.mscover.property;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition.Builder;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Qualifiers;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;

public class ExcludeProjectsProperty extends ConfigurationPropertyBase<List<String>> {
    
    public ExcludeProjectsProperty(Settings settings) {
        super(settings);
    }
    
    public ExcludeProjectsProperty() {
        super();
    }

    @Override
    public Builder getPropertyBuilder() {
        return createProperty(getKey(),PropertyType.STRING)
                .name("Exclude from run")
                .description("list of projects to exclude from test run")
                .multiValues(true).onlyOnQualifiers(Qualifiers.PROJECT);
    }

    @Override
    public String getKey() {
        return DefaultIntegrationTestsConfiguration.MSCOVER
                + "excludeprojects";
    }

    /**
     * array of project names which should be excluded from run
     */
    @Override
    protected List<String> onGetValue(Settings settings) {
        List<String> projects= new ArrayList<String>();
        for(String project:settings.getStringArray(getKey())) {
            projects.add(project);
        }
        return projects;
    }

}
