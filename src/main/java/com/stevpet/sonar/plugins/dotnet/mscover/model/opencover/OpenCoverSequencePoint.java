package com.stevpet.sonar.plugins.dotnet.mscover.model.opencover;

public class OpenCoverSequencePoint implements SequencePoint {
    private int line ;
    private int offset ;
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.opencover.SequencePoint#getVisitedCount()
     */
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.opencover.SequencePoint#getLine()
     */
    public int getLine() {
        return line;
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.opencover.SequencePoint#getOffset()
     */
    public int getOffset() {
        return offset;
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.opencover.SequencePoint#setOffset(java.lang.String)
     */
    public void setOffset(String string) {
        this.offset = Integer.parseInt(string);
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.opencover.SequencePoint#setStartLine(java.lang.String)
     */
    public void setStartLine(String string) {
       this.line = Integer.parseInt(string);
        
    }



    
}
