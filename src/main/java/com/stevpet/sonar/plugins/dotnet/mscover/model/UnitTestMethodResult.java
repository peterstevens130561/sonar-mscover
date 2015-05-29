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
    private MethodId methodId = new MethodId();
    private String testId;
    private String duration;
    private String outcome;
    private String relativeResultsDirectory;
    private String codeBase;
    private String message = StringUtils.EMPTY;
    private String stackTrace;
    private UnitTestingResults parent;

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
        return methodId.getModuleName();
    }

    public String getNamespaceName() {
        return methodId.getNamespaceName();
    }

    public String getTestId() {
        return testId;
    }

    public UnitTestMethodResult setTestId(String testId) {
        this.testId = testId;
        return this;
    }

    public String getTestName() {
        return methodId.getMethodName();
    }

    public UnitTestMethodResult setTestName(String testName) {
        methodId.setMethodName(testName);
        return this;
    }

    public String getDuration() {
        return duration;
    }

    public String getCodeBase() {
        return codeBase;
    }

    public String getClassName() {
        return methodId.getClassName();
    }

    public UnitTestMethodResult setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getOutcome() {
        return outcome;
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
        methodId.setModuleName(parts[parts.length - 1]);
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
        String namespaceName;
        if (lastDot == -1) {
            namespaceName = StringUtils.EMPTY;
        } else {
            namespaceName = value.substring(0, lastDot);
        }
        methodId.setNamespaceName(namespaceName);
        return this;
    }

    public UnitTestMethodResult setClassName(String value) {
        if (StringUtils.isEmpty(value)) {
            throw new SonarException("className can't be null");
        }
        int lastDot = value.lastIndexOf(".");
        String className;
        if (lastDot == -1) {
            className = value;
        } else {
            className = value.substring(lastDot + 1);
        }
        methodId.setClassName(className);
        return this;
    }

    public MethodId getMethodId() {
        return methodId;
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

}
