package com.stevpet.sonar.plugins.dotnet.mscover.property;

import org.sonar.api.config.PropertyDefinition.Builder;

/**
 * 
 * @author stevpet
 *
 */
public interface ConfigurationProperty<T> {

    /**
     * to be used in he overall Configuration class, which will complete the buil, i.e. add the index
     * {@code new SomeConfiguratonProperty().getBuilder().index(6).build()}
     * @return 
     */
    public Builder getPropertyBuilder() ;
    
    /**
     * get the property value
     * @return 
     */
    public T getValue() ;
    
    /**
     * validate the property value
     * @throws InvalidPropertyValueException
     */
    public void validate();

    /**
     * get the propertykey
     * @return value of the property key
     */
    String getKey();
   
    
}
