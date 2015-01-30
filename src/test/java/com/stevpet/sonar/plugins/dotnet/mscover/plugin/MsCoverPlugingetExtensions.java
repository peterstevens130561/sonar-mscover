package com.stevpet.sonar.plugins.dotnet.mscover.plugin;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.SonarPlugin;

@SuppressWarnings({"unchecked","rawtypes","deprecation"})
public class MsCoverPlugingetExtensions {
 
    Logger LOG = LoggerFactory.getLogger(MsCoverPlugingetExtensions.class);
    
    @Test
    public void allElementsImplementBatchExtension_Pass() {
        SonarPlugin classUnderTest = new MsCoverPlugin() ;
        List<String> allowedInterfaces =Arrays.asList("org.sonar.api.batch.Sensor","org.sonar.api.BatchExtension","org.sonar.api.batch.Decorator");

        List<Class> plugins = classUnderTest.getExtensions();
        for(Class plugin:plugins) {
            boolean found = checkImplementation(allowedInterfaces, plugin);
            assertTrue(plugin.getName(),found);
        }
    }
    private boolean checkImplementation(List<String> allowedInterfaces,
            Class plugin) {
        LOG.info("checking {}",plugin.getName());
        boolean found = false;
        for(Class intf: plugin.getInterfaces()) {
            LOG.info(intf.getName());
            if(allowedInterfaces.contains(intf.getName())) {
                found=true;
            }
        }
        if(!found) {
            Class superClass = plugin.getSuperclass();
              LOG.info(superClass.getName());
              if("java.lang.Object".equals(superClass.getName())) {
                  return false;
              }
              return checkImplementation(allowedInterfaces,plugin.getSuperclass());
        }
        return true;
    }
}
