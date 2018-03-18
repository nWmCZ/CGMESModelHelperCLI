package eu.sn;

import org.apache.commons.cli.CommandLine;

import java.io.File;

import static eu.sn.Main.*;
import static java.lang.String.format;

public class ArgumentProcessor {

    private File profilesDirectory = null;
    private boolean verify = false;
    private String dateTime = null;
    private String version = null;
    private boolean ids = false;

    public ArgumentProcessor(CommandLine commandLine) {
        if (commandLine.hasOption(PROFILES_PATH)) {
            File file = new File(commandLine.getOptionValue(PROFILES_PATH));
            if (!file.isDirectory()) {
                System.out.println(format("Not a directory '%s'", PROFILES_PATH));
                System.exit(1);
            } else {
                profilesDirectory = file;
                System.out.println("Setting profiles path to " + file.getAbsolutePath());
            }
        }
        if (commandLine.hasOption(VERIFY)) {
            System.out.println("Profiles dependencies will be verified");
            verify = true;
        }
        if (commandLine.hasOption(DATE_TIME)) {
            String parsedDateTime = commandLine.getOptionValue(DATE_TIME);
            System.out.println("Setting date and time to " + parsedDateTime);
            dateTime = parsedDateTime;
        }
        if (commandLine.hasOption(VERSION)) {
            String parsedVersion = commandLine.getOptionValue(VERSION);
            System.out.println("Setting version to  " + parsedVersion);
            version = parsedVersion;
        }
        if (commandLine.hasOption(IDS)) {

            System.out.println("New Ids will be set");
            ids = true;
        }
    }

    public File getProfilesDirectory() {
        return profilesDirectory;
    }

    public boolean isVerify() {
        return verify;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getVersion() {
        return version;
    }

    public boolean isIds() {
        return ids;
    }

    public void setIds(boolean ids) {
        this.ids = ids;
    }
}
