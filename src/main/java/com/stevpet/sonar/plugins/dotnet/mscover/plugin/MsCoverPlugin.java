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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.PropertyType;
import org.sonar.api.SonarPlugin;

import com.stevpet.sonar.plugins.common.commandexecutor.DefaultProcessLock;
import com.stevpet.sonar.plugins.common.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor.BuildWrapperBuilder;
import com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor.BuildWrapperCache;
import com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor.BuildWrapperConstants;
import com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor.BuildWrapperInitializer;
import com.stevpet.sonar.plugins.dotnet.mscover.DefaultMsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.decorator.UnitTestBlockDecorator;
import com.stevpet.sonar.plugins.dotnet.mscover.language.SupportedLanguage;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.IntegrationTestResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.DefaultDirector;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.IntegrationTestCache;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.UnitTestCache;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor.UnitTestWorkflowSensor;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.DefaultMicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.VisualStudioConfiguration;

/**
 * This class is the entry point for all extensions
 */
@Properties({
        @Property(key = DefaultMsCoverConfiguration.MSCOVER_MODE, name = "runmode: one of skip,runvstest,reuse)", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = DefaultMsCoverConfiguration.MSCOVER_COVERAGETOOL, name = "coveragetool: one of opencover,vstest (default)", defaultValue = "vstest", global = false, project = true, type = PropertyType.STRING),
        @Property(key = DefaultMsCoverConfiguration.MSCOVER_TESTSETTINGS, name = "testsettings file, required in runmode runvstest)", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = DefaultMsCoverConfiguration.MSCOVER_UNIT_RESULTS, name = "name of results file (.trx)", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = DefaultMsCoverConfiguration.MSCOVER_UNITTEST_ASSEMBLIES, name = "pattern for unit test assemblies", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = DefaultMsCoverConfiguration.MSCOVER_INTEGRATION_COVERAGEXML_PATH, name = "integration tests xml file)", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = DefaultMsCoverConfiguration.MSCOVER_UNIT_COVERAGEXML_PATH, name = "unit tests xml file", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = DefaultMsCoverConfiguration.MSCOVER_EXECUTEROOT, name = "when set only root project is used. Set to true for C++", defaultValue = "false", global = false, project = true, type = PropertyType.BOOLEAN),
        @Property(key = DefaultMsCoverConfiguration.MSCOVER_INCLUSIONS, name = "regular expression to match files that should be included", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = DefaultMsCoverConfiguration.MSCOVER_CUTOFFDATE, name = "files modified before cutoffdate and without coverage, will not be included", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = DefaultMsCoverConfiguration.MSCOVER_EXCLUSIONS, name = "regular expression to match files that should be excluded", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = DefaultMsCoverConfiguration.MSCOVER_IGNOREMISSING_DLL, name = "list of dlls that may be ignored if missing", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = DefaultMsCoverConfiguration.MSCOVER_UNITTEST_HINTPATH, name = "hintpath for unit testing dlls", defaultValue = "", global = false, project = true, type = PropertyType.STRING),
        @Property(key = DefaultMsCoverConfiguration.MSCOVER_OPENCOVER_SKIPAUTOPROPS, name = "skip autoproperties", defaultValue = "true", global = true, project = true, type = PropertyType.BOOLEAN)
        })
public final class MsCoverPlugin extends SonarPlugin {

    // This is where you're going to declare all your Sonar extensions
    @SuppressWarnings({ "rawtypes" })
    public List getExtensions() {

        List clazzes=Arrays.asList(
        		//VisualStudioAssemblyLocator.class,
                DefaultMicrosoftWindowsEnvironment.class,
                VsTestEnvironment.class,
                DefaultMsCoverConfiguration.class,
                //VisualStudioProjectBuilder.class,
                UnitTestCache.class,
                DefaultDirector.class, UnitTestBlockDecorator.class,
                UnitTestWorkflowSensor.class,
                IntegrationTestCache.class,
                SupportedLanguage.class,
                DefaultProcessLock.class,

                DefaultResourceResolver.class, 
                IntegrationTestResourceResolver.class,
                
                WindowsCommandLineExecutor.class,
                BuildWrapperInitializer.class,
                BuildWrapperBuilder.class,
                BuildWrapperCache.class,
                
                VisualStudioConfiguration.class
                )
                ;
        List extensions = new ArrayList();
        extensions.addAll(clazzes);
        
        extensions.addAll(BuildWrapperConstants.getProperties());
        extensions.addAll(DefaultMsCoverConfiguration.getProperties());
        extensions.addAll(VisualStudioConfiguration.getProperties());
        return extensions;
    }
}
