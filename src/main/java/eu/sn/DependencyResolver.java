package eu.sn;

import java.util.*;

import static java.lang.String.format;

public class DependencyResolver {
    private static Map<CgmesProfileType, Set<CgmesProfileType>> igmMap = new HashMap<>();

    static {
        // SV
        igmMap.put(CgmesProfileType.STATE_VARIABLES, EnumSet.of(
                CgmesProfileType.STEADY_STATE_HYPOTHESIS,
                CgmesProfileType.TOPOLOGY,
                CgmesProfileType.TOPOLOGY_BOUNDARY
        ));
        // TP
        igmMap.put(CgmesProfileType.TOPOLOGY, EnumSet.of(
                CgmesProfileType.EQUIPMENT
        ));
        // SSH
        igmMap.put(CgmesProfileType.STEADY_STATE_HYPOTHESIS, EnumSet.of(
                CgmesProfileType.EQUIPMENT
        ));
        // EQ
        igmMap.put(CgmesProfileType.EQUIPMENT, EnumSet.of(
                CgmesProfileType.EQUIPMENT_BOUNDARY
        ));
        // TP_BD
        igmMap.put(CgmesProfileType.TOPOLOGY_BOUNDARY, EnumSet.of(
                CgmesProfileType.EQUIPMENT_BOUNDARY
        ));
        // EQ_BD
        igmMap.put(CgmesProfileType.EQUIPMENT_BOUNDARY, Collections.emptySet());
    }

    public static void printProfileDependenciesStatus(Set<Profile> profiles) {
        System.out.println(profiles);
        boolean allFound = true;
        for (Profile profile : profiles) {
            int currentProfileDependentOnSize = profile.getDependentOn().size();
            int igmDefinitionDependentOnSize = igmMap.get(profile.getProfile()).size();
            if (currentProfileDependentOnSize != igmDefinitionDependentOnSize) {
                System.out.println(format("The DependentOn size is different from definition. Found: %d. Should be: %d", currentProfileDependentOnSize, igmDefinitionDependentOnSize));
                allFound = false;
            } else if (new DuplicateStatus(profile.getDependentOn()).isFoundDuplicate()) {
                System.out.println(format("Found duplicate DependentOn %s in profile %s", new DuplicateStatus(profile.getDependentOn()).getDuplicateDependencies(), profile.getProfile()));
                allFound = false;
            } else {
                Set<CgmesProfileType> profilesToBeFound = igmMap.get(profile.getProfile());

                for (String dependentOnString : profile.getDependentOn()) {
                    // find profile by id
                    Optional<Profile> profileFound = profiles.stream().filter(oneProfile -> oneProfile.getProfileId().equals(dependentOnString)).findAny();
                    // verify it should be as dependency
                    if (profileFound.isPresent()) {
                        if (!profilesToBeFound.contains(profileFound.get().getProfile())) {
                            allFound = false;
                            System.out.println(format("Reference '%s' defined in profile '%s' doesn't point to any of required profiles: %s", dependentOnString, profile.getProfile(), profilesToBeFound));
                        }
                    } else {
                        allFound = false;
                        System.out.println(format("Couldn't find profile with id '%s', which is defined in profile '%s'", dependentOnString, profile.getProfile()));
                    }
                }
            }
        }
        if (allFound) {
            System.out.println("Dependencies are valid");
        }
    }
}

class DuplicateStatus {
    private boolean foundDuplicate;
    private Set<String> duplicateDependencies = new HashSet<>();

    public DuplicateStatus(Collection<String> collection) {
        Set<String> regularDependencies = new HashSet<>();
        for (String dependency : collection) {
            boolean addToRegular = regularDependencies.add(dependency);
            if (!addToRegular) {
                duplicateDependencies.add(dependency);
                foundDuplicate = true;
            }
        }
    }

    public boolean isFoundDuplicate() {
        return foundDuplicate;
    }

    public Set<String> getDuplicateDependencies() {
        return duplicateDependencies;
    }
}
