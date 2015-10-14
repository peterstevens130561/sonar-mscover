package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the base parser, it's main purpose is to provide a testable parser
 * @author stevpet
 *
 */
public class StringSpecFlowParser {
    final Logger LOG = LoggerFactory.getLogger(StringSpecFlowParser.class);
    protected SpecFlowScenarioMap map;
    private Pattern namespacePattern = Pattern.compile("^namespace\\s+([A-Za-z0-9\\.]+)\\s?$");
    private Pattern methodNamePattern = Pattern.compile("^\\s?public virtual void ([_A-Za-z][A-Za-z0-9_]+)\\(\\)$");
    private Pattern descriptionPattern = Pattern.compile(".*\\[Microsoft\\.VisualStudio\\.TestTools\\.UnitTesting\\.DescriptionAttribute\\(\"(.*)\".*");
    //\\(\"(.*)\",\"(.*)\"\\)
    private Pattern attributePattern = Pattern.compile(".*\\[Microsoft\\.VisualStudio\\.TestTools\\.UnitTesting\\.TestPropertyAttribute\\(\"(.*)\",\\s?\"(.*)\".*");


    public SpecFlowScenarioMap parse(File file, Reader reader) throws IOException {
        map = new SpecFlowScenarioMap();
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        String testName=null ;
        String namespace=null;
        while((line=bufferedReader.readLine())!=null) {
            Matcher namespaceMatcher = namespacePattern.matcher(line);
            if(namespaceMatcher.matches()) {
                namespace=namespaceMatcher.group(1);
            }
            Matcher descriptionMatcher = descriptionPattern.matcher(line);
            if(descriptionMatcher.matches()) {
                testName = descriptionMatcher.group(1);
            }
            Matcher attributeMatcher = attributePattern.matcher(line);
            if(attributeMatcher.matches()) {
                String attributeKey=attributeMatcher.group(1);
                String attributeValue=attributeMatcher.group(2);
                if("VariantName".equals(attributeKey)) {
                    testName = testName + " " + attributeValue;
                }
                if(attributeKey.startsWith("Parameter:")) {
                    testName = testName + " " + attributeValue;
                }
            }
            Matcher matcher = methodNamePattern.matcher(line);
            if(matcher.matches()) {
                String key=matcher.group(1);
                LOG.debug("found method {} in {}",key,file.getName());
                if(StringUtils.isEmpty(testName)) {
                    testName=key;
                }
                SpecFlowScenario scenario = new SpecFlowScenario(file,namespace,key,testName);
                map.put(scenario);
                testName=null;
            }
        }
        bufferedReader.close();
        return map;
    }

}
