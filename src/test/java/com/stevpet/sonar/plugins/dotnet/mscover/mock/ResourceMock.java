package com.stevpet.sonar.plugins.dotnet.mscover.mock;

import org.sonar.api.resources.Resource;

import static org.mockito.Mockito.when;

public class ResourceMock extends GenericClassMock<Resource> {
    public ResourceMock() {
        super(Resource.class);
    }

    /**
     * 
     * @param scope - the string to return when getScope is used
     */
    public void givenScope(String scope) {
        when(instance.getScope()).thenReturn(scope); 
    }

    /**
     *
     * @param longName - the string to return when getLongName is used
     */
    public void givenLongName(String longName) {
        when(instance.getLongName()).thenReturn(longName);
    }
}
