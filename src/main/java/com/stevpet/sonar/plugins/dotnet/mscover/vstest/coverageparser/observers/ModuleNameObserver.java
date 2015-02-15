package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestCoverageRegistry;

/**
 * Used to parse only those modules that are of interest to this project. Especially for imported coverage files this
 * saves a lot of time
 * @author stevpet
 *
 */
public class ModuleNameObserver extends VsTestCoverageObserver {
    private static final Logger LOG  = LoggerFactory.getLogger(ModuleNameObserver.class);   
    private List<String> modulesToParse = new ArrayList<String>();

    public ModuleNameObserver() {
        setPattern("Module/ModuleName");
    }
    
    /**
     * modules with name (including the .dll, or .exe part) in the list will be parsed, all others will be ignored
     * @param modules modules to parse
     */
    public void addModulesToParse(List<String> modules) {
        for(String  module : modules) {
            modulesToParse.add(module.toLowerCase());
        }
    }
   
    @Override
    public void setVsTestRegistry(VsTestCoverageRegistry vsTestRegistry) {
       
    }

    @ElementMatcher(elementName="ModuleName")
    public void moduleNameMatcher(String value) {
        if(modulesToParse.size()==0) {
            return;
        }
        boolean shouldSkip=!modulesToParse.contains(value);
        if(!shouldSkip) {
            LOG.info("Module {} will be parsed",value);           
        }
        if(shouldSkip) {
            setSkipTillNextElement("Module");
        }
    }
}
