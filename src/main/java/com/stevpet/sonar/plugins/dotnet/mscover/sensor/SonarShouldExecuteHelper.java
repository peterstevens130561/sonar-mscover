package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public class SonarShouldExecuteHelper implements ShouldExecuteHelper {
    
    static final Logger LOG = LoggerFactory
            .getLogger(SonarShouldExecuteHelper.class);
    private MsCoverProperties propertiesHelper;
    public SonarShouldExecuteHelper(MsCoverProperties propertiesHelper) {
        this.propertiesHelper = propertiesHelper;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.sensor.ShouldExecuteHelper#shouldExecuteOnProject(org.sonar.api.resources.Project)
     */
    public boolean shouldExecuteOnProject(Project project) {
        if(propertiesHelper.getRunMode() == RunMode.SKIP) {
            return false;
        }
        if (project == null) {
            LOG.error("MSCover : project is null, will not execute");
            return false;
            
        }
        if(project.isRoot() != propertiesHelper.excuteRoot()) {
            LOG.info("MSCover : Skipping project project.isRoot() =" + project.isRoot() + ", " + PropertiesHelper.MSCOVER_EXECUTEROOT + "=" + propertiesHelper.excuteRoot());
            return false;
        }
        return true;
    }

}
