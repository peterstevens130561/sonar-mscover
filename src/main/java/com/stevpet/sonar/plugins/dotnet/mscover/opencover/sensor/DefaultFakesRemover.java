package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;
import java.io.FilenameFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultFakesRemover implements FakesRemover {

    FilenameFilter fakesFilter = new FakesFilter();
    private static Logger LOG = LoggerFactory.getLogger(DefaultFakesRemover.class);
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.RemoveFakes#removeFakes(java.io.File)
     */
    public int removeFakes(File myDir) {

        if(myDir==null) {
            return 0;
        }
        File [] files = myDir.listFiles(fakesFilter);
        if(files==null) {
            return 0;
        }
        int removed=0;
        for (File file : files) {
            LOG.warn("Removing fakes file {}",file.getName());
            file.delete();
            removed++;
        }
        return removed;
    }
    

}
