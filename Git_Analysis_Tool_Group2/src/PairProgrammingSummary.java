import java.util.HashMap;
import java.util.List;
import java.util.Map;

//do summary function
public class PairProgrammingSummary {
    public static Map<String, Integer> getDriverFrequencies(List<CommitData> pairProgrammingCommits) {
        Map<String, Integer> driverFrequency = new HashMap<>();
        for (CommitData commit : pairProgrammingCommits) {
            Map<String, List<String>> roles = PairProgrammingDetector.detectRoles(commit.getMessage());
            for (String driver : roles.get("Driver")) {
                driverFrequency.put(driver, driverFrequency.getOrDefault(driver, 0) + 1);
            }
        }
        return driverFrequency;
    }

    public static Map<String, Integer> getNavigatorFrequencies(List<CommitData> pairProgrammingCommits) {
        Map<String, Integer> navigatorFrequency = new HashMap<>();
        for (CommitData commit : pairProgrammingCommits) {
            Map<String, List<String>> roles = PairProgrammingDetector.detectRoles(commit.getMessage());
            for (String navigator : roles.get("Navigator")) {
                navigatorFrequency.put(navigator, navigatorFrequency.getOrDefault(navigator, 0) + 1);
            }
        }
        return navigatorFrequency;
    }

    public static Map<String, Integer> getPairFrequencies(List<CommitData> pairProgrammingCommits) {
        Map<String, Integer> pairFrequency = new HashMap<>();
        for (CommitData commit : pairProgrammingCommits) {
            Map<String, List<String>> roles = PairProgrammingDetector.detectRoles(commit.getMessage());
            for (String driver : roles.get("Driver")) {
                for (String navigator : roles.get("Navigator")) {
                    String pair = driver + " - " + navigator;
                    pairFrequency.put(pair, pairFrequency.getOrDefault(pair, 0) + 1);
                }
            }
        }
        return pairFrequency;
    }
}

