package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import java.util.ArrayList;

import javax.xml.stream.XMLStreamException;

import org.codehaus.staxmate.in.SMEvent;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.sensor.BaseCoverageSensor;

public class ParserSubject implements Subject {
 
    static final Logger LOG = LoggerFactory
            .getLogger(ParserSubject.class);
    private ArrayList<String> parentElements = new ArrayList<String>() ;
    private ArrayList<ParserObserver> observers = new ArrayList<ParserObserver>();
    
    public ParserSubject() throws XMLStreamException {
        String[] names =  { "Module","NamespaceTable","Class","Method","Lines","SourceFileNames"};
        for(String name :  names) {
            parentElements.add(name);
        }
    }
    
    public void parse(SMInputCursor rootCursor) throws XMLStreamException {
            SMInputCursor childCursor = rootCursor.childElementCursor();
            parseChild("",childCursor);
    }

    public void registerObserver(ParserObserver observer) {
        observers.add(observer);
    }

    private void parseChild(String path, SMInputCursor childCursor)
            throws XMLStreamException {
        while ((childCursor.getNext()) != null) {
            String name = childCursor.getLocalName();
            if (name.equals("schema")) {
                continue;
            }

            processElement(path, childCursor, name);
        }
    }

    private void processElement(String path, SMInputCursor childCursor,
            String name) throws XMLStreamException {
        String elementPath = createElementPath(path, name);
        if (parentElements.contains(name)) {
            parseChild(elementPath, childCursor.childElementCursor());
        } else {
            String text = childCursor.getElemStringValue();
            invokeObservers(elementPath, name, text);
        }
    }

    private String createElementPath(String path, String name) {
        String elementPath;
        if (path.equals("")) {
            elementPath = name;
        } else {
            elementPath = path + "/" + name;
        }
        return elementPath;
    }

    private void invokeObservers(String path, String name, String text) {
        for (ParserObserver observer : observers) {
            if (observer.isMatch(path)) {
                observer.observe(name, text);
            }
        }
    }
        



    
}
