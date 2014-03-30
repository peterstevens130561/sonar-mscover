/*
 * Sonar .NET Plugin :: MsCover
 * Copyright (C) 2014 Peter Stevens
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package com.stevpet.sonar.plugins.dotnet.mscover.parser;


import org.codehaus.staxmate.in.SMInputCursor;

import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.listener.BaseParserListener;
import com.stevpet.sonar.plugins.dotnet.mscover.listener.ParserListener;

import javax.xml.stream.XMLStreamException;

import static org.sonar.plugins.csharp.gallio.helper.StaxHelper.findElementName;

/**
 * Parser implementation for coverage.xml files, generated by Visual Studio, or any other means
 * See http://msdn.microsoft.com/en-us/library/microsoft.visualstudio.coverage.analysis.coveragedspriv.aspx
 * for more information
 *
 */
public class CoverageParser implements Parser {


    private static String SIGNATURE_ELEMENT = "CoverageDSPriv";
    

    private ParserListener listener =  new BaseParserListener() ;

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.CoverageParser#isCompatible(org.codehaus.staxmate.in.SMInputCursor)
     */
    public boolean isCompatible(SMInputCursor rootCursor) {
        String elementName = findElementName(rootCursor);
        return SIGNATURE_ELEMENT.equals(elementName);
    }

    
    public void setListener(ParserListener listener) {
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
                    String value=cursor.getElemStringValue();
                    doScanLines = listener.onModuleName(value);
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

