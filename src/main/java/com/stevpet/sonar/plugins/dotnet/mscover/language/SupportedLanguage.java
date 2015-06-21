package com.stevpet.sonar.plugins.dotnet.mscover.language;

import org.sonar.api.resources.AbstractLanguage;

public class SupportedLanguage extends AbstractLanguage {

    public SupportedLanguage() {
        super("cs", "C#");
    }

    @Override
    public String[] getFileSuffixes() {
        String suffixes[] = { ".cs" };
        return suffixes;
    }
}
