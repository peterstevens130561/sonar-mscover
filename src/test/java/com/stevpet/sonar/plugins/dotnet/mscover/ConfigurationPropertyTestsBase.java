package com.stevpet.sonar.plugins.dotnet.mscover;

import org.junit.Before;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.PropertyDefinitions;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.property.ConfigurationProperty;
import com.stevpet.sonar.plugins.dotnet.mscover.property.TestRunnerTimeoutProperty;

public class ConfigurationPropertyTestsBase<T> {

    protected Settings settings;
    private PropertyDefinitions propDefinitions;
    ConfigurationProperty<T> property ;


    public void setup(ConfigurationProperty configurationProperty) {
        PropertyDefinition definition=configurationProperty.getPropertyBuilder().build();
        propDefinitions = new PropertyDefinitions();
        propDefinitions.addComponent(definition);
        settings = new Settings(propDefinitions);  
    }
    
    public void setProperty(ConfigurationProperty<T> property) {
        this.property = property;
    }
    
    public void setPropertyValue(String value) {
        settings.setProperty(property.getKey(), value);
    }
    

}