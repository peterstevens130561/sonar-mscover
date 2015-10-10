package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringSpecFlowParser {
    final Logger LOG = LoggerFactory.getLogger(StringSpecFlowParser.class);
    protected SpecFlowScenarioMap map;
    private Pattern methodNamePattern = Pattern.compile("^\\s+public virtual void ([_A-Za-z][A-Za-z0-9_]+)\\(\\)$");
    private Pattern attributePattern = Pattern.compile("\\[Microsoft");


    public SpecFlowScenarioMap parse(File file, InputStream inputStream) throws IOException {
        map = new SpecFlowScenarioMap();
        InputStreamReader input = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(input);
        String line;
        while((line=reader.readLine())!=null) {
            Matcher attributeMatcher = attributePattern.matcher(line);
            if(attributeMatcher.matches()) {
                LOG.debug(" {} {}",attributeMatcher.group(1),attributeMatcher.group(2));
            }
            Matcher matcher = methodNamePattern.matcher(line);
            if(matcher.matches()) {
                String key=matcher.group(1);
                LOG.debug("found method {} in {}",key,file.getName());
                SpecFlowScenario scenario = new SpecFlowScenario(file,key,key);
                map.put(scenario);
            }
        }
        return map;
    }

}
