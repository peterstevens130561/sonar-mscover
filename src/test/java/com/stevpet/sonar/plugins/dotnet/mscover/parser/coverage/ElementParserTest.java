/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage;

import java.io.File;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMEvent;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.test.TestUtils;

public class ElementParserTest {

    private ArrayList<String> parentElements = new ArrayList<String>() ;

    @Test
    public void SimpleBooksTest() throws XMLStreamException {
        String[] names =  { "Module","NamespaceTable","Class","Method","Lines","SourceFileNames"};
        for(String name :  names) {
            parentElements.add(name);
        }
        StringBuilder list = new StringBuilder();
        File file = TestUtils.getResource("mscoverage.xml");
            SMInputFactory inf = new SMInputFactory(XMLInputFactory.newInstance());
            SMHierarchicCursor rootCursor = inf.rootElementCursor(file);
            rootCursor.advance();
            SMInputCursor childCursor = rootCursor.childElementCursor();
            parseChild(0,"",list, childCursor);
        String result = list.toString();
        Assert.assertNotNull(result);
    }

    private void parseChild(int level,String path,StringBuilder list, SMInputCursor childCursor)
            throws XMLStreamException {
        SMEvent thisEvent=null;
        list.append("level " + level + " " + path + "\n");
        while((thisEvent=childCursor.getNext())!=null) {
               String name = childCursor.getLocalName();
               if(parentElements.contains(name)) {
                   parseChild(level + 1,path + "/" + name,list,childCursor.childElementCursor());
               } else {
                   if(thisEvent.hasText()) {
                       
                   }
               }
        }
    }
}

