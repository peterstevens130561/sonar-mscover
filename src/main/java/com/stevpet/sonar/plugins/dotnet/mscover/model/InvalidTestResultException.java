package com.stevpet.sonar.plugins.dotnet.mscover.model;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult.TestResult;

public class InvalidTestResultException extends MsCoverException {

    private static final long serialVersionUID = 1L;

    public InvalidTestResultException(String msg) {
        super(msg);
    }

    public InvalidTestResultException(TestResult outcome) {
        super("method did not handle " + outcome.toString());
    }

}
