package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers;

import java.util.ArrayList;
import java.util.Collection;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;

public class OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs extends OpenCoverObserver{
    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs.class);
    private boolean isMissing=false;
    private Collection<String> pdbs = new ArrayList<String>();
    
    public OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs() {
        setPattern("Modules/Module" +
                 "|Modules/Module/FullName" +
                 "|Modules/Module/ModuleName"
                 );
    }

    public void setPdbsThatCanBeIgnoredIfMissing(Collection<String> missingPdbsThatCanBeIgnored) {
        if(missingPdbsThatCanBeIgnored == null) {
            throw new IllegalArgumentException("pdbs can't be null");
        }
        this.pdbs = missingPdbsThatCanBeIgnored;
    }
    
    @AttributeMatcher(attributeName="skippedDueTo",elementName="Module")
    public void fileRefMatcher(String attributeValue) {
        isMissing="MissingPdb".equals(attributeValue);
    }
    
    @ElementMatcher(elementName="FullName")
    public void fullName(String value) {
        if(isMissing) {
            String msg="Missing PDB file for " + value + "\n did you use a file reference instead of a project reference ?";
            int lastSlash=value.lastIndexOf("\\");
            String name;
            if(lastSlash!=-1) {
                name =value.substring(lastSlash+1);
            } else {
                name=value;
            }
            if(pdbs.contains(name)) {
                LOG.warn(msg);
            } else {
                LOG.error(msg);
                super.setError();
            }
        }
        isMissing=false;
    }
}
