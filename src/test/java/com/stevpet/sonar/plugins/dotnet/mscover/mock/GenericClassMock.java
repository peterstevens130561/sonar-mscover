package com.stevpet.sonar.plugins.dotnet.mscover.mock;
import static org.mockito.Mockito.mock;
public class GenericClassMock<T> {
    protected T instance = null;
    
    
    public GenericClassMock(Class<T> clazz) {
        instance = mock(clazz);

    }
   
    public T getMock() {
        return instance;
    }
}
;