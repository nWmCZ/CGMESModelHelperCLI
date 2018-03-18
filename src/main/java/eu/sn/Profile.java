package eu.sn;

import java.util.HashSet;
import java.util.Set;

class Profile {
    private String profileId;
    private String scenarioTime;
    private String version;
    private Set<String> dependentOn;
    private CgmesProfileType profile;

    public Profile() {
        dependentOn = new HashSet<String>();
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getScenarioTime() {
        return scenarioTime;
    }

    public void setScenarioTime(String scenarioTime) {
        this.scenarioTime = scenarioTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<String> getDependentOn() {
        return dependentOn;
    }

    public CgmesProfileType getProfile() {
        return profile;
    }

    public void setProfile(CgmesProfileType profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "profileId='" + profileId + '\'' +
                ", scenarioTime='" + scenarioTime + '\'' +
                ", version='" + version + '\'' +
                ", dependentOn=" + dependentOn +
                ", profile=" + profile +
                '}';
    }
}
