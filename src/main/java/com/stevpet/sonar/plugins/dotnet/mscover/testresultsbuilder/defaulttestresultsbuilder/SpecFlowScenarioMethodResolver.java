package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class SpecFlowScenarioMethodResolver implements BatchExtension {
    
    private final Logger LOG = LoggerFactory.getLogger(SpecFlowScenarioMethodResolver.class);
    
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private Map<String,File> methodToFileMap;
    
    public SpecFlowScenarioMethodResolver(MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
    }
    
    /**
     * Find the sourcefile that declares the given test method as private virtual void [methodName]()
     * @param methodName
     * @return file in which the method is declared, or null.
     */
    public File getFile(String methodName) {
        if(methodToFileMap==null) {
            methodToFileMap=loadMap();
        }
        if(StringUtils.isEmpty(methodName)) {
            return null;
        }
        return methodToFileMap.get(methodName);
        
    }
    
    private Map<String, File> loadMap() {
        Map<String,File>map = new HashMap<>();
        List<File> files=microsoftWindowsEnvironment.getUnitTestSourceFiles();
        for(File file : files) {
            loadFile(map, file);   
        }
        return map;
    }


    private void loadFile(Map<String, File> map, File file) {
        Pattern functionPattern = Pattern.compile("^\\s+public virtual void ([_A-Za-z][A-Za-z0-9_]+)\\(\\)$");
        try {
            List<String> lines=FileUtils.readLines(file);
            for(String line:lines) {
                Matcher matcher = functionPattern.matcher(line);
                if(matcher.matches()) {
                    String key=matcher.group(1);
                    map.put(key, file);
                }
            }
        } catch (IOException e) {
            String msg = "IOException during accessing file " + file.getAbsolutePath();
            LOG.error(msg);
            throw new MsCoverException(msg,e);
        }
    }
}
