package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Parse the OpenCover file, and give each module to the moduleLambda
 * 
 */
public class OpenCoverModuleSplitter implements ModuleSplitter {
    private final Logger LOG = LoggerFactory.getLogger(OpenCoverModuleSplitter.class);
    private CoverageModuleSaver coverageModuleSaver;
    private CoverageHashes coverageHashes;
    private final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
    private final TransformerFactory transformerFactory = TransformerFactory.newInstance();

    /**
     * For general use
     * @param coverageHashes
     */
    public OpenCoverModuleSplitter(CoverageHashes coverageHashes) {
        this(new OpenCoverCoverageModuleSaver(new OpenCoverModuleParser()), coverageHashes);
    }

    /**
     * For unit testing
     * @param moduleHelper
     * @param coverageHashes
     */
    public OpenCoverModuleSplitter(CoverageModuleSaver moduleHelper,CoverageHashes coverageHashes) {
        this.coverageModuleSaver = moduleHelper;
        this.coverageHashes = coverageHashes;
    }

    @Override
    public int splitCoverageFileInFilePerModule(@Nonnull File coverageRootDir, @Nonnull String testProjectName, @Nonnull File testCoverageFile) {

        InputStream inputStream;
        try {
            inputStream = new FileInputStream(testCoverageFile);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Could not find" + testCoverageFile.getAbsolutePath());
        }
        try {
            return split(coverageRootDir, testProjectName, inputStream);
        } catch (XMLStreamException | TransformerException e) {
            throw new IllegalStateException("XML exception", e);
        }
    }
    private int split(File coverageRootDir,String testProjectName,InputStream inputStream) throws XMLStreamException,
            TransformerException {

        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        
        BufferedReader in = new BufferedReader(new InputStreamReader(
                inputStream));
        
        XMLStreamReader reader=xmlInputFactory.createXMLStreamReader(in);
 
        int modules = 0;
        while (reader.hasNext()) {
            reader.next();
            if(isModuleStartElement(reader)) {
            LOG.debug(reader.getLocalName());
                String xml = getModuleIntoNewXmlDoc(transformer, reader);
                ++modules;
                if(!coverageHashes.add(xml)) {
                    coverageModuleSaver.save(coverageRootDir,testProjectName,xml);
                }
            }

        }
        return modules;
    }

    private boolean isModuleStartElement(XMLStreamReader streamReader) {
        return streamReader.getEventType() == XMLStreamReader.START_ELEMENT
                && "Module".equals(streamReader.getLocalName());
    }

    private String getModuleIntoNewXmlDoc(Transformer t, XMLStreamReader streamReader) throws TransformerException {
        StringWriter writer = new StringWriter();
        t.transform(new StAXSource(streamReader), new StreamResult(
                writer));
        StringBuilder sb = writeXml(writer);
        String xml = sb.toString();
        return xml;
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
