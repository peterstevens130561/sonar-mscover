package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;

import org.sonar.api.utils.SonarException;

/**
 * Parse the OpenCover file, and give each module to the moduleLambda
 * 
 */
class OpenCoverModuleSplitterBase implements ModuleSplitter {

	private ModuleLambda moduleHelper;

	OpenCoverModuleSplitterBase(ModuleLambda moduleHelper) {
		this.moduleHelper = moduleHelper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.stevpet.sonar.plugins.dotnet.mscover.modulesplitter.ModuleSplitter
	 * #splitFile(java.io.File)
	 */
	@Override
	public int splitFile(File file) {
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new SonarException("Could not find" + file.getAbsolutePath());
		}
		try {
			return split(inputStream);
		} catch (XMLStreamException | TransformerException e) {
			throw new SonarException("XML exception", e);
		}
	}

	private int split(InputStream inputStream) throws XMLStreamException,
			TransformerException {

		BufferedReader in = new BufferedReader(new InputStreamReader(
				inputStream));

		XMLInputFactory factory = XMLInputFactory.newInstance();
		TransformerFactory tf = TransformerFactory.newInstance();

		Transformer t = tf.newTransformer();
		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

		XMLStreamReader streamReader = factory.createXMLStreamReader(in);
		int modules = 0;
		while (streamReader.hasNext()) {
			streamReader.next();

			if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT
					&& "Module".equals(streamReader.getLocalName())) {
				StringWriter writer = new StringWriter();
				t.transform(new StAXSource(streamReader), new StreamResult(
						writer));
				StringBuilder sb = writeXml(writer);
				moduleHelper.execute(sb.toString());

			}

		}
		return modules;

	}

	private StringBuilder writeXml(StringWriter writer) {
		StringBuilder sb = new StringBuilder(10240);
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<CoverageSession xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		sb.append("<Modules>");
		sb.append(writer.toString());
		sb.append("</Modules>");
		sb.append("</CoverageSession>");
		return sb;
	}

}
