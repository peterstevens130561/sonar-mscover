package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;

public class DefaultIntegrationTestsConfiguration implements IntegrationTestsConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultIntegrationTestsConfiguration.class);
    public static final String MSCOVER = "sonar.mscover.integrationtests.";
    public static final String MSCOVER_INTEGRATION_RESULTS= MSCOVER + "dir";
    public static final String MSCOVER_INTEGRATION_TOOL=MSCOVER+ "tool";
    public static final String MSCOVER_INTEGRATION_MODE=MSCOVER + "mode";
    

	private Settings settings;
    
    public DefaultIntegrationTestsConfiguration(Settings settings) {
    	this.settings = settings;
    }
    
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration#getMode()
	 */
    @Override
	public Mode getMode() {
    	String modeValue=settings.getString(MSCOVER_INTEGRATION_MODE);
    	if(StringUtils.isEmpty(modeValue)) { 
    		return Mode.DISABLED;
    	}
    	Mode mode;
    	try {
    		mode=Enum.valueOf(Mode.class, modeValue.toUpperCase());
    	} catch (IllegalArgumentException e) {
    		LOG.error("invalid property setting '{}={}'. Leave empty or set to one of: disabled run read",MSCOVER_INTEGRATION_MODE,modeValue);
    		throw e;
    	}
    	return mode;
    }
    
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration#getCoverageDir()
	 */
    @Override
	public File getDirectory(){
    	String coverageDir=settings.getString(MSCOVER_INTEGRATION_RESULTS);
    	if(StringUtils.isEmpty(coverageDir)) {
    		LOG.error("property {} must be set to directoy where coverage results should be stored",MSCOVER_INTEGRATION_RESULTS);
    	}
    	return new File(coverageDir);
    }
    
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration#getTool()
	 */
    @Override
	public Tool getTool() {
    	String settingValue=settings.getString(MSCOVER_INTEGRATION_TOOL);
    	if(StringUtils.isEmpty(settingValue)) { 
    		return Tool.OPENCOVER;
    	}
    	Tool tool;
    	try {
    		tool=Enum.valueOf(Tool.class, settingValue.toUpperCase());
    	} catch (IllegalArgumentException e) {
    		LOG.error("invalid property setting '{}={}'. Leave empty or set to one of: disabled run read",MSCOVER_INTEGRATION_MODE,settingValue);
    		throw e;
    	}
    	return tool;  	
    }
    
    /**
     * Used by sensors in shouldExecute() to see if they should run.
     * @param tool
     * @param mode
     * @return
     */
    @Override
	public boolean matches(Tool tool,Mode mode) {
    	if(!(getMode() == mode)) {
    		return false;
    	}
    	if(getMode() == Mode.DISABLED) {
    		return false;
    	}
    	
    	return getTool() == tool;
    	
    }
}
