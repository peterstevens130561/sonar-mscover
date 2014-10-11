package com.stevpet.sonar.plugins.dotnet.mscover;

public interface MsCoverPropertiesLogic {

    public abstract boolean isIntegrationTestsEnabled();

    public abstract boolean isUnitTestsEnabled();

}