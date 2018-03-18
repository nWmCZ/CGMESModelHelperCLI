package eu.sn;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileProcessor {

    private Set<Profile> cgmesProfiles = new HashSet<>();

    public void processFiles(File inputFilesDir) throws FileNotFoundException, XMLStreamException {

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
                    System.out.println("TODO move to 'orig' folder and unzip");
                    //System.exit(1);
                } else {
                    cgmesProfiles.add(xmlProcesor.extractModelMetadata(file));
                }
            }
        } else {
            System.out.println("No files");
            System.exit(0);
        }
    }

    public Set<Profile> getCgmesProfiles() {
        return cgmesProfiles;
    }
}
