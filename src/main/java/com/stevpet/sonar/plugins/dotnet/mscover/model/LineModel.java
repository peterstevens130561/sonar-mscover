package com.stevpet.sonar.plugins.dotnet.mscover.model;


public class LineModel extends Model {
    
    private int lnStart;
    private int coverage;

    public void setLnStart(String text) {
        lnStart = Integer.parseInt(text);
    }
    
    public void setCoverage(String text) {
        coverage = Integer.parseInt(text);
    }
    
    public boolean isCovered() {
        return coverage == 0;
    }
    
    public int getLnStart() {
        return lnStart;
    }

}
