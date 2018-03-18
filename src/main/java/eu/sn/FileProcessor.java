package eu.sn;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FileProcessor {

    private Map<CgmesProfileType, File> profilesMap = new HashMap();
    private Set<Profile> cgmesProfiles = new HashSet<>();

    public void processFiles(File inputFilesDir, String dateTime, String version, boolean ids) throws FileNotFoundException, XMLStreamException {

        XMLProcesor xmlProcesor = new XMLProcesor();

        File inputDir;
        if (inputFilesDir != null) {
            inputDir = inputFilesDir;
        } else {
            inputDir = new File(".");
        }
        File[] files = inputDir.listFiles();

        if (files.length > 0) {

            for (File file : files) {
                System.out.println(Arrays.toString(files));
                if (!file.getName().toLowerCase().endsWith(".xml")) {
                    System.out.println("Ignoring: " + file.getName());
                    //System.exit(1);
                } else {
                    xmlProcesor.extractModelMetadata(file);
                }

            }
        } else {
            System.out.println("No files");
            System.exit(0);
        }
    }
}
