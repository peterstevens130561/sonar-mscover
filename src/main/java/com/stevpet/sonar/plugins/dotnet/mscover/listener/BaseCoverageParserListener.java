package com.stevpet.sonar.plugins.dotnet.mscover.listener;

import org.codehaus.staxmate.in.SMInputCursor;

public class BaseCoverageParserListener implements ParserListener {

    public void onLine(SMInputCursor linesCursor) {
        //Base does nothing
    }

    public void onSourceFileNames(SMInputCursor childCursor) {
        //Base does nothing
    }

    public boolean onModuleName(String moduleName) {
        return true;
    }

}
