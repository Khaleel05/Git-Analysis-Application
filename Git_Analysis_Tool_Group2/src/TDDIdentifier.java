import java.util.*;
import java.util.stream.Collectors;

public class TDDIdentifier {

    public TDDIdentifier() {
    }

    public List<String> detectTDDCommits(List<CommitData> commitDataList) {
        List<String> tddCommits = new ArrayList<>();

        // Sort commits by timestamp
        commitDataList.sort(Comparator.comparing(CommitData::getTimestamp));

        for (int i = 1; i < commitDataList.size(); i++) {
            CommitData currentCommit = commitDataList.get(i);
            CommitData previousCommit = commitDataList.get(i - 1);

            // Check if the previous commit is a test commit
            if (isTestCommit(previousCommit) && !isTestCommit(currentCommit)) {
                tddCommits.add("TDD Detected: " + previousCommit.getTimestamp() + " -> " + currentCommit.getTimestamp());
            }
        }

        return tddCommits;
    }
    public static boolean isTestCommit(CommitData commitData) {
        // Example heuristic: check for the presence of "test" in the file paths or commit messages
        String message = commitData.getMessage().toLowerCase();
        return message.contains("test") || message.contains("unit");
    }
}
