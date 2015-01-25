package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.VsTestRegistry;


public class ModuleNameObserver extends VsTestCoverageObserver {

    private static final Logger LOG  = LoggerFactory.getLogger(ModuleNameObserver.class);
    public ModuleNameObserver() {
        setPattern("Module/ModuleName");
    }
    @Override
    public void setVsTestRegistry(VsTestRegistry vsTestRegistry) {
       
    }

    @ElementMatcher(elementName="ModuleName")
    public void moduleNameMatcher(String value) {
       // boolean shouldSkip=!value.equals("rabarber.dll");
        boolean shouldSkip=false;
        LOG.info(value);
        if(shouldSkip) {
            setSkipTillNextElement("Module");
        }
    }
}
