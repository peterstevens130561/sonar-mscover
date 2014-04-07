package com.stevpet.sonar.plugins.dotnet.mscover.model;


public class LineModel extends Model {
    
    private static final String COVERAGE_ELEMENT = "Coverage";
    private static final String LN_START_ELEMENT = "LnStart";
    private static final String SOURCE_FILE_ID_ELEMENT = "SourceFileID";
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
    
    public void setField(String name, String text) {
        if (name.equals(LN_START_ELEMENT)) {
            setLnStart(text);
        }
        if (name.equals(COVERAGE_ELEMENT)) {
            setCoverage(text);
        }
    }

    public String getFirstElement() {
        return LN_START_ELEMENT;
    }
    
    public  String getLastElement() {
        return SOURCE_FILE_ID_ELEMENT;
    }

}
