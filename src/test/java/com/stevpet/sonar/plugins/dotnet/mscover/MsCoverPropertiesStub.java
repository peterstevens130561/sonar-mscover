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
package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;
import java.util.Collection;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultMsCoverConfiguration.RunMode;

public class MsCoverPropertiesStub implements MsCoverConfiguration {

    private boolean integrationTestsEnabled;
    private boolean unitTestsEnabled;
    private String integrationTestsPath;
    private boolean _executeRoot;
    private String openCoverInstallPath;
    private boolean ignoreMissingUnitTestsAssembliesSpecified;
    private Collection<String> pdbsThatMayBeIgnoredWhenMissing;
    private Collection<String> unitTestAssembliesThatCanBeIgnoredIfMissing;
    private String requiredBuildPlatform;
    private String requiredBuildConfiguration;
    private boolean _runOpenCover;
    private boolean _runVsTest;
    private RunMode runMode;
    private List<String> languages;
    private boolean cPlusPlus;
    private boolean msCoverRun;
    private String testSettings;
    private String unitTestAssemblies;
    private String mode;
    private String unitTestResultsPath;
    private String integrationTestResultsPath;
    private String cutOffDate;
    private boolean pluginEnabled;
    private String exclusions;
    private String unitTestCoveragePath;
    private String inclusions;

    public boolean isIntegrationTestsEnabled() {

        return integrationTestsEnabled;
    }

    public boolean isUnitTestsEnabled() {

        return unitTestsEnabled;
    }

    public String getIntegrationTestsPath() {
        return integrationTestsPath;
    }

    public boolean excuteRoot() {
        return _executeRoot;
    }

    public String getUnitTestCoveragePath() {
        return unitTestCoveragePath;
    }

    public String getExclusions() {

        return exclusions;
    }

    public boolean isPluginEnabled() {
        return pluginEnabled;
    }

    public String getInclusions() {
        return inclusions;
    }

    public String getCutOffDate() {
        return cutOffDate;
    }

    public String getIntegrationTestResultsPath() {
        return integrationTestResultsPath;
    }

    public String getUnitTestResultsPath() {
        return unitTestResultsPath;
    }

    public String getMode() {
        return mode;
    }

    public String getUnitTestsAssemblies() {
        return unitTestAssemblies;
    }

    public String getTestSettings() {
        return testSettings;
    }

    public boolean shouldMsCoverRun() {
        return msCoverRun;
    }

