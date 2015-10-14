package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.File;

public class SpecFlowScenario {
    
    private String methodName;
    private String testName ;
    private String namespace;
    private File featureSourceFile;
    
    public SpecFlowScenario() {
        
    }
    /**
     * 
     * @param file unit test feature file i.e somefeature.feature.cs
     * @param methodName name of method in the file
     * @param testName name as we would like to present it in SonarQube
     */
    public SpecFlowScenario(File file, String namespace,String methodName, String testName) {
        featureSourceFile=file;
        this.methodName=methodName;
        this.setNamespace(namespace);
        this.testName=testName;
    }

    /**
     * get the name of the method in the source file
     * @return the name of the method in the source file i.e SomeTestVariant0
     */
    public String getMethodName() {
        return methodName;
    }
    
    /**
     * set the name of the method in the source file i.e SomeTestVariant0
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    /**
     * 
     * @return the name of the scenario, including indication of the variant i.e. Some test point:(0,0,0) result:(1,1)
     */
    public String getTestName() {
        return testName;
    }
    
    /**
     * 
     * @param set the name of the test 
     */
    public void setTestName(String testName) {
        this.testName = testName;
    }
    
    /**
     * 
     * @return the source file (feature.cs)
     */
    public File getFeatureSourceFile() {
        return featureSourceFile;
    }
    
    /**
     * the source file
     * @param featureSourceFile
     */
    public void setFeatureSourceFile(File featureSourceFile) {
        this.featureSourceFile = featureSourceFile;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

}
