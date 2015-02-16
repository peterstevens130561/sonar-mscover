package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameRow;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable;

public class OpenCoverFileNamesAndIdObserver extends BaseParserObserver {

    
    private SourceFileNameTable registry ;
    private SourceFileNameRow model;

    public OpenCoverFileNamesAndIdObserver() {
        setPattern("Modules/Module/Files/File");
    }
    
    public void setRegistry(SourceFileNameTable registry) {
        this.registry=registry;
    }

    @AttributeMatcher(attributeName = "uid", elementName = "File")
    public void uidMatcher(String attributeValue) {
        model = new SourceFileNameRow();
        model.setSourceFileID(Integer.parseInt(attributeValue));
    }
    
    @AttributeMatcher(attributeName="fullPath",elementName="File")
    public void fileMatcher(String attributeValue) {
        model.setSourceFileName(attributeValue);
        registry.add(model.getSourceFileID(), model);
    }

}
