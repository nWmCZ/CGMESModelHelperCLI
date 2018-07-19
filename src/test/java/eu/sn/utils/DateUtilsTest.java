package eu.sn.utils;

import org.junit.jupiter.api.Test;

import static eu.sn.utils.DateUtils.getScenarioTime;
import static eu.sn.utils.DateUtils.getScenarioTimeForBD;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateUtilsTest {
    @Test
    public void getScenarioTimeTest() {
        String result = getScenarioTime("20140601T1030");
        assertEquals("2014-06-01T10:30:00Z", result);
    }

    @Test
    public void getScenarioTimeTestUTC() {
        String result = getScenarioTime("20140601T1030Z");
        assertEquals("2014-06-01T10:30:00Z", result);
    }

    @Test
    public void getScenarioTimeForBDTests() {
        String result = getScenarioTimeForBD("20140601T1030");
        assertEquals("2014-06-01T00:00:00Z", result);
    }

    @Test
    public void getScenarioTimeForBDTestsUTC() {
        String result = getScenarioTimeForBD("20140601T1030Z");
        assertEquals("2014-06-01T00:00:00Z", result);
    }
}
