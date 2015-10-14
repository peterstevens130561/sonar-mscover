package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class SpecFlowScenarioMethodResolver implements BatchExtension {
    
    final Logger LOG = LoggerFactory.getLogger(SpecFlowScenarioMethodResolver.class);
    
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private SpecFlowScenarioMap scenarioMap;
    private SpecFlowCSharpFileParser parser = new SpecFlowCSharpFileParser();
    public SpecFlowScenarioMethodResolver(MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
    }
    
    /**
     * Find the sourcefile that declares the given test method as private virtual void [methodName]()
     * @param methodName
     * @return file in which the method is declared, or null.
     */
    public File getFile(MethodId testMethod) {
        if(scenarioMap==null) {
            scenarioMap=loadMap();
        }
        if(testMethod==null) {
            return null;
        }
        SpecFlowScenario scenario= scenarioMap.get(testMethod);
        return scenario.getFeatureSourceFile();
        
    }
    
    private SpecFlowScenarioMap loadMap() {
        SpecFlowScenarioMap map = new SpecFlowScenarioMap();
        List<File> files=microsoftWindowsEnvironment.getUnitTestSourceFiles();
        for(File file : files) {
            map.putAll(parser.loadFile(file));  
        }
        return map;
    }
    

}
