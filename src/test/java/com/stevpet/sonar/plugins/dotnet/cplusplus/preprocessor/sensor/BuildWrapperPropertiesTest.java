package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.sonar.api.config.PropertyDefinition;


public class BuildWrapperPropertiesTest {
    @Test
    public void twoProperties() {
       List classes = Arrays.asList(BuildWrapperInitializer.class);
       List myList = new ArrayList();
       Collection<PropertyDefinition> properties=BuildWrapperConstants.getProperties();
       myList.addAll(classes);
       myList.addAll(properties);
       assertEquals("expect  properties",5,myList.size());

    }
    
}
