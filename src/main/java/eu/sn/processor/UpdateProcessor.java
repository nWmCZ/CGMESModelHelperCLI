package eu.sn.processor;

import eu.sn.model.CgmesProfileType;
import eu.sn.model.Profile;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static eu.sn.processor.DependencyResolver.igmMap;
import static eu.sn.utils.DateUtils.getScenarioTime;
import static eu.sn.utils.DateUtils.getScenarioTimeForBD;
import static java.lang.String.format;

public class UpdateProcessor {

    static Logger log = LoggerFactory.getLogger(UpdateProcessor.class);

    public static String URN_UUID_PREFIX = "urn:uuid:";

    Map<CgmesProfileType, String> newProfileIds = new HashMap<>();

    public void updateFiles(Set<Profile> profiles, String dateTime, String version, boolean newIds) throws JDOMException, IOException {
        XMLProcessor xmlProcessor = new XMLProcessor();

        for (CgmesProfileType cgmesProfileType: igmMap.keySet()) {
            StringBuilder newProfileId = new StringBuilder(URN_UUID_PREFIX);

            Profile p = profiles.stream().filter(profile -> cgmesProfileType.equals(profile.getProfile())).findFirst().get();

            if (p.getIdBox().getPrefix().isPresent()) {
                newProfileId.append(p.getIdBox().getPrefix().get());
            }

            if (newIds) {
                newProfileId.append(UUID.randomUUID().toString());
            } else {
                String originalProfileId = p.getProfileId();
                newProfileId.append(originalProfileId);
            }
            if (p.getIdBox().getPostfix().isPresent()) {
                newProfileId.append(p.getIdBox().getPostfix().get());
            }
            newProfileIds.put(cgmesProfileType, newProfileId.toString());
        }

        for (Profile profile : profiles) {
            switch (profile.getProfile()) {
                case STATE_VARIABLES:
                    xmlProcessor.updateModelMetadata(profile.getFile(), newProfileIds.get(profile.getProfile()), getScenarioTime(dateTime), version, prepareDependentOnSet(profile.getProfile()));
                    break;
                case TOPOLOGY:
                    xmlProcessor.updateModelMetadata(profile.getFile(), newProfileIds.get(profile.getProfile()), getScenarioTime(dateTime), version, prepareDependentOnSet(profile.getProfile()));
                    break;
                case STEADY_STATE_HYPOTHESIS:
                    xmlProcessor.updateModelMetadata(profile.getFile(), newProfileIds.get(profile.getProfile()), getScenarioTime(dateTime), version, prepareDependentOnSet(profile.getProfile()));
                    break;
                case EQUIPMENT:
                    xmlProcessor.updateModelMetadata(profile.getFile(), newProfileIds.get(profile.getProfile()), getScenarioTime(dateTime), version, prepareDependentOnSet(profile.getProfile()));
                    break;
                case TOPOLOGY_BOUNDARY:
                    xmlProcessor.updateModelMetadata(profile.getFile(), newProfileIds.get(profile.getProfile()), getScenarioTimeForBD(dateTime), version, prepareDependentOnSet(profile.getProfile()));
                    break;
                case EQUIPMENT_BOUNDARY:
                    xmlProcessor.updateModelMetadata(profile.getFile(), newProfileIds.get(profile.getProfile()), getScenarioTimeForBD(dateTime), version, prepareDependentOnSet(profile.getProfile()));
                    break;
                default:
                    log.info(format("Default option %s", profile.getProfile()));
            }
        }
    }

    private Set<String> prepareDependentOnSet(CgmesProfileType cgmesProfileType) {
        Set<String> dependencies = new HashSet<>();
        for (CgmesProfileType findCgmesProfileType : igmMap.get(cgmesProfileType)) {
            dependencies.add(newProfileIds.get(findCgmesProfileType));
        }
        return dependencies;
    }
}
