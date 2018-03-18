package eu.sn;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

public class XMLProcesor {

    public static final String FULL_MODEL = "FullModel";
    public static final String RDF_ABOUT = "about";
    public static final String RDF_RESOURCE = "resource";

    private XMLInputFactory factory = XMLInputFactory.newInstance();

    public  Profile extractModelMetadata(File file) throws FileNotFoundException, XMLStreamException {
        // source https://www.tutorialspoint.com/java_xml/java_stax_parse_document.htm

        boolean fullModel = false;
        boolean scenarioTime = false;
        boolean version = false;
        boolean dependentOn = false;
        boolean profile = false;

        String attributeValue = "initialAttributeValue";

        XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(file));

        boolean stillContinue = true;

        Profile cgmesProfile = new Profile();

        while (eventReader.hasNext() && stillContinue) {
            XMLEvent event = eventReader.nextEvent();

            switch (event.getEventType()) {

                case XMLStreamConstants.START_ELEMENT:
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();

                    if (qName.equalsIgnoreCase(FULL_MODEL)) {
                        attributeValue = retrieveAttribute(RDF_ABOUT, startElement.getAttributes());
                        fullModel = true;
                    } else if (qName.equalsIgnoreCase("Model.scenarioTime")) {
                        scenarioTime = true;
                    } else if (qName.equalsIgnoreCase("Model.version")) {
                        version = true;
                    } else if (qName.equalsIgnoreCase("Model.DependentOn")) {
                        attributeValue = retrieveAttribute(RDF_RESOURCE, startElement.getAttributes());
                        dependentOn = true;
                    } else if (qName.equalsIgnoreCase("Model.profile")) {
                        profile = true;
                    }

                    break;
                case XMLStreamConstants.CHARACTERS:
                    Characters characters = event.asCharacters();
                    if(fullModel) {
                        System.out.println("fullModel rdf:about: " + attributeValue);
                        fullModel = false;
                        cgmesProfile.setProfileId(attributeValue);
                    }
                    if(scenarioTime) {
                        System.out.println("scenarioTime: " + characters.getData());
                        scenarioTime = false;
                        cgmesProfile.setScenarioTime(characters.getData());
                    }
                    if(version) {
                        System.out.println("version: " + characters.getData());
                        version = false;
                        cgmesProfile.setVersion(characters.getData());
                    }
                    if(dependentOn) {
                        System.out.println("dependentOn: " + attributeValue);
                        dependentOn = false;
                        cgmesProfile.getDependentOn().add(attributeValue);
                    }
                    if(profile) {
                        System.out.println("profile: " + characters.getData());
                        profile = false;
                        cgmesProfile.setProfile(CgmesProfileType.resolveProfile(characters.getData()));
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    EndElement endElement = event.asEndElement();

                    if (endElement.getName().getLocalPart().equalsIgnoreCase("FullModel")) {
                        System.out.println("End Element : FullModel");
                        stillContinue = false;
                    }
                    break;
            }
        }
        return cgmesProfile;
    }

    private static String retrieveAttribute(String attribute, Iterator<Attribute> attributes) {
        String foundAttributeValue = "nothing";
        if (attributes.hasNext()) {
            Attribute foundAttribute = attributes.next();
            System.out.println(foundAttribute);
            if (attribute.equals(foundAttribute.getName().getLocalPart().toString())) {
                foundAttributeValue = foundAttribute.getValue();
                System.out.println(foundAttribute);
            } else {
                System.out.println("First argument is not " + attribute + ". Found: " + foundAttribute + "TODO: should we iterate?");
                System.exit(1);
            }
        }
        return foundAttributeValue;
    }
}
