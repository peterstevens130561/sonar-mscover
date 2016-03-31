package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover;

/**
 * Contains settings directly related to opencover itself, i.e. the directory where OpenCover.Console.Exe is installed, command line arguments etc
 * These are most likely set in the sonar-runner.properties, or at global level
 * @author stevpet
 *
 */
public interface OpenCoverCommandLineConfiguration {
    /**
     * The directory where OpenCover.Console.Exe is installed. 
     * @throws IllegalArgumentValueException when value is not set
     * @return 
     */
    String getInstallDir();
    
    /**
     * Skip auto generated properties (yes/no)
     * @return
     */
    boolean getSkipAutoProps();
    
    /**
     * Used to set the -register:[value] option
     * <br>
     * - null - argument not set
     * <br>
     *  -any string value will lead to -user:[value]
     *  @throws IllegalArgumentValueException when value is not one of user, Path32, Path64
     * @return value of the setting, can be null
     */
    String getRegister();
}
