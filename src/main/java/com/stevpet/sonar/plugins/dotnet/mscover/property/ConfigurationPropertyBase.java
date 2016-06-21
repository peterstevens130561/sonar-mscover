package com.stevpet.sonar.plugins.dotnet.mscover.property;


import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nonnull;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.config.PropertyDefinition.Builder;


import com.google.common.base.Preconditions;

public abstract class ConfigurationPropertyBase<T> implements ConfigurationProperty<T> {

    private final Settings settings;
    public ConfigurationPropertyBase(Settings settings) {
        this.settings = settings ; 
    }
    public ConfigurationPropertyBase() {
        settings=null;
    }
    public T getValue() {
        Preconditions.checkNotNull(settings);
        return onGetValue(settings);
    }
    
    protected abstract T onGetValue(Settings settings);
    
    public void validate() {
        getValue();
    }
    
    protected Builder createProperty(String key, PropertyType propertyType) {
        return PropertyDefinition.builder(key).type(propertyType).subCategory("Integration tests");

    }
    
    protected  Pattern getPattern(@Nonnull String property){
        String value =settings.getString(property);
        if(StringUtils.isEmpty(value)) {
            return null ;
        }
        Pattern pattern = null ;
        try {
            pattern=Pattern.compile(value);
        } catch (PatternSyntaxException e) {
            String msg = "Property value is not a valid regular expression:" + pattern + "=" + value;
            throw new IllegalStateException(msg,e);
        }
        return pattern;
        
    }

    public ConfigurationPropertyBase register(List<ConfigurationProperty> list) {
        list.add(this);
        return this;
    }

}
