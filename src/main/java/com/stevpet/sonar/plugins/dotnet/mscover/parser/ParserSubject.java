package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import java.io.File;
import java.lang.reflect.Method;
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
                    processStartElement(path, childCursor);
                }
            }

    private void processStartElement(String path, SMInputCursor childCursor)
            throws XMLStreamException {
        String name = childCursor.getLocalName();
        if ("schema".equals(name)) {
            return;
        }
        String elementPath = createElementPath(path, name);
        processAttributes(elementPath,name,childCursor);
        processElement(elementPath, name, childCursor);
    }

    private void processElement(String elementPath, String name, SMInputCursor childCursor)
            throws XMLStreamException {

                if (parentElements.contains(name)) {
                    parseChild(elementPath, childCursor.childElementCursor());
                } else {
                    
                    String text = getTrimmedElementStringValue(childCursor);
                    invokeElementObservers(elementPath, name, text);
                }
            }

    private void invokeElementObservers(String path, String name, String text) {
        for (ParserObserver observer : observers) {
            if (observer.isMatch(path)) {
                observer.observeElement(name, text);
                invokeAnnotatedElementMethods(name,text,observer);
            }
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
                invokeAnnotatedMethods(elementName, attributeValue,attributeName, observer);
            }
        }
    }


    private void invokeAnnotatedElementMethods(String elementName,
            String elementValue,ParserObserver observer) {
        Method[] methods = observer.getClass().getMethods();
        for (Method method : methods) {
            invokeAnnotatedElementMethod(elementName, elementValue,observer, method);
        }
    }

    private void invokeAnnotatedElementMethod(String elementName,
            String elementValue,ParserObserver observer, Method method) {
        ElementMatcher annos = method.getAnnotation(ElementMatcher.class);

        if (annos == null) {
            return;
        }
        if (elementName.equals(annos.elementName()))
            invokeMethod(elementValue, observer, method);
        }

    private void invokeMethod(String elementValue, ParserObserver observer,
            Method method) {
        try {
            method.invoke(observer,elementValue);
        } catch (Exception e) {
            LOG.error("Exception thrown when invoking method",e);
            throw new SonarException(e);
        }
    }
  
    private void invokeAnnotatedMethods(String elementName,
            String attributeValue,String attributeName, ParserObserver observer) {
        Method[] methods = observer.getClass().getMethods();
        for (Method method : methods) {
            invokeAnnotatedMethod(elementName, attributeValue,attributeName, observer, method);
        }
    }

    private void invokeAnnotatedMethod(String elementName,
            String attributeValue,String attributeName, ParserObserver observer, Method method) {
        AttributeMatcher annos = method.getAnnotation(AttributeMatcher.class);

        if (annos == null) {
            if(method.getName().equals("executedMatcher")) {
                LOG.error(method.getName());
            }
            return;
        }
        if (elementName.equals(annos.elementName())
                && attributeName.equals(annos.attributeName())) {
            invokeMethod(attributeValue, observer, method);
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
