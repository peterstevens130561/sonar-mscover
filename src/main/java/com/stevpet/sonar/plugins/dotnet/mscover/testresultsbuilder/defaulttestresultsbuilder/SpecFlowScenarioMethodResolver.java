/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
/**
 * On first invocation will get all the unittest sourcefiles, and extract all test methods, the name of the method
 * serves as key to map to the sourcefile
 * 
 * The algorithm will work in most cases, if the same method is used in other files then the result may be assigned
 * to the wrong test
 * 
 *
 */
public class SpecFlowScenarioMethodResolver implements Function<MethodId, String> {
    
    private final Logger LOG = LoggerFactory.getLogger(SpecFlowScenarioMethodResolver.class);
    
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private Map<String,File> methodToFileMap;
    
    public SpecFlowScenarioMethodResolver(MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
    }
    
    @Override
    public String apply(MethodId methodId) {
        String methodName=methodId.getMethodName();
        File  file=getFile(methodName);
        if(file==null) {
            LOG.warn("Tried to resolve a potential specflow method, but failed {}",methodName);
        }
        return file==null?null:file.getAbsolutePath();
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
            throw new IllegalStateException(msg,e);
        }
    }


}
