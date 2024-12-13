import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.util.*;
import java.util.regex.*;

public class PairProgrammingDetector {
    // Enhanced regex patterns for Driver and Navigator
    private static final Pattern DRIVER_PATTERN = Pattern.compile("\\[(?i:Driver):\\s*(\\w+)\\]", Pattern.CASE_INSENSITIVE);
    private static final Pattern NAVIGATOR_PATTERN = Pattern.compile("\\[(?i:(Navigator|Naviagtor|Nevigator)):\\s*(\\w+)\\]", Pattern.CASE_INSENSITIVE);


    // Detect Driver and Navigator roles in a commit message
    // Detect Driver and Navigator roles in a commit message
    public static Map<String, List<String>> detectRoles(String commitMessage) {
        Map<String, List<String>> roles = new HashMap<>();
        roles.put("Driver", new ArrayList<>());
        roles.put("Navigator", new ArrayList<>());

        // Split the message into lines and process each line
        String[] lines = commitMessage.split("\n");
        for (String line : lines) {
            line = line.trim(); // Trim whitespace from each line

            // Detect Driver roles
            Matcher driverMatcher = DRIVER_PATTERN.matcher(line);
            while (driverMatcher.find()) {
                roles.get("Driver").add(driverMatcher.group(1)); // Extract Driver ID
            }

            // Detect Navigator roles
            Matcher navigatorMatcher = NAVIGATOR_PATTERN.matcher(line);
            while (navigatorMatcher.find()) {
                roles.get("Navigator").add(navigatorMatcher.group(2)); // Extract Navigator ID
            }
        }

        return roles;
    }


    public static List<CommitData> detectPairProgrammingCommits(List<CommitData> commitDataList) {
        List<CommitData> pairProgrammingCommits = new ArrayList<>();

        for (CommitData commit : commitDataList) {
            // Normalize the commit message for consistent processing
            String normalizedMessage = commit.getMessage().replace("\r", "").trim();

            // Detect roles
            Map<String, List<String>> roles = PairProgrammingDetector.detectRoles(normalizedMessage);

            // Include commit if roles are present
            if (roles.get("Driver") != null && !roles.get("Driver").isEmpty() ||
                    roles.get("Navigator") != null && !roles.get("Navigator").isEmpty()) {
                pairProgrammingCommits.add(commit);
            }
        }

        return pairProgrammingCommits;
    }
}
