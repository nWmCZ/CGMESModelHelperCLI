package eu.sn.processor;

import eu.sn.utils.ParameterValidator;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static eu.sn.Main.*;
import static java.lang.String.format;

public class ArgumentProcessor {

    static Logger log = LoggerFactory.getLogger(ArgumentProcessor.class);

    private File profilesDirectory = null;
    private boolean verify = false;
    private String dateTime = null;
    private String version = null;
    private boolean ids = false;
    private String tso = null;
    private String resolution = null;
    private String prefix = null;
    private String postfix = null;

    public ArgumentProcessor(CommandLine commandLine) {
        if (commandLine.hasOption(PROFILES_PATH)) {
            File file = new File(commandLine.getOptionValue(PROFILES_PATH));
            if (!file.isDirectory()) {
                log.info(format("Not a directory '%s'", PROFILES_PATH));
                System.exit(1);
            } else {
                profilesDirectory = file;
                log.info("Setting profiles path to " + file.getAbsolutePath());
            }
        }
        if (commandLine.hasOption(VERIFY)) {
            log.info("Profiles dependencies will be verified");
            verify = true;
            return;
        }
        if (commandLine.hasOption(DATE_TIME)) {
            String parsedDateTime = commandLine.getOptionValue(DATE_TIME);
            ParameterValidator.validateDateTime(parsedDateTime);
            log.info("Date and time will be set to " + parsedDateTime);
            dateTime = parsedDateTime;
        }
        if (commandLine.hasOption(VERSION)) {
            String parsedVersion = commandLine.getOptionValue(VERSION);
            ParameterValidator.validateVersion(parsedVersion);
            log.info("Version will be set to " + parsedVersion);
            version = parsedVersion;
        }
        if (commandLine.hasOption(IDS)) {

            log.info("New Ids will be set");
            ids = true;
        }
        if (commandLine.hasOption(TSO)) {
            String parsedTso = commandLine.getOptionValue(TSO);
            log.info("TSO will be set to " + parsedTso);
            tso = parsedTso;
        }
        if (commandLine.hasOption(RESOLUTION)) {
            String parsedResolution = commandLine.getOptionValue(RESOLUTION);
            log.info("Resolution will be set to " + parsedResolution);
            resolution = parsedResolution;
        }
        if (commandLine.hasOption(PREFIX)) {
            String parsedPrefix = commandLine.getOptionValue(PREFIX);
            log.info("Prefix will be set to " + parsedPrefix);
            prefix = parsedPrefix;
        }
        if (commandLine.hasOption(POSTFIX)) {
            String parsedPostfix = commandLine.getOptionValue(POSTFIX);
            log.info("Postfix will be set to " + parsedPostfix);
            postfix = parsedPostfix;
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

    public String getTso() {
        return tso;
    }

    public String getResolution() {
        return resolution;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getPostfix() {
        return postfix;
    }
}
