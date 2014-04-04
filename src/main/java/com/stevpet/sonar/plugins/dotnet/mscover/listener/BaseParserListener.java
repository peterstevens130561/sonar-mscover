package com.stevpet.sonar.plugins.dotnet.mscover.listener;

import org.codehaus.staxmate.in.SMInputCursor;

public class BaseParserListener implements ParserObserver {

    public void onLine(SMInputCursor linesCursor) {
        //Base does nothing
    }

    public void onSourceFileNames(SMInputCursor childCursor) {
        //Base does nothing
    }


    public boolean onModuleName(SMInputCursor cursor) {
        // TODO Auto-generated method stub
        return true;
    }

}
