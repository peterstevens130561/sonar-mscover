package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableCollection;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.exceptions.MsCoverUnitTestAssemblyDoesNotExistException;

public class IgnoreMissingAssembliesFinder extends AbstractAssembliesFinder implements AssembliesFinder {
    private Logger log = LoggerFactory.getLogger(getClass());
    private MsCoverProperties propertiesHelper ;
    public IgnoreMissingAssembliesFinder(MsCoverProperties propertiesHelper) {
        super(propertiesHelper);
        this.propertiesHelper = propertiesHelper;
    }

    public void onNonExistingFile(File assemblyFile) {
        String assemblyName = assemblyFile.getName();
        Collection<String> canBeIgnoredIfMissing = new ArrayList<String>();
        canBeIgnoredIfMissing = propertiesHelper.getUnitTestAssembliesThatCanBeIgnoredIfMissing();
        if(canBeIgnoredIfMissing.contains(assemblyName)) {
            log.warn("Ignoring non-existent unit test assembly {}",assemblyName);
        } else {
            String assemblyPath=assemblyFile.getAbsolutePath();
            log.error("unit test assembly {} does not exist",assemblyPath);
            throw new MsCoverUnitTestAssemblyDoesNotExistException(assemblyFile);
        }
    }
    
    void setLogger(Logger log) {
        this.log=log;
    }
}
