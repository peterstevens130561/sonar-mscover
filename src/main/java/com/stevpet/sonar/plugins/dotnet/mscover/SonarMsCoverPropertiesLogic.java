package com.stevpet.sonar.plugins.dotnet.mscover;

import org.apache.commons.lang.StringUtils;


public class SonarMsCoverPropertiesLogic implements MsCoverPropertiesLogic {
    private final MsCoverProperties properties;
    public SonarMsCoverPropertiesLogic(MsCoverProperties properties) {
        this.properties = properties;
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesLogic#isIntegrationTestsEnabled()
     */
    public boolean isIntegrationTestsEnabled() {
        return StringUtils.isNotEmpty(properties.getIntegrationTestsPath());
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesLogic#isUnitTestsEnabled()
     */
    public boolean isUnitTestsEnabled() {
        return StringUtils.isNotEmpty(properties.getUnitTestCoveragePath());
    }
}
