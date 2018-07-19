package eu.sn;

import eu.sn.model.IdBox;
import eu.sn.model.Profile;
import eu.sn.processor.ArgumentProcessor;
import eu.sn.processor.DependencyResolver;
import eu.sn.processor.FileProcessor;
import eu.sn.processor.UpdateProcessor;
import eu.sn.utils.FileUtils;
import org.apache.commons.cli.*;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    static Logger log = LoggerFactory.getLogger(Main.class);

    public static final String PROFILES_PATH = "p";
    public static final String VERIFY = "verify";
    public static final String DATE_TIME = "d";
    public static final String VERSION = "v";
    public static final String IDS = "i";
    public static final String TSO = "t";
    public static final String RESOLUTION = "r";
    public static final String PREFIX = "prefix";
    public static final String POSTFIX = "postfix";

    public static void main(String[] args) throws ParseException, IOException, XMLStreamException, JDOMException {

        Options options = new Options();

        options.addOption(PROFILES_PATH, true, "Profiles path to check. USAGE: java -jar app.jar -" + PROFILES_PATH + " /tmp/");
        options.addOption(VERIFY, false, "Verify correct dependencies between profiles. USAGE: java -jar app.jar -" + VERIFY);
        options.addOption(DATE_TIME, true, "New date time for profiles. USAGE: java -jar app.jar -" + DATE_TIME + " 20180303T0330");
        options.addOption(VERSION, true, "New version for profiles. USAGE: java -jar app.jar -" + VERSION + " 2");
        options.addOption(IDS, false, "New Ids for profiles. USAGE: java -jar app.jar -" + IDS);
        options.addOption(TSO, true, "TSO name. USAGE: java -jar app.jar -" + TSO + " ELIA");
        options.addOption(RESOLUTION, true, "Resolution of profiles. USAGE: java -jar app.jar -" + RESOLUTION + " 1D");
        options.addOption(PREFIX, true, "Prefix of profile ID. USAGE: java -jar app.jar -" + PREFIX + " MY_PREFIX_");
        options.addOption(POSTFIX, true, "Postfix of profile ID. USAGE: java -jar app.jar -" + POSTFIX + " _MY_POSTFIX");

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, args);
        log.info(Arrays.toString(cmd.getArgs()));

        log.info("##########################################################################################");
        log.info("# Application extracts all ZIP files in the given directory and then load all XML files. #");
        log.info("##########################################################################################");
        if (cmd.getOptions().length == 0) {
            log.info("No arguments specified, please use these arguments: ");
            for (Option option : options.getOptions()) {
                log.info("ARG: " + option.getOpt() + " DESCRIPTION: " + option.getDescription());
            }
            System.exit(1);
        } else {
            // TODO create mandatory validator for parameters. TSO and Resolution are mandatory, date and version are optional
            ArgumentProcessor argumentProcessor = new ArgumentProcessor(cmd);

            FileProcessor fileProcessor = new FileProcessor();
            fileProcessor.processFiles(argumentProcessor.getProfilesDirectory());

            if (DependencyResolver.isAllProfilesPresent(fileProcessor.getCgmesProfiles())) {
                if (argumentProcessor.isVerify()) {
                    DependencyResolver.printProfileDependenciesStatus(fileProcessor.getCgmesProfiles());
                    System.exit(0);
                }

                for (Profile profile: fileProcessor.getCgmesProfiles()) {
                    if (argumentProcessor.getDateTime() != null) {
                        profile.setScenarioTime(argumentProcessor.getDateTime());
                    }
                    if (argumentProcessor.getVersion() != null) {
                        profile.setVersion(argumentProcessor.getVersion());
                    }
                    IdBox idBox = new IdBox();
                    if (argumentProcessor.getPrefix() != null) {
                        idBox.setPrefix(argumentProcessor.getPrefix());
                    }
                    if (argumentProcessor.getPostfix() != null) {
                        idBox.setPostfix(argumentProcessor.getPostfix());
                    }
                    profile.setIdBox(idBox);
                }
                FileUtils.createNewFilesAccordingToSpecification(fileProcessor.getCgmesProfiles(), argumentProcessor.getTso(), argumentProcessor.getResolution());


                UpdateProcessor updateProcessor = new UpdateProcessor();
                updateProcessor.updateFiles(fileProcessor.getCgmesProfiles(), argumentProcessor.getDateTime(), argumentProcessor.getVersion(), argumentProcessor.isIds());

            } else {
                log.info("Not all required profiles are present.");
                System.exit(1);
            }


        }
    }
}
