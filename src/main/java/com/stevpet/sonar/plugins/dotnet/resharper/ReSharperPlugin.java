package com.stevpet.sonar.plugins.dotnet.resharper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sonar.api.SonarPlugin;

import com.stevpet.sonar.plugins.dotnet.resharper.inspectcode.ReSharperCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.DefaultIssueValidator;
import com.stevpet.sonar.plugins.dotnet.resharper.profiles.CSharpRegularReSharperProfileExporter;
import com.stevpet.sonar.plugins.dotnet.resharper.profiles.CSharpRegularReSharperProfileImporter;
import com.stevpet.sonar.plugins.dotnet.resharper.profiles.ReSharperSonarWayProfileCSharp;
import com.stevpet.sonar.plugins.dotnet.resharper.saver.DefaultInspectCodeIssuesSaver;

public class ReSharperPlugin extends SonarPlugin {

    @Override
    public List getExtensions() {
        List imported=Arrays.asList();
        List exported=Arrays.asList(
                ReSharperConfiguration.class,
                CSharpRegularReSharperProfileExporter.class,
                CSharpRegularReSharperProfileImporter.class,
                ReSharperSonarWayProfileCSharp.class,
                ReSharperRuleRepositoryProvider.class, 
                ReSharperCommandBuilder.class,
                DefaultInspectCodeIssuesSaver.class,
                InspectCodeBatchData.class,
                DefaultInspectCodeRunner.class,
                DefaultIssueValidator.class,
                DefaultReSharperWorkflow.class,
                ReSharperSensor.class);
        List extensions = new ArrayList();
        extensions.addAll(imported);
        extensions.addAll(exported);
        extensions.addAll(ReSharperConfiguration.getProperties());
        return extensions;
    }

}
