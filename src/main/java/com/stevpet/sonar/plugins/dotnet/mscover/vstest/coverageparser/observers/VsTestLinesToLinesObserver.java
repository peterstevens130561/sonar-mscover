package com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.observers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stevpet.sonar.plugins.dotnet.mscover.model.LineModel;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.LinesRegistry;


public class VsTestLinesToLinesObserver extends BaseParserObserver{

    private LinesRegistry registry ;
    private LineModel model = new LineModel();

    public VsTestLinesToLinesObserver() {
        setPattern("Module/NamespaceTable/Class/Method/Lines/(LnStart|Coverage|SourceFileID)");
    }


    @ElementMatcher(elementName="LnStart")
    public void lnStartMatcher(String value) {
        model = new LineModel();
        model.setLnStart(value);
    }
    
    @ElementMatcher(elementName="Coverage")
    public void coverageMatcher(String value) {
        model.setCoverage(value);
    }
    
    @ElementMatcher(elementName="SourceFileID")
    public void sourceFileIdMatcher(String fileId){
         registry.add(fileId, model);  
    }
    
    public void setRegistry(LinesRegistry registry) {
        this.registry=registry;
    }

}
