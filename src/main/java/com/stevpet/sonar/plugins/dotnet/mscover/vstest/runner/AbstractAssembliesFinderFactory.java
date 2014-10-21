package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public interface AbstractAssembliesFinderFactory {
    AssembliesFinder create(MsCoverProperties propertiesHelper);
}