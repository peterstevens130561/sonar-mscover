package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.ParserObserver;


public interface Subject {
    void registerObserver(ParserObserver observer);
}
