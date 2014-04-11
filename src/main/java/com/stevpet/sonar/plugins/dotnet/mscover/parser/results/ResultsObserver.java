package com.stevpet.sonar.plugins.dotnet.mscover.parser.results;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserObserver;

public class ResultsObserver implements ParserObserver {
    static final Logger LOG = LoggerFactory
            .getLogger(ResultsObserver.class);
    private final Pattern pattern;

    public ResultsObserver() {
        pattern = Pattern
                .compile("ResultSummary/Counters");
    }
    
    private int executedTests ;
    
    public boolean isMatch(String path) {
        LOG.error(path);
        Matcher matcher = pattern.matcher(path);
        return matcher.matches();
    }

    public void observeElement(String name, String text) {
        executedTests=186;
    }
    
    public int getExecutedTests() {
        return executedTests;
    }


}
