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

public class ObserverTest {

	protected Document doc;
	protected Element rootElement;
	protected Element methodElement;
	private Element moduleElement;
	private Element namespaceTableElement;
	private Element classElement;
	public ObserverTest() {
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
		Element nameElement = doc.createElement("ModuleName");
		nameElement.setTextContent(name);
		moduleElement.appendChild(nameElement);
	}
		
	protected void createNamespaceTable(String name) {
		namespaceTableElement = createChild(moduleElement,"NamespaceTable");
		Element nameElement = doc.createElement("NamespaceName");
		nameElement.setTextContent(name);
		namespaceTableElement.appendChild(nameElement);
	}
	
	protected void createClass (String name) {
		classElement = createChild(namespaceTableElement,"Class");
		Element nameElement = doc.createElement("ClassName");
		nameElement.setTextContent(name);
		classElement.appendChild(nameElement);
	}
	
	protected void createMethod (String name) {
		methodElement = createChild(classElement,"Method");
		Element nameElement = doc.createElement("MethodFullName");
		nameElement.setTextContent(name);
		methodElement.appendChild(nameElement);
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
		Element sourceFileIdElement = doc.createElement("SourceFileID");
		sourceFileIdElement.setTextContent(id);
		fileNamesElement.appendChild(sourceFileIdElement);
		Element sourceFileNameElement =doc.createElement("SourceFileName");
		sourceFileNameElement.setTextContent(name);
		fileNamesElement.appendChild(sourceFileNameElement);
	}	
	
	protected void addLine(String fileID, String line,String coverage) {
		Element linesElement = doc.createElement("Lines");
		addLineElement(linesElement,"LnStart",line);
		addLineElement(linesElement,"ColStart","9");
		addLineElement(linesElement,"LnEnd","37");
		addLineElement(linesElement,"ColEnd","10");
		addLineElement(linesElement,"Coverage",coverage);
		addLineElement(linesElement,"SourceFileID",fileID);
		addLineElement(linesElement,"FileID","10");
		methodElement.appendChild(linesElement);
	}
	
	private void addLineElement(Element linesElement,String name, String value) {
		Element lineElement = doc.createElement(name);
		lineElement.setTextContent(value);
		linesElement.appendChild(lineElement);
	}

}