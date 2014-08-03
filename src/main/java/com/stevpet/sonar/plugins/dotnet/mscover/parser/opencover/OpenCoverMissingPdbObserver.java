package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ElementMatcher;

public class OpenCoverMissingPdbObserver extends OpenCoverObserver{
    private List<String> missingPdbs = new ArrayList<String>();
    private boolean isMissing=false;
    public List<String> getMissingPdbs() {
        return missingPdbs;
    }
    
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
            missingPdbs.add(value);
        }
        isMissing=false;
    }
}
