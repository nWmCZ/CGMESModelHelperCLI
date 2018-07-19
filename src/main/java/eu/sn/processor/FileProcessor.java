package eu.sn.processor;

import eu.sn.model.Profile;
import eu.sn.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileProcessor {

    static Logger log = LoggerFactory.getLogger(FileProcessor.class);

    private Set<Profile> cgmesProfiles = new HashSet<>();

    public void processFiles(File inputFilesDir) throws IOException, XMLStreamException {

        XMLProcessor xmlProcessor = new XMLProcessor();

        File inputDir;
        if (inputFilesDir != null) {
            inputDir = inputFilesDir;
        } else {
            inputDir = new File(".");
        }
        File[] files = inputDir.listFiles();

        if (files.length > 0) {
            log.info(Arrays.toString(files));
            // Unzip all zipped files
            for (File file : files) {
                if (file.getName().toLowerCase().endsWith(".zip")) {
                    FileUtils.unZipFile(file);
                }
            }

            File[] newFiles = inputDir.listFiles();
            // Process all XML files
            for (File newFile : newFiles) {
                if (newFile.getName().toLowerCase().endsWith(".xml")) {
                    Profile p = xmlProcessor.extractModelMetadata(newFile);
                    p.setFile(newFile);
                    cgmesProfiles.add(p);
                } else {
                    log.info("Not XML file. Ignoring: " + newFile.getName());
                }
            }

        } else {
            log.info("No files to process.");
            System.exit(0);
        }
    }

    public Set<Profile> getCgmesProfiles() {
        return cgmesProfiles;
    }
}
