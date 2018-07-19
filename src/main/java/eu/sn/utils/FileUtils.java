package eu.sn.utils;

import eu.sn.model.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {

    static Logger log = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {

    }

    public static File unZipFile(File zipFile) {
        File newFile = null;
        byte[] buffer = new byte[1024];

        try {
            //get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                newFile = new File(zipFile.getParent() + File.separator + fileName);

                log.debug("New file will be unzipped: " + newFile.getAbsoluteFile());

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            log.debug("Unzipped file:" + newFile.getAbsoluteFile());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return newFile;
    }

    public static void createNewFilesAccordingToSpecification(Set<Profile> profiles, String tso, String resolution) throws IOException {
        for (Profile profile: profiles) {
            File f = new File(profile.getFile().getParent() + File.separator + NameUtils.getFilenameXML(profile.getProfile(), profile.getScenarioTime(), profile.getVersion(), tso, resolution));
            Files.copy(profile.getFile().toPath(), f.toPath(), StandardCopyOption.REPLACE_EXISTING);
            profile.setFile(f);
        }
    }

}
