package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ElementMatcher;

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
            LOG.error("Missing PDB file for " + value);
            super.setError();
        }
        isMissing=false;
    }
}
