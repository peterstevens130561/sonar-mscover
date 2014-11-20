package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;
import java.io.FilenameFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultFakesRemover implements FakesRemover {

    private static Logger LOG = LoggerFactory.getLogger(DefaultFakesRemover.class);
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.RemoveFakes#removeFakes(java.io.File)
     */
    public void removeFakes(File myDir) {
        if(myDir==null) {
            return;
        }
        File [] files = myDir.listFiles(new FakesFilter());
        if(files==null) {
            return;
        }
        for (File file : files) {
            LOG.warn("Removing fakes file {}",file.getName());
            file.delete();
        }
    }
    
    private class FakesFilter implements FilenameFilter {

        public boolean accept(File dir, String name) {
            return name.contains("fakes");
        }
        
    }

}
