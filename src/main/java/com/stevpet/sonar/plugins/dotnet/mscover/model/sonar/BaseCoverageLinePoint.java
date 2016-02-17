package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

public abstract class BaseCoverageLinePoint<T extends BaseCoverageLinePoint<?>>  implements CoverageLinePoint<T> {
   protected int line;
   protected int covered;
   protected int toCover;
   

    public int getLine() {
        return line;
    }
    /**
     * @param line the line to set
     */
    public void setLine(int line) {
        this.line = line;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoveragePoint#getCovered()
     */
    @Override
    public int getCovered() {
        return covered;
    }
    
    @Override
    public void setCovered(int covered) {
        this.covered = covered;
    }
    
    public void setCovered(boolean visited) {
        covered= visited?1:0;
    }
    
    public int getToCover() {
        return toCover;
    }
    
    public boolean equals(Object o) {
        if(o==null) {
            return false;
        }
        T other = (T) o;
        return this.covered == other.covered && 
                this.line == other.line &&
                this.toCover == other.toCover;
    }
    
    
    /**
     * merge the coverage information of the other one with this one, updating this.
     * @param other a coveragelinepoint on the same line
     */
}
