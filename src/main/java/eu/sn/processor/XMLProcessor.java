package eu.sn.processor;

import eu.sn.model.CgmesProfileType;
import eu.sn.model.Profile;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

public class XMLProcessor {

    static Logger log = LoggerFactory.getLogger(XMLProcessor.class);

    public static final String FULL_MODEL = "FullModel";
    public static final String MD_PREFIX = "md";
    public static final String MD_URI = "http://iec.ch/TC57/61970-552/ModelDescription/1#";
    public static final String RDF_ABOUT = "about";
    public static final String RDF_PREFIX = "rdf";
    public static final String RDF_NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    public static final Namespace MD = Namespace.getNamespace(MD_PREFIX, MD_URI);
    public static final Namespace RDF = Namespace.getNamespace(RDF_PREFIX, RDF_NAMESPACE);

    public static final String RDF_RESOURCE = "resource";
    public static final String MODEL_CREATED = "Model.created";
    public static final String MODEL_SCENARIO_TIME = "Model.scenarioTime";
    public static final String MODEL_VERSION = "Model.version";
    public static final String MODEL_DEPENDENT_ON = "Model.DependentOn";
    public static final String MODEL_PROFILE = "Model.profile";

    private XMLInputFactory factory = XMLInputFactory.newInstance();

    public void updateModelMetadata(File file, String modelId, String dateTime, String version, Set<String> dependentOn) throws IOException, JDOMException {
        // source https://www.tutorialspoint.com/java_xml/java_stax_modify_document.htm
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(file);
        Element rootElement = document.getRootElement();
        Element fullModel = rootElement.getChild(FULL_MODEL, MD);

        fullModel.removeAttribute(RDF_ABOUT, RDF);
        fullModel.setAttribute(new Attribute(RDF_ABOUT, modelId, RDF));

        fullModel.getChild(MODEL_CREATED, MD).setText(LocalDateTime.now().toString());
        fullModel.getChild(MODEL_SCENARIO_TIME, MD).setText(dateTime);
        fullModel.getChild(MODEL_VERSION, MD).setText(version);

        fullModel.getChildren(MODEL_DEPENDENT_ON, MD).clear();

        for (String dependent: dependentOn) {
            fullModel.addContent(new Element(MODEL_DEPENDENT_ON, MD_PREFIX, MD_URI).setText(dependent));
        }

        writeToFile(document, file);
    }

    private void writeToFile(Document document, File file) throws IOException {
        log.info("Writing file {} ", file);
        XMLOutputter xmlOutput = new XMLOutputter();

        FileOutputStream fos = new FileOutputStream(file);
        xmlOutput.setFormat(Format.getPrettyFormat());
        xmlOutput.output(document, fos);
        log.info("File written {} ", file);
    }

    public Profile extractModelMetadata(File file) throws FileNotFoundException, XMLStreamException {
        // source https://www.tutorialspoint.com/java_xml/java_stax_parse_document.htm

        boolean fullModel = false;
        boolean scenarioTime = false;
        boolean version = false;
        boolean dependentOn = false;
        boolean profile = false;

        String attributeValue = "initialAttributeValue";

        XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(file));

        Profile cgmesProfile = new Profile();

        boolean stillContinue = true;
        while (eventReader.hasNext() && stillContinue) {
            XMLEvent event = eventReader.nextEvent();

            switch (event.getEventType()) {

                case XMLStreamConstants.START_ELEMENT:
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();

                    if (qName.equalsIgnoreCase(FULL_MODEL)) {
                        attributeValue = retrieveAttribute(RDF_ABOUT, startElement.getAttributes());
                        fullModel = true;
                    } else if (qName.equalsIgnoreCase(MODEL_SCENARIO_TIME)) {
                        scenarioTime = true;
                    } else if (qName.equalsIgnoreCase(MODEL_VERSION)) {
                        version = true;
                    } else if (qName.equalsIgnoreCase(MODEL_DEPENDENT_ON)) {
                        attributeValue = retrieveAttribute(RDF_RESOURCE, startElement.getAttributes());
                        dependentOn = true;
                    } else if (qName.equalsIgnoreCase(MODEL_PROFILE)) {
                        profile = true;
                    }

                    break;
                case XMLStreamConstants.CHARACTERS:
                    Characters characters = event.asCharacters();
                    if(fullModel) {
                        log.trace("fullModel rdf:about: " + attributeValue);
                        fullModel = false;
                        cgmesProfile.setProfileId(attributeValue);
                    }
                    if(scenarioTime) {
                        log.trace("scenarioTime: " + characters.getData());
                        scenarioTime = false;
                        cgmesProfile.setScenarioTime(characters.getData());
                    }
                    if(version) {
                        log.trace("version: " + characters.getData());
                        version = false;
                        cgmesProfile.setVersion(characters.getData());
                    }
                    if(dependentOn) {
                        log.trace("dependentOn: " + attributeValue);
                        dependentOn = false;
                        cgmesProfile.getDependentOn().add(attributeValue);
                    }
                    if(profile) {
                        log.trace("profile: " + characters.getData());
                        profile = false;
                        cgmesProfile.setProfile(CgmesProfileType.resolveProfile(characters.getData()));
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    EndElement endElement = event.asEndElement();

                    if (endElement.getName().getLocalPart().equalsIgnoreCase(FULL_MODEL)) {
                        log.trace("End Element : FullModel");
                        stillContinue = false;
                    }
                    break;
            }
        }
        return cgmesProfile;
    }

    private static String retrieveAttribute(String attribute, Iterator<javax.xml.stream.events.Attribute> attributes) {
        String foundAttributeValue = "nothing";
        if (attributes.hasNext()) {
            javax.xml.stream.events.Attribute foundAttribute = attributes.next();
            log.trace(foundAttribute.getName().getLocalPart());
            if (attribute.equals(foundAttribute.getName().getLocalPart())) {
                foundAttributeValue = foundAttribute.getValue();
                log.trace(foundAttributeValue);
            } else {
                log.info("First argument is not " + attribute + ". Found: " + foundAttribute.getName().getLocalPart());
                System.exit(1);
            }
        }
        return foundAttributeValue;
    }
}
