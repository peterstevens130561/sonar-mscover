package com.stevpet.sonar.plugins.dotnet.mscover.model;

public class ResultsModel {
    private int executedTests;
    private int passedTests;
    private int failedTests;
    private int erroredTests;

    public ResultsModel() {
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
        // TODO Auto-generated method stub
        return erroredTests;
    }

    public void add(UnitTestFileResultModel fileResults) {
        this.passedTests += fileResults.getPassed();
        this.failedTests += fileResults.getFail();
        this.executedTests = this.passedTests + this.failedTests;
    }
}