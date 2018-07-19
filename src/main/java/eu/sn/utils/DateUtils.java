package eu.sn.utils;

public class DateUtils {
    private DateUtils() {

    }

    public static String getScenarioTime(String date) {
        // replace 20140601T1030 to valid scenario type
        StringBuilder stringBuilder = new StringBuilder(date.substring(0,4)); //year
        stringBuilder
                .append("-")
                .append(date.substring(4,6)) //month
                .append("-")
                .append(date.substring(6,8)) //day
                .append("T")
                .append(date.substring(9,11)) //hour
                .append(":")
                .append(date.substring(11,13)) //minute
                .append(":00Z");
        return stringBuilder.toString();
    }

    public static String getScenarioTimeForBD(String date) {
        // replace 20140601T1030 to 2014-06-01T00:00:00Z
        StringBuilder stringBuilder = new StringBuilder(date.substring(0,4)); //year
        stringBuilder
                .append("-")
                .append(date.substring(4,6)) //month
                .append("-")
                .append(date.substring(6,8)) //day
                .append("T")
                .append("00") //hour
                .append(":")
                .append("00") //minute
                .append(":00Z");
        return stringBuilder.toString();
    }

    public static String getScenarioTimeForBDFilename(String date) {
        // replace 20140601T1030 to 20140601T0000
        StringBuilder stringBuilder = new StringBuilder(date.substring(0,9)); //year
        stringBuilder.append("0000");
        return stringBuilder.toString();
    }
}
