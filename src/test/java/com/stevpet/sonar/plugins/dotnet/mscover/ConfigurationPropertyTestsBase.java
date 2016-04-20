package com.stevpet.sonar.plugins.dotnet.mscover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.PropertyDefinitions;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.property.ConfigurationProperty;
import com.stevpet.sonar.plugins.dotnet.mscover.property.InvalidPropertyValueException;
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
    
    protected void checkInRangeInt(T value) {
        setPropertyValue(value.toString());
        T actual = property.getValue();
        assertEquals(value,actual);
    }
    
    protected void checkOutsideRangeInt(T value) {
        setPropertyValue(value.toString());
        try {
            property.validate();
        } catch (InvalidPropertyValueException e) {
            return ; // this is expected
        } catch ( Exception e) {
            fail("just too much expected InvalidPropertyException"); 
        }
    }
    
    protected void checkExceptionOnNotInt() {
        setPropertyValue("groble");
        try {
            property.validate();
        } catch (InvalidPropertyValueException e) {
            return ; // this is expected
        } catch ( Exception e) {
            fail("expected InvalidPropertyException"); 
        }
    }
        
    protected void checkDefaultOnNotSet(T expected) {
        T value = property.getValue();
        assertEquals("expect default",expected,value);
    }
    public void setPropertyValue(String value) {
        settings.setProperty(property.getKey(), value);
    }

}