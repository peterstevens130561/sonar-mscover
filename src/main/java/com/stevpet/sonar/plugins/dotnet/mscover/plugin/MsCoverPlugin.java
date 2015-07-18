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
package com.stevpet.sonar.plugins.dotnet.mscover.plugin;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.PropertyType;
import org.sonar.api.SonarPlugin;

import com.stevpet.sonar.plugings.dotnet.resharper.DefaultInspectCodeIssuesSaver;
import com.stevpet.sonar.plugings.dotnet.resharper.DefaultInspectCodeResultsParser;
import com.stevpet.sonar.plugings.dotnet.resharper.DefaultInspectCodeRunner;
import com.stevpet.sonar.plugings.dotnet.resharper.ReSharperRuleRepositoryProvider;
import com.stevpet.sonar.plugings.dotnet.resharper.ReSharperSensor;
import com.stevpet.sonar.plugings.dotnet.resharper.inspectcode.ReSharperCommandBuilder;
import com.stevpet.sonar.plugings.dotnet.resharper.profiles.CSharpRegularReSharperProfileExporter;
import com.stevpet.sonar.plugings.dotnet.resharper.profiles.CSharpRegularReSharperProfileImporter;
import com.stevpet.sonar.plugings.dotnet.resharper.profiles.ReSharperSonarWayProfileCSharp;
import com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor.BuildWrapperBuilder;
import com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor.BuildWrapperConstants;
import com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor.BuildWrapperInitializer;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.decorator.IntegrationTestBlockDecorator;
import com.stevpet.sonar.plugins.dotnet.mscover.decorator.IntegrationTestLineDecorator;
import com.stevpet.sonar.plugins.dotnet.mscover.decorator.UnitTestBlockDecorator;
import com.stevpet.sonar.plugins.dotnet.mscover.decorator.UnitTestLineDecorator;
import com.stevpet.sonar.plugins.dotnet.mscover.language.SupportedLanguage;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.DefaultDirector;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor.IntegrationTestWorkflowSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor.UnitTestWorkflowSensor;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.SimpleMicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.VisualStudioProjectBuilder;
import com.stevpet.sonar.plugings.dotnet.resharper.ReSharperConstants;

/**
 * This class is the entry point for all extensions
 */
