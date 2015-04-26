package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ObserverTest {

	protected Document doc;
	protected Element rootElement;
	public ObserverTest() {
		super();
	}

	protected String docToString()
			throws TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException {
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				StringWriter writer = new StringWriter();
				transformer.transform(new DOMSource(doc), new StreamResult(writer));
				String output = writer.getBuffer().toString();
			
			
				return output;
			}
	
	protected Element createNewDoc() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.newDocument();
		rootElement = doc.createElement("CoverageDSPriv");
		doc.appendChild(rootElement);
		return rootElement;
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

}