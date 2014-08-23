package com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNamesModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;

public class OpenCoverFileNamesAndIdObserver extends BaseParserObserver {

    
    private SourceFileNamesRegistry registry ;
    private SourceFileNamesModel model;

    public OpenCoverFileNamesAndIdObserver() {
        setPattern("Modules/Module/Files/File");
    }
    
    public void setRegistry(SourceFileNamesRegistry registry) {
        this.registry=registry;
    }

    @AttributeMatcher(attributeName = "uid", elementName = "File")
    public void uidMatcher(String attributeValue) {
        model = new SourceFileNamesModel();
        model.setSourceFileID(attributeValue);
    }
    
    @AttributeMatcher(attributeName="fullPath",elementName="File")
    public void fileMatcher(String attributeValue) {
        model.setSourceFileName(attributeValue);
        registry.add(model.getSourceFileID(), model);
    }

}
