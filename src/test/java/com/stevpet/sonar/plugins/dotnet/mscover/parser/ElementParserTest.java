package com.stevpet.sonar.plugins.dotnet.mscover.parser;

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
        SMEvent prevEvent=null;
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

