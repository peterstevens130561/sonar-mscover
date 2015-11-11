/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.common.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementEntry;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.ElementObserver.Event;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.annotations.PathMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.exceptions.MsCoverParserException;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.exceptions.ParserSubjectErrorException;

/**
 * Each parser should implement this class
 * 
 *
 */
public abstract class XmlParserSubject implements ParserSubject {

    private static final String FACTORY_CONFIGURATION_ERROR = "FactoryConfigurationError";
    private static final String COULD_NOT_CREATE_CURSOR = "Could not create cursor ";
    private static final Logger LOG = LoggerFactory
            .getLogger(XmlParserSubject.class);
    private List<ParserObserver> observers = new ArrayList<ParserObserver>();

    private List<String> parentElements = new ArrayList<String>();
    private int line;
    private int column;
    private ParserData parserData = new ParserData();
    private ElementObserverInvoker elementObserver = new ElementObserverInvoker();
    public XmlParserSubject() {
        String[] names = getHierarchy();
        for (String name : names) {
            parentElements.add(name);
        }
    }

    public List<ParserObserver> getObservers() {
        return observers;
    }

    public abstract String[] getHierarchy();

    @SuppressWarnings("ucd")
    public void parseString(String string) {
        SMInputCursor cursor;
        try {
            cursor = getCursorFromString(string);
            parse(cursor);
        } catch (FactoryConfigurationError e) {
            LOG.error(FACTORY_CONFIGURATION_ERROR, e);
            throw new SonarException(e);
        } catch (XMLStreamException e) {
            String msg = "XMLStreamException in string";
            LOG.error(msg, e);
            throw new SonarException(msg, e);
        }
    }

    /**
     * Gets the cursor for the given file
     * 
     * @param file
     * @return
     * @throws FactoryConfigurationError
     * @throws XMLStreamException
     */
    private SMInputCursor getCursorFromString(String string) {
        SMInputCursor result = null;
        try {
            SMInputFactory inf = new SMInputFactory(
                    XMLInputFactory.newInstance());
            InputStream inputStream = new ByteArrayInputStream(
                    string.getBytes());
            SMHierarchicCursor cursor = inf.rootElementCursor(inputStream);
            result = cursor.advance();
        } catch (XMLStreamException e) {
            String msg = COULD_NOT_CREATE_CURSOR + e.getMessage();
            LOG.error(msg);
            throw new SonarException(msg, e);
        }
        return result;
    }

    public void parseFile(File file) {
        SMInputCursor cursor=null;
        try {
            cursor = getCursor(file);
            parse(cursor);
        } catch (FactoryConfigurationError e) {
            LOG.error(FACTORY_CONFIGURATION_ERROR, e);
            throw new SonarException(e);
        } catch (XMLStreamException e) {
            String msg = "XMLStreamException in " + file.getAbsolutePath()
                    + " column/line " + column + "/" + line;
            LOG.error(msg, e);
            throw new SonarException(msg, e);
        } finally {
            closeStream(cursor);
        }
        checkOnErrors(file);

    }

    public void closeStream(SMInputCursor cursor) {
        try {
                if(cursor==null) {
                    return;
                }
                XMLStreamReader2 reader = cursor.getStreamReader();
                if(reader==null) {
                    return;
                }
                reader.closeCompletely();
        } catch (XMLStreamException e) {
            throw new SonarException("exception during closing stream",e);
        }
    }

    private void checkOnErrors(File file) {
        for (ParserObserver observer : observers) {
            if (observer.hasError()) {
                throw new ParserSubjectErrorException(file);
            }
        }
    }

    private void parse(SMInputCursor rootCursor) throws XMLStreamException {
        injectVariablesInObservers();
        elementObserver.setObservers(observers);
        SMInputCursor childCursor = rootCursor.childElementCursor();
        parseChild("", childCursor);
    }

    private void injectVariablesInObservers() {
        for (ParserObserver observer : observers) {
            observer.setParserData(parserData);
        }
    }

    public void registerObserver(ParserObserver observer) {
        observers.add(observer);
    }

    private boolean parseChild(String path, SMInputCursor childCursor)
            throws XMLStreamException {
        boolean parsedChild = false;
        parserData.levelDown();
        while ((childCursor.getNext()) != null) {
            if (!parserData.parseLevelAndBelow()) {
                processStartElement(path, childCursor);
                parsedChild = true;
            }
        }
        parserData.levelUp();
        return parsedChild;
    }

    /**
     * executed on exit of an element, can override to change behavior.
     * Default behavior is to execute the exit methods
     * @param path
     */
    protected void onExit(String path) {
        elementObserver.invokeObservers(path,Event.EXIT);
    }

    /**
     * executed on entry of an element, can override to change behavior.
     * Default behavior is to execute the entry methods
     * @param path
     */
    protected void onEntry(String path) {
        elementObserver.invokeObservers(path,Event.ENTRY);
    }
    

    private void processStartElement(String path, SMInputCursor childCursor)
            throws XMLStreamException {
        String name = childCursor.getLocalName();
        if ("schema".equals(name)) {
            return;
        }
        String elementPath = createElementPath(path, name);
        onEntry(elementPath);
        processAttributes(elementPath, name, childCursor);
        processElement(elementPath, name, childCursor);
        onExit(elementPath);
    }

    private void processElement(String elementPath, String name,
            SMInputCursor childCursor) throws XMLStreamException {

        if (parentElements.contains(name)) {
            parseChild(elementPath, childCursor.childElementCursor());
        } else {
            updateLocation(childCursor);
            String text = getTrimmedElementStringValue(childCursor);
            invokeElementObservers(elementPath, name, text);
        }
    }

