package eu.sn;

import org.apache.commons.cli.*;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class Main {
    // Can't parse "-"
    public static final String PROFILES_PATH = "path";
    public static final String VERIFY = "verify";
    public static final String DATE_TIME = "datetime";
    public static final String VERSION = "version";
    public static final String IDS = "ids";

    public static void main(String[] args) throws ParseException, FileNotFoundException, XMLStreamException {

        Options options = new Options();

        options.addOption(PROFILES_PATH, true, "Profiles path to check. Usage: java -jar app.jar -" + PROFILES_PATH + " /tmp/");
        options.addOption(VERIFY, false, "Verify correct dependencies between profiles. Usage: java -jar app.jar -" + VERIFY);
        options.addOption(DATE_TIME, true, "New date time for profiles. Usage: java -jar app.jar -" + DATE_TIME + "20180303T0330");
        options.addOption(VERSION, true, "New version for profiles. Usage: java -jar app.jar -" + VERSION + "2");
        options.addOption(IDS, false, "New Ids for profiles. Usage: java -jar app.jar -" + IDS);

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, args);
        System.out.println(Arrays.toString(cmd.getArgs()));

        if(cmd.getOptions().length == 0) {
            System.out.println("No arguments specified, please use these arguments: ");
            for (Option option: options.getOptions()) {
                System.out.println("ARG: " + option.getOpt() + " DESCRIPTION: " + option.getDescription());
            }
            System.exit(1);
        }
        else {
            new ArgumentProcessor(cmd);
        }
    }
}
