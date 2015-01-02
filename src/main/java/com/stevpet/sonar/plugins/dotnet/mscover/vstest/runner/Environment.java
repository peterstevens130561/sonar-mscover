package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public class Environment {
    
    enum Result {
        Check,
        Ignore  
    };
    
    private Result result = Result.Check;
    private File assembly ;
    private String assenblyName;
    private MsCoverProperties msCoverProperties;
    
    public void setAssembly(File assemblyFile) {
        this.assembly=assemblyFile;
        setCheck();
    }
    
    public File getAssembly() {
        return assembly ;
    }
    
    public void setResult(Result result) {
        this.result=result;
    }
    
    public Result getResult() {
        return result;
    }

    public boolean isIgnore() {
        return result == Result.Ignore;
    }
    
    public boolean isCheck() {
        return result == Result.Check;
    }

    public boolean exists() {
        return isCheck() && assembly != null && assembly.exists();
    }

    public void setCheck() {
        result = Result.Check;
    }

    public void setIgnore() {
        result = Result.Ignore;
    }

    /**
     * @return the assenblyName
     */
    public String getAssenblyName() {
        return assenblyName;
    }

    /**
     * @param assenblyName the assenblyName to set
     */
    public void setAssenblyName(String assenblyName) {
        this.assenblyName = assenblyName;
    }

    /**
     * @return the msCoverProperties
     */
    public MsCoverProperties getMsCoverProperties() {
        return msCoverProperties;
    }

    /**
     * @param msCoverProperties the msCoverProperties to set
     */
    public void setMsCoverProperties(MsCoverProperties msCoverProperties) {
        this.msCoverProperties = msCoverProperties;
    }
}
