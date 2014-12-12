package com.stevpet.sonar.plugins.dotnet.mscover.mock;
import static org.mockito.Mockito.mock;
public class GenericClassMock<T> implements ClassMock<T> {
    protected T instance = null;
    
    
    public GenericClassMock(Class<T> clazz) {
        instance = mock(clazz);

    }
   
     /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.mock.ClassMock#getMock()
     */
    public T getMock() {
        return instance;
    }
}
;