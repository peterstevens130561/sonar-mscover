package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;

public abstract class ParserSubject implements Subject {

    private static final Logger LOG = LoggerFactory
            .getLogger(ParserSubject.class);
    List<ParserObserver> observers = new ArrayList<ParserObserver>();

    List<String> parentElements = new ArrayList<String>() ;
    
    public ParserSubject() {
        String[] names =  getHierarchy();
        for(String name :  names) {
            parentElements.add(name);
        }
    }
    
    public abstract String[] getHierarchy() ;
    
    public void parseFile(File file) { 
        SMInputCursor cursor;
        try {
            cursor = getCursor(file);
            parse(cursor);
        } catch (FactoryConfigurationError e) {
            LOG.error("FactoryConfigurationError",e);
            throw new SonarException(e);
        } catch (XMLStreamException e) {
            LOG.error("XMLStreamException",e);
            throw new SonarException(e);
        }

    }
    
    private void parse(SMInputCursor rootCursor) throws XMLStreamException {
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
                    if ("schema".equals(name)) {
                        continue;
                    }
            
                    processElement(path, childCursor, name);
                }
            }

    private void processElement(String path, SMInputCursor childCursor, String name)
            throws XMLStreamException {
                String elementPath = createElementPath(path, name);
                if (parentElements.contains(name)) {
                    parseChild(elementPath, childCursor.childElementCursor());
                } else {
                    
                    String text = getTrimmedElementStringValue(childCursor);
                    invokeElementObservers(elementPath, name, text);
                }
            }

    private void processAttributes(String path, String name, SMInputCursor elementCursor) throws XMLStreamException {
        int attributeCount = elementCursor.getAttrCount();
        for(int index=0;index<attributeCount;index++) {
            String attributeValue=elementCursor.getAttrValue(index);
            String attributeName = elementCursor.getAttrLocalName(index);
            invokeAttributeObservers(name, path, attributeValue,attributeName);
            
        }
    }
    private void invokeAttributeObservers(String elementName, String path,
            String attributeValue, String attributeName) {
        for (ParserObserver observer : observers) {
            if (observer.isMatch(path)) {
                observer.observeAttribute(elementName,path,attributeValue,attributeName);
            }
        }
    }

    private String getTrimmedElementStringValue(SMInputCursor childCursor)
            throws XMLStreamException {
        String text = childCursor.getElemStringValue();
        if(StringUtils.isNotEmpty(text)) {
            text = text.trim();
        }
        return text;
    }

    private String createElementPath(String path, String name) {
        String elementPath;
        if ("".equals(path)) {
            elementPath = name;
        } else {
            elementPath = path + "/" + name;
        }
        return elementPath;
    }

    private void invokeElementObservers(String path, String name, String text) {
        for (ParserObserver observer : observers) {
            if (observer.isMatch(path)) {
                observer.observeElement(name, text);
            }
        }
    }
    
    /**
     * Gets the cursor for the given file
     * @param file
     * @return
     * @throws FactoryConfigurationError
     * @throws XMLStreamException
     */
    public SMInputCursor getCursor(File file)
            throws FactoryConfigurationError, XMLStreamException {
        SMInputFactory inf = new SMInputFactory(XMLInputFactory.newInstance());
        SMHierarchicCursor cursor= inf.rootElementCursor(file);
        return cursor.advance();
    }
}
