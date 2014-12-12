package com.stevpet.sonar.plugins.dotnet.mscover.decorator;

import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class DecoratorContextMock extends GenericClassMock<DecoratorContext> {
    public DecoratorContextMock() {
        super(DecoratorContext.class);
    } 

}
