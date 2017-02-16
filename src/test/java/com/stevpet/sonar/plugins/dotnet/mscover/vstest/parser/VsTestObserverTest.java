/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import static org.junit.Assert.fail;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class VsTestObserverTest {

	protected Document doc;
	protected Element rootElement;
	protected Element methodElement;
	private Element moduleElement;
	private Element namespaceTableElement;
	private Element classElement;
	public VsTestObserverTest() {
		super();
	}

	protected String docToString()  {
		String output="";
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			output = writer.getBuffer().toString();
		} catch (TransformerException e) {
			fail("impossible exception" + e );
		}
		return output;
	}
	
	protected Element createNewDoc() {
		try { 
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
			rootElement = doc.createElement("CoverageDSPriv");
			doc.appendChild(rootElement);
		} catch ( ParserConfigurationException e ) {
			fail("impossible exception " + e);
		}
		
		return rootElement;
	}
	
	/**
	 * Create the hierarchy from Module to Method
	 */
	protected void createModuleToMethod() {
		createModule("bogus");
		createNamespaceTable("bogus.namespace");
		createClass("BogusClass");
		createMethod("BogusMethod");
	}
	
	protected void createModule(String name) {
		moduleElement=createChild(rootElement,"Module");
		addTextElement(moduleElement,"ModuleName",name);
	}
		
	protected void createNamespaceTable(String name) {
		namespaceTableElement = createChild(moduleElement,"NamespaceTable");
		addTextElement(namespaceTableElement,"NamespaceName",name);
	}
	
	protected void createClass (String name) {
		classElement = createChild(namespaceTableElement,"Class");
		addTextElement(classElement,"ClassName",name);
	}
	
	protected void createMethod (String name) {
		methodElement = createChild(classElement,"Method");
		addTextElement(methodElement,"MethodFullName",name);
	}
	
	private Element createChild(Element parent,String name) {
		Element element=doc.createElement(name);
		parent.appendChild(element);
		return element;
	}
	/**
	 * Create a filename
	 * @param absolute path to file
	 * @param id of file (numerical)
	 */
	protected void createFileName(String name, String id) {
		Element fileNamesElement = doc.createElement("SourceFileNames");
		rootElement.appendChild(fileNamesElement);
		
		addTextElement(fileNamesElement,"SourceFileID",id);
		addTextElement(fileNamesElement,"SourceFileName",name);
	}	
	
	protected void addLine(String fileID, String line,String coverage) {
		Element linesElement = doc.createElement("Lines");
		addTextElement(linesElement,"LnStart",line);
		addTextElement(linesElement,"ColStart","9");
		addTextElement(linesElement,"LnEnd","37");
		addTextElement(linesElement,"ColEnd","10");
		addTextElement(linesElement,"Coverage",coverage);
		addTextElement(linesElement,"SourceFileID",fileID);
		addTextElement(linesElement,"FileID","10");
		methodElement.appendChild(linesElement);
	}
	
	private void addLineElement(Element linesElement,String name, String value) {
		Element lineElement = doc.createElement(name);
		lineElement.setNodeValue(value);
		linesElement.appendChild(lineElement);
	}
	
	private void addTextElement(Element root,String name,String value) {
	    Element textElement=doc.createElement(name);
	    Text text = doc.createTextNode(value);
	    textElement.appendChild(text);
	    root.appendChild(textElement);
	}

}