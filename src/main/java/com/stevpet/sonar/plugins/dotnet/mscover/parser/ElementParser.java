package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import static org.sonar.plugins.csharp.gallio.helper.StaxHelper.findElementName;

import javax.xml.stream.XMLStreamException;

import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.listener.BaseParserListener;
import com.stevpet.sonar.plugins.dotnet.mscover.listener.ParserObserver;

public class ElementParser {

    private static String SIGNATURE_ELEMENT = "CoverageDSPriv";
    

    private ParserObserver listener =  new BaseParserListener() ;

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.CoverageParser#isCompatible(org.codehaus.staxmate.in.SMInputCursor)
     */
    public boolean isCompatible(SMInputCursor rootCursor) {
        String elementName = findElementName(rootCursor);
        return SIGNATURE_ELEMENT.equals(elementName);
    }

    
    public void setListener(ParserObserver listener) {
        this.listener = listener ;
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.CoverageParser#parse(org.codehaus.staxmate.in.SMInputCursor)
     */
    public void parse(
            SMInputCursor startElementCursor) {
        try {
            tryParseFile(startElementCursor);
        } catch (XMLStreamException e) {
            throw new SonarException(e);
        }
    }

    private void tryParseFile(SMInputCursor cursor)
            throws XMLStreamException {
        SMInputCursor childCursor = cursor.childElementCursor();
        while (childCursor.getNext() != null) {
            String elementName = childCursor.getLocalName();
            parseModule(childCursor,elementName);
            parseSourceFileNames(childCursor,elementName);
        }
    }

    private void parseSourceFileNames(SMInputCursor cursor,String elementName) throws XMLStreamException {

        if ("SourceFileNames".equals(elementName)) {
            listener.onSourceFileNames(cursor);
        }
    }

    private void parseModule(SMInputCursor cursor,String elementName) throws XMLStreamException {
        if ("Module".equals(elementName)) {
                SMInputCursor childCursor = cursor.childElementCursor();
                scanModuleChildren(childCursor);
        }
    }
    
    private void scanModuleChildren(SMInputCursor cursor) throws XMLStreamException {
        boolean doScanLines=false;    
        while(cursor.getNext() != null) {
                String name=cursor.getLocalName();
                
                if("ModuleName".equals(name)) {
                    doScanLines = listener.onModuleName(cursor);
                }
                if("NamespaceTable".equals(name) && doScanLines) {
                    scanLines(cursor);
                }
            }        
    }

    private void scanLines(SMInputCursor startElementCursor) throws XMLStreamException {
        SMInputCursor linesCursor = startElementCursor
                .descendantElementCursor("Lines");
        while (linesCursor.getNext() != null) {
            listener.onLine(linesCursor);
        }
    }

}
