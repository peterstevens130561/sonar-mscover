package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository;

import java.util.stream.Stream;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;

public interface MethodIds {

    void add(MethodId methodId);

    Stream<MethodId> stream();

}
