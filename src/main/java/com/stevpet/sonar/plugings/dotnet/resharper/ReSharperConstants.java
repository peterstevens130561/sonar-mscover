/*
 * Sonar .NET Plugin :: ReSharper
 * Copyright (C) 2013 John M. Wright
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
 */
package com.stevpet.sonar.plugings.dotnet.resharper;

/**
 * Constants of the ReSharper plugin.
 */
public final class ReSharperConstants {

    private ReSharperConstants() {
    }

    public static final String REPOSITORY_KEY = "resharper";
    public static final String REPOSITORY_NAME = "ReSharper";
    public static final String[] SUPPORTED_LANGUAGES = new String[] {"cs", "vbnet"};


    // ----------- Plugin Configuration Properties ----------- //
    public static final String MODE = "sonar.resharper.mode";

    public static final String REPORTS_PATH_KEY = "sonar.resharper.reports.path";
    public static final String REPORT_FILENAME = "resharper-report.xml";

    public static final String INSTALL_DIR_KEY = "sonar.resharper.installDirectory";
    public static final String INSTALL_DIR_DEFVALUE = "C:/jetbrains-commandline-tools";

    public static final String TIMEOUT_MINUTES_KEY = "sonar.resharper.timeoutMinutes";
    public static final int TIMEOUT_MINUTES_DEFVALUE = 20;

    public static final String CUSTOM_RULES_PROP_KEY = "sonar.resharper.customRules.definition";
    
    public static final String CUSTOM_SEVERITIES_PROP_KEY = "sonar.resharper.customSeverities.definition";
    public static final String PROFILE_NAME = "sonar.resharper.profile";
    public static final String PROFILE_DEFVALUE="Sonar Way";

    public static final String INCLUDE_ALL_FILES = "sonar.resharper.includeAllFiles";
    public static final String FAIL_ON_ISSUES_KEY = "sonar.resharper.failOnIssues";
	public static final String CUSTOM_SEVERITIES_DEFINITON = "sonar.resharper.customSeverities.definition";
	public static final String CUSTOM_SEVERITIES_PATH = "sonar.resharper.customSeverities.path";
	public static final String INSPECTCODE_PROPERTIES = "sonar.resharper.inspectcode.properties" ;
    public static final String PROFILE_DEFAULT="Sonar way";
    public static final String CACHES_HOME = "sonar.resharper.inspectcode.cacheshome";
    public static final String INSPECTCODE_PROFILE = "sonar.resharper.inspectcode.profile";
	public static final String MODE_REUSE_REPORT = "resuseReport";
}