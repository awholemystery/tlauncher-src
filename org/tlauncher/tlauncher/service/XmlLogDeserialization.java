package org.tlauncher.tlauncher.service;

import ch.qos.logback.core.CoreConstants;
import com.google.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.tlauncher.util.U;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/service/XmlLogDeserialization.class */
public class XmlLogDeserialization {
    private static final Logger log = Logger.getLogger(XmlLogDeserialization.class);
    private DocumentBuilder builder;
    private StringBuilder log4jMessage = new StringBuilder();
    private final String logFormat = "[%s] [%s/%s]: %s";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:SS");
    private final String LOG_PATTERN_REPLACE = "\u001b\\[[;\\d]*[ -/]*[@-~]";
    private boolean xmlLog;

    @Inject
    public void init() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        this.builder = factory.newDocumentBuilder();
    }

    public void addToLog(String message) {
        String trimmedMessage = message.trim();
        if (trimmedMessage.startsWith("<log4j:Event")) {
            this.xmlLog = true;
        }
        if (this.xmlLog) {
            this.log4jMessage.append(message).append(System.lineSeparator());
            if (trimmedMessage.startsWith("<") && trimmedMessage.startsWith("</log4j:Event")) {
                try {
                    Document doc = this.builder.parse(new ByteArrayInputStream(this.log4jMessage.toString().getBytes()));
                    Node node1 = doc.getFirstChild();
                    NamedNodeMap attributes = node1.getAttributes();
                    message = String.format("[%s] [%s/%s]: %s", Instant.ofEpochMilli(Long.valueOf(node1.getAttributes().item(3).getFirstChild().getTextContent()).longValue()).atZone(ZoneId.systemDefault()).toLocalTime().format(this.formatter), attributes.item(2).getFirstChild().getTextContent(), attributes.item(0).getFirstChild().getTextContent(), node1.getChildNodes().item(1).getFirstChild().getTextContent());
                    if (node1.getChildNodes().getLength() == 5) {
                        message = message + System.lineSeparator() + node1.getChildNodes().item(3).getTextContent();
                    }
                } catch (IOException | SAXException e) {
                    log.warn("error with parsing log ", e);
                }
                U.plog(message.replaceAll("\u001b\\[[;\\d]*[ -/]*[@-~]", CoreConstants.EMPTY_STRING));
                this.log4jMessage.setLength(0);
                this.xmlLog = false;
                return;
            }
            return;
        }
        U.plog(message.replaceAll("\u001b\\[[;\\d]*[ -/]*[@-~]", CoreConstants.EMPTY_STRING));
    }
}
