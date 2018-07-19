package eu.sn.processor;

import org.jdom2.JDOMException;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class XMLProcessorTest {

    XMLProcessor xmlProcessor = new XMLProcessor();

    @Test
    void updateModelMetadataTest() throws JDOMException, XMLStreamException, IOException {
        File f = new File("/tmp/test.xml");

        xmlProcessor.updateModelMetadata(f, "ModelID","DateTime", "001", new HashSet<String>() {{ add("DEP_1"); add("DEP_2"); }});
    }
}
