package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;



public class ModuleXmlDocWriter {

	public StringBuilder write(Writer write) throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder=docBuilderFactory.newDocumentBuilder();
		Document document=docBuilder.newDocument();
		Element mainRootElement=document.createElement("CoverageSession");
		document.appendChild(mainRootElement);
		Element modulesElement = document.createElement("Modules");
		mainRootElement.appendChild(modulesElement);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(mainRootElement);
		transformer.transform(source, result);

		String xmlString = result.getWriter().toString();
		StringBuilder sb = new StringBuilder();
		sb.append(xmlString);
		return sb;
	}
}
