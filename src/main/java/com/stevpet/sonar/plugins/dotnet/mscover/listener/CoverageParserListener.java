package com.stevpet.sonar.plugins.dotnet.mscover.listener;


import javax.xml.stream.XMLStreamException;

import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.model.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;

public class CoverageParserListener implements ParserObserver {
    

    private static final String COVERAGE = "Coverage";
    private static final String LINE_ID = "LineID";
    private static final String COL_END = "ColEnd";
    private static final String COL_START = "ColStart";
    private static final String LN_END = "LnEnd";
    private static final String LN_START = "LnStart";
    private static final String SOURCE_FILE_ID = "SourceFileID";
    private int coveredLines;
    private CoverageRegistry registry;
    private int notCoveredLines;


    public void onLine(SMInputCursor linesCursor) throws XMLStreamException{
        SMInputCursor lineCursor = linesCursor.childElementCursor();
        int lnStart = getNextIntElement(lineCursor, LN_START);
        getNextIntElement(lineCursor, COL_START);
        int lnend = getNextIntElement(lineCursor, LN_END);
        getNextIntElement(lineCursor, COL_END);
        int coverage = getNextIntElement(lineCursor, COVERAGE);
        Integer sourceFileID=getNextIntElement(lineCursor, SOURCE_FILE_ID);
        getNextIntElement(lineCursor, LINE_ID);
        
        CoveragePoint point = new CoveragePoint();
        point.setStartLine(lnStart);
        point.setEndLine(lnend);
        
        
        switch(coverage){
        //Covered
        case 0: 
            addCoveredLine(sourceFileID, point);
            break;
        //Error
        case 1:
        //NotCovered
        case 2:
        // Partially covered
        case 3:
            addUncoveredLine(sourceFileID, point);
            break;
        default:
                throw new SonarException("illegal value of coverage " + coverage);
        
        }  
    }

    private void addUncoveredLine(Integer sourceFileID, CoveragePoint point) {
        point.setCountVisits(0);
        registry.addUnCoveredLine(sourceFileID,point);
        setNotCoveredLines(getNotCoveredLines() + 1);
    }

    private void addCoveredLine(Integer sourceFileID, CoveragePoint point) {
        setCoveredLines(getCoveredLines() + 1);
        point.setCountVisits(1);
        registry.addCoveredLine(sourceFileID,point);
    }

    public void onSourceFileNames(SMInputCursor sourceFileNamesCursor) throws XMLStreamException {
            SMInputCursor sourceFileNameCursor = sourceFileNamesCursor
                    .childElementCursor();
            int fileId = getNextElement(sourceFileNameCursor, SOURCE_FILE_ID)
                    .getElemIntValue();
            String fileName = getNextElement(sourceFileNameCursor, "SourceFileName")
                    .getElemStringValue();
            registry.addFile(fileId, fileName);   
    }

    public void setRegistry(CoverageRegistry registry) {
        this.registry = registry;
    }
    
    
    private int getNextIntElement(SMInputCursor cursor, String expectedName)
            throws XMLStreamException {
        return getNextElement(cursor, expectedName).getElemIntValue();
    }

    private SMInputCursor getNextElement(SMInputCursor cursor,
            String expectedName) throws XMLStreamException {
        cursor.getNext();
        String localName = cursor.getLocalName();
        if (!expectedName.equals(localName)) {
            throw new XMLStreamException("expected " + expectedName + " got "
                    + localName);
        }
        return cursor;
    }

    public int getNotCoveredLines() {
        return notCoveredLines;
    }

    public void setNotCoveredLines(int notCoveredLines) {
        this.notCoveredLines = notCoveredLines;
    }

    public int getCoveredLines() {
        return coveredLines;
    }

    public void setCoveredLines(int coveredLines) {
        this.coveredLines = coveredLines;
    }

    public boolean onModuleName(SMInputCursor cursor) {
        // TODO Auto-generated method stub
        return true;
    }

}
