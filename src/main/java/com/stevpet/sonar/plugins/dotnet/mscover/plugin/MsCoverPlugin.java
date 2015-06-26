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

import com.stevpet.sonar.plugings.dotnet.resharper.DefaultInspectCodeResultsParser;
import com.stevpet.sonar.plugings.dotnet.resharper.ReSharperConfiguration;
import com.stevpet.sonar.plugings.dotnet.resharper.ReSharperResultParser;
import com.stevpet.sonar.plugings.dotnet.resharper.ReSharperRuleRepositoryProvider;
import com.stevpet.sonar.plugings.dotnet.resharper.ReSharperSensor;
import com.stevpet.sonar.plugings.dotnet.resharper.profiles.CSharpRegularReSharperProfileExporter;
import com.stevpet.sonar.plugings.dotnet.resharper.profiles.CSharpRegularReSharperProfileImporter;
import com.stevpet.sonar.plugings.dotnet.resharper.profiles.ReSharperProfileExporter;
import com.stevpet.sonar.plugings.dotnet.resharper.profiles.ReSharperProfileImporter;
import com.stevpet.sonar.plugings.dotnet.resharper.profiles.ReSharperSonarWayProfileCSharp;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
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

/**
 * This class is the entry point for all extensions
 */
@Properties({
    @Property(
            key=PropertiesHelper.MSCOVER_MODE,
            name = "runmode: one of skip,runvstest,reuse)",
            defaultValue="",global=false,project=true,type = PropertyType.STRING),
            @Property(
                    key=PropertiesHelper.MSCOVER_COVERAGETOOL,
                    name="coveragetool: one of opencover,vstest (default)",
                    defaultValue="vstest",global=false,project=true,type=PropertyType.STRING),
    @Property(
            key=PropertiesHelper.MSCOVER_TESTSETTINGS,
            name = "testsettings file, required in runmode runvstest)",
            defaultValue="",global=false,project=true,type = PropertyType.STRING),
    @Property(
            key=PropertiesHelper.MSCOVER_UNIT_RESULTS,
            name = "name of results file (.trx)",
            defaultValue="",global=false,project=true,type = PropertyType.STRING),
    @Property(
            key=PropertiesHelper.MSCOVER_UNITTEST_ASSEMBLIES,
            name = "pattern for unit test assemblies",
            defaultValue="",global=false,project=true,type = PropertyType.STRING),
    @Property(
            key=PropertiesHelper.MSCOVER_INTEGRATION_COVERAGEXML_PATH,
            name = "integration tests xml file)",
            defaultValue="",global=false,project=true,type = PropertyType.STRING),
    @Property(
            key=PropertiesHelper.MSCOVER_UNIT_COVERAGEXML_PATH,
            name = "unit tests xml file",
            defaultValue="",global=false,project=true,type = PropertyType.STRING),
    @Property(
            key=PropertiesHelper.MSCOVER_EXECUTEROOT,
            name = "when set only root project is used. Set to true for C++",
            defaultValue="false",global=false,project=true,type = PropertyType.BOOLEAN),   
    @Property(
            key=PropertiesHelper.MSCOVER_INCLUSIONS,
            name = "regular expression to match files that should be included",
            defaultValue="",global=false,project=true,type = PropertyType.STRING),
    @Property(
            key=PropertiesHelper.MSCOVER_CUTOFFDATE,
            name = "files modified before cutoffdate and without coverage, will not be included",
            defaultValue="",global=false,project=true,type = PropertyType.STRING),
    @Property(
            key=PropertiesHelper.MSCOVER_EXCLUSIONS,
            name = "regular expression to match files that should be excluded",
            defaultValue="",global=false,project=true,type = PropertyType.STRING),
    @Property(
            key=PropertiesHelper.MSCOVER_IGNOREMISSING_DLL,
            name="list of dlls that may be ignored if missing",
            defaultValue="",global=false,project=true,type=PropertyType.STRING),
    @Property(
            key=PropertiesHelper.MSCOVER_UNITTEST_HINTPATH,
            name="hintpath for unit testing dlls",
            defaultValue="",global=false,project=true,type=PropertyType.STRING),
    @Property(
            key=PropertiesHelper.MSCOVER_OPENCOVER_SKIPAUTOPROPS,
            name="skip autoproperties",
            defaultValue="true",global=true,project=true,type=PropertyType.BOOLEAN)
    }
)
            
public final class MsCoverPlugin extends SonarPlugin {

  // This is where you're going to declare all your Sonar extensions
  @SuppressWarnings({ "rawtypes" })
public List getExtensions() {
      
    return Arrays.asList(
        SimpleMicrosoftWindowsEnvironment.class,
        VsTestEnvironment.class,
        PropertiesHelper.class,
        VisualStudioProjectBuilder.class,
        IntegrationTestLineDecorator.class,
        UnitTestLineDecorator.class,
        IntegrationTestBlockDecorator.class,
        DefaultDirector.class,
        UnitTestBlockDecorator.class,
        UnitTestWorkflowSensor.class,
        IntegrationTestWorkflowSensor.class,
        SupportedLanguage.class,
        CSharpRegularReSharperProfileExporter.class,
        CSharpRegularReSharperProfileImporter.class,
        ReSharperConfiguration.class,
        ReSharperSonarWayProfileCSharp.class,
        ReSharperResultParser.class,
        ReSharperRuleRepositoryProvider.class,
        ReSharperSensor.class,
        DefaultInspectCodeResultsParser.class,
        DefaultResourceResolver.class
        );
  }
}
