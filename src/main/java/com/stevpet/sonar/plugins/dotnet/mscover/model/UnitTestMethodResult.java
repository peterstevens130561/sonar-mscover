/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.model;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.SonarException;

public class UnitTestMethodResult {
    
    public enum TestResult {
        Passed,
        Failed,
        Ignored
    }

    private String testId;
    private String duration;
    private String outcome;
    private String relativeResultsDirectory;
    private String codeBase;
    private String message = StringUtils.EMPTY;
    private String stackTrace;
    private UnitTestingResults parent;
	private String moduleName;
	private String namespaceName;
	private String className;
	private String methodName;

    UnitTestMethodResult(UnitTestingResults unitTestingResults) {
        this.parent = unitTestingResults;
    }

    public UnitTestMethodResult() {
    }

    /**
     * add this method to the parent
     * 
     * @return
     */
    public UnitTestMethodResult addToParent() {
        parent.add(this);
        return this;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public String getTestId() {
        return testId;
    }

    public UnitTestMethodResult setTestId(String testId) {
        this.testId = testId;
        return this;
    }

    public String getTestName() {
        return methodName;
    }

    public UnitTestMethodResult setTestName(String testName) {
        methodName=testName;
        return this;
    }

    public String getDuration() {
        return duration;
    }

    public String getCodeBase() {
        return codeBase;
    }

    public String getClassName() {
        return className;
    }

    public String getOutcome() {
        return outcome;
    }

    public TestResult getTestResult() {
        switch(outcome) {
        case "Passed" : return TestResult.Passed;
        case "Failed" : return TestResult.Failed;
        case "NotExecuted" : return TestResult.Ignored;
        default: throw new InvalidTestResultException(outcome);
        }
    }
    public UnitTestMethodResult setOutcome(String outcome) {
        this.outcome = outcome;
        return this;
    }

    public String getRelativeResultsDirectory() {
        return relativeResultsDirectory;
    }

    public UnitTestMethodResult setRelativeResultsDirectory(
            String relativeResultsDirectory) {
        this.relativeResultsDirectory = relativeResultsDirectory;
        return this;
    }

    public UnitTestMethodResult setCodeBase(String value) {
        this.codeBase = value;
        return this;

    }

    /**
     * set the module to the last segment in the codeBase
     * 
     * @param path
     *            to the dll, may use / or \ in the path,
     */
    public UnitTestMethodResult setModuleFromCodeBase(String codeBase) {
        if (StringUtils.isEmpty(codeBase)) {
            throw new SonarException("module can't be null");
        }
        codeBase = codeBase.replace("\\", "/");
        String[] parts = codeBase.split("/");
        this.moduleName=parts[parts.length - 1];
       
        return this;
    }

    /**
     * 
     * @param value
     *            is the fully qualified classname (namespace + class)
     */
    public UnitTestMethodResult setNamespaceNameFromClassName(String value) {
        if (StringUtils.isEmpty(value)) {
            throw new SonarException("namespacename can't be null");
        }
        int lastDot = value.lastIndexOf(".");
        if (lastDot == -1) {
            namespaceName = StringUtils.EMPTY;
        } else {
            namespaceName = value.substring(0, lastDot);
        }
        return this;
    }

    public UnitTestMethodResult setClassName(String value) {
        if (StringUtils.isEmpty(value)) {
            throw new SonarException("className can't be null");
        }
        int lastDot = value.lastIndexOf(".");
        if (lastDot == -1) {
            className = value;
        } else {
            className = value.substring(lastDot + 1);
        }
        return this;
    }

    public MethodId getMethodId() {
        return new MethodId(moduleName,namespaceName,className,methodName);
    }

    public UnitTestMethodResult setMessage(String value) {
        message = value;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public UnitTestMethodResult setStackTrace(String value) {
        stackTrace = value;
        return this;
    }

    public String getStackTrace() {
        return stackTrace;
    }


    /**
     * 
     * @param durationsInMicroSeconds - duration in microSeconds
     */
    public UnitTestMethodResult setDuration(long durationsInMicroSeconds) {
       duration= String.format("%.3f", durationsInMicroSeconds/1000.);
       return this;
    }

}
