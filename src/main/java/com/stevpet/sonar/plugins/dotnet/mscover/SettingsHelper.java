package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nonnull;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;


public class SettingsHelper {
    private Logger LOG = LoggerFactory.getLogger(SettingsHelper.class);
    private Settings settings;

    public SettingsHelper(Settings settings) {
        this.settings=settings ; }
    
    /**
     * get Pattern for the regular expression that the property defines
     * @param property
     * @return pattern
     * @throws IllegalStateException when pattern is invalid
     */
    public Pattern getPattern(@Nonnull String property){
        String value =settings.getString(property);
        if(StringUtils.isEmpty(value)) {
            return null ;
        }
        Pattern pattern = null ;
        try {
            pattern=Pattern.compile(value);
        } catch (PatternSyntaxException e) {
            String msg = "Property value is not a valid regular expression:" + pattern + "=" + value;
            LOG.error(msg);
            throw new IllegalStateException(msg,e);
        }
        return pattern;
        
    }
}
