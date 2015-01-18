package com.stevpet.sonar.plugins.dotnet.mscover.opencover.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;

/**
 * Very simple apprach to an interprocesslock
 * @author stevpet
 *
 */
public class ProcessLock {
    private static Logger LOG = LoggerFactory.getLogger(ProcessLock.class);
    private File lockFile;
    private FileChannel channel;
    private FileLock lock;
    public ProcessLock(File lockFile) {
        this.lockFile = lockFile;
    }
    
    public void lock() {
        try {
            channel = new RandomAccessFile(lockFile,"rw").getChannel();
        } catch (FileNotFoundException e) {
            throw new SonarException("Could not find lockFile " + lockFile.getAbsolutePath(),e);
        }
        try {
            LOG.info("Acquiring processlock on " + lockFile.getAbsolutePath());
            lock = channel.lock();
            LOG.info("Acquired processlock on " + lockFile.getAbsolutePath());
        } catch (IOException e) {

            throw new SonarException("Could not lock " + lockFile.getAbsolutePath(),e);
        }
    }
    
    public void release() {
        try {
            lock.release();
        } catch (IOException e) {
            throw new SonarException("Could not release " + lockFile.getAbsolutePath(),e);
        }
        try {
            channel.close();
        } catch (IOException e) {
            throw new SonarException("Could not release " + lockFile.getAbsolutePath(),e);
        }
        LOG.info("Released processlock on " + lockFile.getAbsolutePath());
    }
}
