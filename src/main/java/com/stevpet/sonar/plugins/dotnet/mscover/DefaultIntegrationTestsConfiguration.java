package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Mode;

public class DefaultIntegrationTestsConfiguration implements IntegrationTestsConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultIntegrationTestsConfiguration.class);
    private static final String MSCOVER = "sonar.mscover.integrationtests.";
    private static final String MSCOVER_INTEGRATION_RESULTS= MSCOVER + "dir";
    private static final String MSCOVER_INTEGRATION_TOOL=MSCOVER+ "tool";
    private static final String MSCOVER_INTEGRATION_MODE=MSCOVER + "mode";
	static final String MSCOVER_SPECFLOWTESTS_ROOT = DefaultIntegrationTestsConfiguration.MSCOVER + "root";
	private static final String MSCOVER_INTEGRATION_TESTCASEFILTER= DefaultIntegrationTestsConfiguration.MSCOVER + "testcasefilter";

	private Settings settings;
	private FileSystem fileSystem;
	
    public DefaultIntegrationTestsConfiguration(Settings settings,FileSystem fileSystem) {
		this.settings = settings;
		this.fileSystem=fileSystem;
		
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
		if(mode==Mode.AUTO) {
			mode = isModulePathChildOfRootPath()?Mode.RUN:Mode.READ;	
		}
    	return mode;
    }
    
	private boolean isModulePathChildOfRootPath() {
		String modulePath=fileSystem.baseDir().getAbsolutePath();
		String rootPath=settings.getString(MSCOVER_SPECFLOWTESTS_ROOT);
		Preconditions.checkNotNull(rootPath,"property not set: " + MSCOVER_SPECFLOWTESTS_ROOT);
		String windowsPath = rootPath.replaceAll("/","\\\\");
		return  modulePath.contains(windowsPath);
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

	@Override
	public String getTestCaseFilter() {

		return settings.getString(MSCOVER_INTEGRATION_TESTCASEFILTER);
	}
}
