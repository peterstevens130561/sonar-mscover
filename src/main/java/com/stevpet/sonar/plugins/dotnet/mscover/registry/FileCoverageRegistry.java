package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.model.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;

public class FileCoverageRegistry implements LineCoverageRegistry {
    private static final Logger LOG = LoggerFactory
            .getLogger(FileCoverageRegistry.class);

    private Map<Integer, FileCoverage> registry = new HashMap<Integer, FileCoverage>();

    private int lineCount;
    SourceFilePathHelper pathHelper;
 

    private int coveredLineCount;

    public FileCoverageRegistry(String projectDirectory) {
        pathHelper = new SourceFilePathHelper();
        LOG.info("Setting projectDirectory to " + projectDirectory);
        pathHelper.setProjectPath(projectDirectory);
    }

    /**
     * add a point to the file coverage
     * 
     * @param fileId
     *            of file
     * @param point
     *            unique! point
     */
    public void addCoveredLine(int fileId, CoveragePoint point) {
        FileCoverage fileCoverage = getFileCoverage(fileId);
        fileCoverage.addCoveredLine(point);
        lineCount++;
        coveredLineCount++;
    }

    public void addUnCoveredLine(Integer sourceFileID, CoveragePoint point) {
        FileCoverage fileCoverage = getFileCoverage(sourceFileID);
        fileCoverage.addUnCoveredLine(point);
        lineCount++;  
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.CoverageRegistry#addFile(int,
     * java.io.File)
     */
    public void addFile(int fileId, String fullPath)  {
        FileCoverage fileCoverage = getFileCoverage(fileId);
        pathHelper.setFilePath(fullPath);
        if (pathHelper.isModuleInSolution()) {
            String projectPath = pathHelper.getSolutionPath();
            File file = new File(projectPath);
            LOG.info("MsCover path : "+ projectPath);
            fileCoverage.setCanonicalFile(file);
            if (!file.exists()) {
                LOG.warn("Ignoring that file '" + projectPath
                        + "' does not exist. Original :" + fullPath);
            }

        }
    }

    /**
     * get the filecoverage
     * 
     * @param fileId
     *            of file
     * @return
     */
    private FileCoverage getFileCoverage(int fileId) {
        if (!registry.containsKey(fileId)) {
            registry.put(fileId, new FileCoverage(fileId));
        }
        return registry.get(fileId);
    }

    /**
     * Returns all FileCoverage in the registry. Note that sourcefiles that are not in the project will have their File set to null
     * @return
     */
    public Collection<FileCoverage> getFileCoverages() {
        return registry.values();
    }



    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.CoverageRegistry#getFileCount()
     */
    public int getFileCount() {
        return registry.size();
    }

    public int getLineCount() {
        return lineCount;
    }

    public int getCoveredLineCount() {
        return coveredLineCount;
    }
    
    protected Map<Integer, FileCoverage> getRegistry() {
        return registry ;
    }

}
