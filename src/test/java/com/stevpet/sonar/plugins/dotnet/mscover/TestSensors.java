package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.reflections.Reflections;
import org.sonar.api.batch.Sensor;

import com.stevpet.sonar.plugins.dotnet.mscover.plugin.Extension;

@Extension
public class TestSensors {

    public void simpleExperiment() {
        String name=TestSensors.class.getPackage().getName();
        List<Class<?>> results = new ArrayList<Class<?>>();
        Reflections reflections = new Reflections("com.stevpet.sonar.plugins.dotnet.mscover");
         Set<Class<? extends Sensor>> subtypes=reflections.getSubTypesOf(Sensor.class);
        for(Class<? extends Sensor> clazz : subtypes) {
        
            Extension annotation = clazz.getAnnotation(Extension.class);
            if(annotation !=null) {
                results.add(clazz);
            }
        }
        Assert.assertNotNull(subtypes);
        Assert.assertEquals(3,results.size());
    }
    

    public void straightfromClass() {
           Extension extension=this.getClass().getAnnotation(Extension.class);
           Assert.assertNotNull(extension);
    }
}