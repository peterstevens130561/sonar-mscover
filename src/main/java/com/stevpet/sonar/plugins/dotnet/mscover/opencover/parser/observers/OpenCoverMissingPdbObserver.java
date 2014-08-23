package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;

public class OpenCoverMissingPdbObserver extends OpenCoverObserver{
    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverMissingPdbObserver.class);
    private boolean isMissing=false;

    
    public OpenCoverMissingPdbObserver() {
        setPattern("Modules/Module" +
                 "|Modules/Module/FullName" +
                 "|Modules/Module/ModuleName"
                 );
    }

    @AttributeMatcher(attributeName="skippedDueTo",elementName="Module")
    public void fileRefMatcher(String attributeValue) {
        isMissing="MissingPdb".equals(attributeValue);
    }
    
    @ElementMatcher(elementName="FullName")
    public void fullName(String value) {
        if(isMissing) {
            LOG.error("Missing PDB file for " + value + "\n did you use a file reference instead of a project reference ?");
            super.setError();
        }
        isMissing=false;
    }
}
