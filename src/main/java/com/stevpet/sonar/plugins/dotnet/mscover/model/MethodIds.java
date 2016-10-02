package com.stevpet.sonar.plugins.dotnet.mscover.model;

import java.util.stream.Stream;

public interface MethodIds {

    void add(MethodId methodId);

    Stream<MethodId> stream();

}
