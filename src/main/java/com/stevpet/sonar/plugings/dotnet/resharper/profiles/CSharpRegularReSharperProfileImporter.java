package com.stevpet.sonar.plugings.dotnet.resharper.profiles;

import org.sonar.api.rules.RuleFinder;

public class CSharpRegularReSharperProfileImporter extends ReSharperProfileImporter {
    public CSharpRegularReSharperProfileImporter(RuleFinder ruleFinder) {
        super("cs", ruleFinder);
    }
}