package eu.sn.utils;

import eu.sn.model.CgmesProfileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static eu.sn.utils.DateUtils.getScenarioTimeForBDFilename;

public class NameUtils {

    static Logger log = LoggerFactory.getLogger(NameUtils.class);

    private NameUtils() {

    }

    // 20140601T0000Z_ENTSO-E_EQ_BD_001.xml and 20140601T0000Z_ENTSO-E_TP_BD_001.xml
    // 20140601T1030Z_01_ELIA_SSH_001.xml TP SV
    // 20140601T1030Z_ELIA_EQ_001.xml
    public static String getFilenameXML(CgmesProfileType profileType, String dateTime, String version, String tso, String resolution) {
        switch (profileType) {
            case EQUIPMENT:
                return  dateTime + "_" + tso + "_EQ_" + fillToThree(version) + ".xml";
            case TOPOLOGY:
                return  dateTime + "_" + resolution + "_" + tso + "_TP_" + fillToThree(version) + ".xml";
            case STATE_VARIABLES:
                return  dateTime + "_" + resolution + "_" + tso + "_SV_" + fillToThree(version) + ".xml";
            case STEADY_STATE_HYPOTHESIS:
                return  dateTime + "_" + resolution + "_" + tso + "_SSH_" + fillToThree(version) + ".xml";
            case EQUIPMENT_BOUNDARY:
                return  getScenarioTimeForBDFilename(dateTime) + "Z_ENTSO-E_EQ_BD_" + fillToThree(version) + ".xml";
            case TOPOLOGY_BOUNDARY:
                return  getScenarioTimeForBDFilename(dateTime) + "Z_ENTSO-E_TP_BD_" + fillToThree(version) + ".xml";
            default:
                return "undefined";
        }
    }

    public static String fillToThree(String string) {
        return ("000" + string).substring(string.length());
    }

    public static String fillToTwo(String string) {
        return ("00" + string).substring(string.length());
    }
}
