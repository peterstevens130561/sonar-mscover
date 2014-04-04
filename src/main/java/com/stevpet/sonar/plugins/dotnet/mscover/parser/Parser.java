package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import org.codehaus.staxmate.in.SMInputCursor;

import com.stevpet.sonar.plugins.dotnet.mscover.listener.ParserObserver;

public interface Parser {

    boolean isCompatible(SMInputCursor rootCursor);

    void parse(SMInputCursor startElementCursor);

    void setListener(
            ParserObserver testCoverageParserListener);

}