@Properties({
        @Property(key = PropertiesHelper.MSCOVER_MODE, name = "runmode: one of skip,runvstest,reuse)", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = PropertiesHelper.MSCOVER_COVERAGETOOL, name = "coveragetool: one of opencover,vstest (default)", defaultValue = "vstest", global = false, project = true, type = PropertyType.STRING),
        @Property(key = PropertiesHelper.MSCOVER_TESTSETTINGS, name = "testsettings file, required in runmode runvstest)", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = PropertiesHelper.MSCOVER_UNIT_RESULTS, name = "name of results file (.trx)", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = PropertiesHelper.MSCOVER_UNITTEST_ASSEMBLIES, name = "pattern for unit test assemblies", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH, name = "integration tests xml file)", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = PropertiesHelper.MSCOVER_UNIT_COVERAGEXML_PATH, name = "unit tests xml file", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = PropertiesHelper.MSCOVER_EXECUTEROOT, name = "when set only root project is used. Set to true for C++", defaultValue = "false", global = false, project = true, type = PropertyType.BOOLEAN),
        @Property(key = PropertiesHelper.MSCOVER_INCLUSIONS, name = "regular expression to match files that should be included", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = PropertiesHelper.MSCOVER_CUTOFFDATE, name = "files modified before cutoffdate and without coverage, will not be included", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = PropertiesHelper.MSCOVER_EXCLUSIONS, name = "regular expression to match files that should be excluded", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = PropertiesHelper.MSCOVER_IGNOREMISSING_DLL, name = "list of dlls that may be ignored if missing", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = PropertiesHelper.MSCOVER_UNITTEST_HINTPATH, name = "hintpath for unit testing dlls", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = PropertiesHelper.MSCOVER_OPENCOVER_SKIPAUTOPROPS, name = "skip autoproperties", defaultValue = "true", global = true, project = true, type = PropertyType.BOOLEAN),
        @Property(key = ReSharperConstants.MODE, defaultValue = "", name = "ReSharper activation mode", description = "Possible values : empty (means active), 'skip' and 'reuseReport'.", global = false, project = false, type = PropertyType.SINGLE_SELECT_LIST, options = {
                "skip", "reuseReport" }),
        @Property(key = ReSharperConstants.REPORTS_PATH_KEY, defaultValue = "", name = "Path of the ReSharper report file(s)", description = "Path of the ReSharper report file(s) used when reuse report mode is activated. "
                + "This can be an absolute path, or a path relative to each project base directory.", global = false, project = false),
        @Property(key = ReSharperConstants.INSTALL_DIR_KEY, defaultValue = ReSharperConstants.INSTALL_DIR_DEFVALUE, name = "ReSharper Command Line Tools install directory", description = "Absolute path of the ReSharper Command Line Tools installation folder.", global = true, project = false),
        @Property(key = ReSharperConstants.TIMEOUT_MINUTES_KEY, defaultValue = ReSharperConstants.TIMEOUT_MINUTES_DEFVALUE + "", name = "ReSharper program timeout", description = "Maximum number of minutes before the ReSharper program will be stopped.", global = true, project = true, type = PropertyType.INTEGER),
        @Property(key = ReSharperConstants.INCLUDE_ALL_FILES, defaultValue = "true", name = "ReSharper file inclusion mode", description = "Determines if violations are reported on any file (ignores filters and unsupported file types) or only those supported by the dotNet core plugin.", global = false, project = false, type = PropertyType.BOOLEAN),
        @Property(key = ReSharperConstants.CUSTOM_SEVERITIES_DEFINITON, defaultValue = "", name = "ReSharper custom severities", description = "Add &lt;String&gt; vales from ReSharper's custom definitions (including &lt:wpf:ResourceDictionary&gt;) A restart is required to take affect.", type = PropertyType.TEXT, global = true, project = false),
        @Property(key = ReSharperConstants.PROFILE_NAME, defaultValue = ReSharperConstants.PROFILE_DEFAULT, name = "Profile", description = "Profile to which rules will be saved on restart, if profile does not exist", type = PropertyType.STRING, global = true, project = false),
        @Property(key = ReSharperConstants.CUSTOM_SEVERITIES_PATH, name = "Path to custom severities settings", description = "Absolute path to file with exported ReSharper settings: RESHARPER, Manage Options...,Import/Export Settiings, Export to file,CodeInspection", type = PropertyType.STRING, global = true, project = false),
        @Property(key = ReSharperConstants.INSPECTCODE_PROPERTIES, name = "properties argument for inspectcode", type = PropertyType.STRING, global = true, project = true),
        @Property(key = ReSharperConstants.CACHES_HOME, name = "caches home", type = PropertyType.STRING, global = false, project = true),
        @Property(key = ReSharperConstants.INSPECTCODE_PROFILE, name = "path to .DotSettings file on server", type = PropertyType.STRING, global = false, project = true),
        @Property(key = ReSharperConstants.FAIL_ON_EXCEPTION_KEY, name = "fail analysis on thrown exception", type = PropertyType.BOOLEAN,global=true,project=false,defaultValue="true"),
        @Property(key= BuildWrapperConstants.BUILDWRAPPER_INSTALLDIR_KEY, name="directory where buildwrapper is installed", type=PropertyType.STRING,global=true,project=true),
        @Property(key= BuildWrapperConstants.BUILDWRAPPER_MSBUILD_OPTIONS_KEY, name="msbuild options",type=PropertyType.STRING,global=true,project=true),
        @Property(key=ReSharperConstants.BUILD_CONFIGURATION_KEY, name="Configuration",type=PropertyType.STRING,global=true,project=true,defaultValue=ReSharperConstants.BUILD_CONFIGURATIONS_DEFVALUE),
        @Property(key=ReSharperConstants.BUILD_PLATFORM_KEY, name="Platform",type=PropertyType.STRING,global=true,project=true,defaultValue=ReSharperConstants.BUILD_PLATFORM_DEFVALUE),
        @Property(key= BuildWrapperConstants.BUILDWRAPPER_ENABLED_KEY, name="buildwrapper enabled",type=PropertyType.BOOLEAN,global=true,project=true),
        @Property(key= BuildWrapperConstants.BUILDWRAPPER_OUTDIR_KEY,name="buildwrapper output dir",global=true,project=true)})
public final class MsCoverPlugin extends SonarPlugin {

    // This is where you're going to declare all your Sonar extensions
    @SuppressWarnings({ "rawtypes" })
    public List getExtensions() {

        return Arrays.asList(SimpleMicrosoftWindowsEnvironment.class,
                VsTestEnvironment.class,
                PropertiesHelper.class,
                VisualStudioProjectBuilder.class,
                IntegrationTestLineDecorator.class,
                UnitTestLineDecorator.class,
                IntegrationTestBlockDecorator.class,
                DefaultDirector.class, UnitTestBlockDecorator.class,
                UnitTestWorkflowSensor.class,
                IntegrationTestWorkflowSensor.class,
                SupportedLanguage.class,
                CSharpRegularReSharperProfileExporter.class,
                CSharpRegularReSharperProfileImporter.class,
                ReSharperSonarWayProfileCSharp.class,
                ReSharperRuleRepositoryProvider.class, 
                ReSharperSensor.class, 
                DefaultInspectCodeResultsParser.class,
                DefaultResourceResolver.class, 
                DefaultInspectCodeIssuesSaver.class,
                DefaultInspectCodeRunner.class,
                ReSharperCommandBuilder.class,
                WindowsCommandLineExecutor.class,
                BuildWrapperInitializer.class,
                BuildWrapperBuilder.class)
                ;
    }
}