    public boolean isCPlusPlus() {
        return cPlusPlus;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public RunMode getRunMode() {
        return runMode;
    }

    public boolean runOpenCover() {
        return _runOpenCover;
    }

    public boolean runVsTest() {
        return _runVsTest;
    }

    public String getRequiredBuildConfiguration() {
        return requiredBuildConfiguration;
    }

    public String getRequiredBuildPlatform() {
        return requiredBuildPlatform;
    }

    public Collection<String> getUnitTestAssembliesThatCanBeIgnoredIfMissing() {
        return unitTestAssembliesThatCanBeIgnoredIfMissing;
    }

    public Collection<String> getPdbsThatMayBeIgnoredWhenMissing() {
        return pdbsThatMayBeIgnoredWhenMissing;
    }

    public boolean isIgnoreMissingUnitTestAssembliesSpecified() {
        return ignoreMissingUnitTestsAssembliesSpecified;
    }

    public String getOpenCoverInstallPath() {
        return openCoverInstallPath;
    }

    /**
     * @param integrationTestsEnabled the integrationTestsEnabled to set
     */
    public void setIntegrationTestsEnabled(boolean integrationTestsEnabled) {
        this.integrationTestsEnabled = integrationTestsEnabled;
    }

    /**
     * @param unitTestsEnabled the unitTestsEnabled to set
     */
    public void setUnitTestsEnabled(boolean unitTestsEnabled) {
        this.unitTestsEnabled = unitTestsEnabled;
    }

    /**
     * @param integrationTestsPath the integrationTestsPath to set
     */
    public void setIntegrationTestsPath(String integrationTestsPath) {
        this.integrationTestsPath = integrationTestsPath;
    }

    /**
     * @param _executeRoot the _executeRoot to set
     */
    public void setExecuteRoot(boolean _executeRoot) {
        this._executeRoot = _executeRoot;
    }

    /**
     * @param openCoverInstallPath the openCoverInstallPath to set
     */
    public void setOpenCoverInstallPath(String openCoverInstallPath) {
        this.openCoverInstallPath = openCoverInstallPath;
    }

    /**
     * @param ignoreMissingUnitTestsAssembliesSpecified the ignoreMissingUnitTestsAssembliesSpecified to set
     */
    public void setIgnoreMissingUnitTestsAssembliesSpecified(
            boolean ignoreMissingUnitTestsAssembliesSpecified) {
        this.ignoreMissingUnitTestsAssembliesSpecified = ignoreMissingUnitTestsAssembliesSpecified;
    }

    /**
     * @param pdbsThatMayBeIgnoredWhenMissing the pdbsThatMayBeIgnoredWhenMissing to set
     */
    public void setPdbsThatMayBeIgnoredWhenMissing(
            Collection<String> pdbsThatMayBeIgnoredWhenMissing) {
        this.pdbsThatMayBeIgnoredWhenMissing = pdbsThatMayBeIgnoredWhenMissing;
    }

    /**
     * @param unitTestAssembliesThatCanBeIgnoredIfMissing the unitTestAssembliesThatCanBeIgnoredIfMissing to set
     */
    public void setUnitTestAssembliesThatCanBeIgnoredIfMissing(
            Collection<String> unitTestAssembliesThatCanBeIgnoredIfMissing) {
        this.unitTestAssembliesThatCanBeIgnoredIfMissing = unitTestAssembliesThatCanBeIgnoredIfMissing;
    }

    /**
     * @param requiredBuildPlatform the requiredBuildPlatform to set
     */
    public void setRequiredBuildPlatform(String requiredBuildPlatform) {
        this.requiredBuildPlatform = requiredBuildPlatform;
    }

    /**
     * @param requiredBuildConfiguration the requiredBuildConfiguration to set
     */
    public void setRequiredBuildConfiguration(String requiredBuildConfiguration) {
        this.requiredBuildConfiguration = requiredBuildConfiguration;
    }

    /**
     * @param _runOpenCover the _runOpenCover to set
     */
    public void setRunOpenCover(boolean _runOpenCover) {
        this._runOpenCover = _runOpenCover;
    }

    /**
     * @param _runVsTest the _runVsTest to set
     */
    public void setRunVsTest(boolean _runVsTest) {
        this._runVsTest = _runVsTest;
    }

    /**
     * @param runMode the runMode to set
     */
    public void setRunMode(RunMode runMode) {
        this.runMode = runMode;
    }

    /**
     * @param languages the languages to set
     */
    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    /**
     * @param cPlusPlus the cPlusPlus to set
     */
    public void setcPlusPlus(boolean cPlusPlus) {
        this.cPlusPlus = cPlusPlus;
    }

    /**
     * @param msCoverRun the msCoverRun to set
     */
    public void setMsCoverRun(boolean msCoverRun) {
        this.msCoverRun = msCoverRun;
    }

    /**
     * @param testSettings the testSettings to set
     */
    public void setTestSettings(String testSettings) {
        this.testSettings = testSettings;
    }

    /**
     * @param unitTestAssemblies the unitTestAssemblies to set
     */
    public void setUnitTestAssemblies(String unitTestAssemblies) {
        this.unitTestAssemblies = unitTestAssemblies;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @param unitTestResultsPath the unitTestResultsPath to set
     */
    public void setUnitTestResultsPath(String unitTestResultsPath) {
        this.unitTestResultsPath = unitTestResultsPath;
    }

    /**
     * @param integrationTestResultsPath the integrationTestResultsPath to set
     */
    public void setIntegrationTestResultsPath(String integrationTestResultsPath) {
        this.integrationTestResultsPath = integrationTestResultsPath;
    }

    /**
     * @param cutOffDate the cutOffDate to set
     */
    public void setCutOffDate(String cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

    /**
     * @param pluginEnabled the pluginEnabled to set
     */
    public void setPluginEnabled(boolean pluginEnabled) {
        this.pluginEnabled = pluginEnabled;
    }

    /**
     * @param exclusions the exclusions to set
     */
    public void setExclusions(String exclusions) {
        this.exclusions = exclusions;
    }

    /**
     * @param unitTestCoveragePath the unitTestCoveragePath to set
     */
    public void setUnitTestCoveragePath(String unitTestCoveragePath) {
        this.unitTestCoveragePath = unitTestCoveragePath;
    }

    public String getUnitTestHintPath() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean getOpenCoverSkipAutoProps() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getIntegrationTestsDir() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getVsTestInstallPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public File getWorkSpaceRoot() {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public String getIntegrationTestsTool() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean integrationTestsToolIsOpenCover() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean integrationTestsToolIsVsTest() {
		// TODO Auto-generated method stub
		return false;
	}

}
