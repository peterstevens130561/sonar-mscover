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

public class TestResults {
    private int executedTests;
    private int passedTests;
    private int failedTests;
    private int erroredTests;

    public TestResults() {
    }

    public int getExecutedTests() {
        return executedTests;
    }

    public void setExecutedTests(int executedTests) {
        this.executedTests = executedTests;
    }

    public int getPassedTests() {
        return passedTests;
    }

    public void setPassedTests(int passedTests) {
        this.passedTests = passedTests;
    }

    public int getFailedTests() {
        return failedTests;
    }

    public void setFailedTests(int failedTests) {
        this.failedTests = failedTests;
    }

    public void setErroredTests(int erroredTests) {
        this.erroredTests = erroredTests;
    }
    public double getErroredTests() {
        return erroredTests;
    }

    public void add(UnitTestClassResult fileResults) {
        this.passedTests += fileResults.getPassed();
        this.failedTests += fileResults.getFail();
        this.executedTests = this.passedTests + this.failedTests;
    }
}