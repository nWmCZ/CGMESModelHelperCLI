package eu.sn.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class Profile {
    private String profileId;
    private String scenarioTime;
    private String version;
    private Collection<String> dependentOn;
    private CgmesProfileType profile;
    private File file;
    private IdBox idBox;

    public Profile() {
        dependentOn = new ArrayList<>();
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

    public Collection<String> getDependentOn() {
        return dependentOn;
    }

    public CgmesProfileType getProfile() {
        return profile;
    }

    public void setProfile(CgmesProfileType profile) {
        this.profile = profile;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public IdBox getIdBox() {
        return idBox;
    }

    public void setIdBox(IdBox idBox) {
        this.idBox = idBox;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "profileId='" + profileId + '\'' +
                ", scenarioTime='" + scenarioTime + '\'' +
                ", version='" + version + '\'' +
                ", dependentOn=" + dependentOn +
                ", profile=" + profile +
                ", file=" + file +
                ", idBox=" + idBox +
                '}';
    }
}