    private void invokeElementObservers(String path, String name, String text) {
        for (ParserObserver observer : observers) {
            if (observer.isMatch(path)) {
                observer.observeElement(name, text);
                invokeAnnotatedElementMethods(path, name, text, observer);
            }
        }
    }

    private void processAttributes(String path, String name,
            SMInputCursor elementCursor) throws XMLStreamException {
        int attributeCount = elementCursor.getAttrCount();
        for (int index = 0; index < attributeCount; index++) {
            String attributeValue = elementCursor.getAttrValue(index);
            String attributeName = elementCursor.getAttrLocalName(index);
            updateLocation(elementCursor);
            invokeAttributeObservers(name, path, attributeValue, attributeName);
        }
    }

    private void updateLocation(SMInputCursor elementCursor) {
        Location location;
        try {
            location = elementCursor.getCursorLocation();

        } catch (XMLStreamException e) {
            throw new MsCoverParserException(
                    "Exception thrown on getting location", e);
        }
        line = location.getLineNumber();
        column = location.getColumnNumber();

    }

    private void invokeAttributeObservers(String elementName, String path,
            String attributeValue, String attributeName) {
        for (ParserObserver observer : observers) {
            if (observer.isMatch(path)) {
                observer.observeAttribute(elementName, path, attributeValue,
                        attributeName);
                invokeAnnotatedMethods(elementName, attributeValue,
                        attributeName, observer);
            }
        }
    }

    private void invokeAnnotatedElementMethods(String elementPath,
            String elementName, String elementValue, ParserObserver observer) {
        Method[] methods = observer.getClass().getMethods();
        for (Method method : methods) {
            invokeAnnotatedElementMethod(elementName, elementValue, observer,
                    method);
            invokePathMatcherMethod(elementPath, elementValue, observer, method);
        }
    }

    private void invokePathMatcherMethod(String path, String elementValue,
            ParserObserver observer, Method method) {
        PathMatcher annos = method.getAnnotation(PathMatcher.class);
        if (annos == null) {
            return;
        }
        if (path.equals(annos.path())) {
            invokeMethod(observer, method, elementValue);
        }

    }

    private void invokeAnnotatedElementMethod(String elementName,
            String elementValue, ParserObserver observer, Method method) {
        ElementMatcher annos = method.getAnnotation(ElementMatcher.class);

        if (annos == null) {
            return;
        }
        if (elementName.equals(annos.elementName())) {
            invokeMethod(observer, method, elementValue);
        }
    }
    



    private void invokeMethod(ParserObserver observer, Method method,
            String... elementValue ) {
        try {
            Object[] varargs = elementValue;
            method.invoke(observer, varargs);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() != null) {
                String msg = "Exception thrown when invoking method"
                        + observer.getClass().getName() + ":"
                        + method.getName() + lineMsg();
                LOG.error(msg, e.getTargetException());
                throw new SonarException(msg, e);
            }
            String msg = "Invocation Target Exception thrown when invoking method "
                    + observer.getClass().getName()
                    + ":"
                    + method.getName()
                    + lineMsg();
            LOG.error(msg, e);
            throw new SonarException(msg, e);
        } catch (IllegalAccessException e) {
            String msg = "Illegal Access Exception thrown when invoking method "
                    + observer.getClass().getName()
                    + ":"
                    + method.getName()
                    + lineMsg();
            LOG.error(msg, e);
            throw new SonarException(msg, e);
        } catch (IllegalArgumentException e) {
            String msg = "Illegal Argument Exception thrown when invoking method "
                    + observer.getClass().getName()
                    + ":"
                    + method.getName()
                    + lineMsg();
            LOG.error(msg, e);
            throw new SonarException(msg, e);
        }
    }

    private String lineMsg() {
        return " line/column = " + line + "/" + column;
    }

    private void invokeAnnotatedMethods(String elementName,
            String attributeValue, String attributeName, ParserObserver observer) {
        Method[] methods = observer.getClass().getMethods();
        for (Method method : methods) {
            invokeAnnotatedMethod(elementName, attributeValue, attributeName,
                    observer, method);
        }
    }

    private void invokeAnnotatedMethod(String elementName,
            String attributeValue, String attributeName,
            ParserObserver observer, Method method) {
        AttributeMatcher annos = method.getAnnotation(AttributeMatcher.class);

        if (annos == null) {
            return;
        }
        if (elementName.equals(annos.elementName())
                && attributeName.equals(annos.attributeName())) {
            invokeMethod(observer, method, attributeValue);
        }
    }

    private String getTrimmedElementStringValue(SMInputCursor childCursor)
            throws XMLStreamException {
        String text = childCursor.getElemStringValue();
        if (StringUtils.isNotEmpty(text)) {
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
     * 
     * @param file
     * @return
     * @throws FactoryConfigurationError
     * @throws XMLStreamException
     */
    private SMInputCursor getCursor(File file) {
        SMInputCursor result = null;
        try {
            SMInputFactory inf = new SMInputFactory(
                    XMLInputFactory.newInstance());
            SMHierarchicCursor cursor = inf.rootElementCursor(file);
            result = cursor.advance();
        } catch (XMLStreamException e) {
            String msg = COULD_NOT_CREATE_CURSOR + e.getMessage();
            LOG.error(msg);
            throw new SonarException(msg, e);
        }
        return result;
    }

}
