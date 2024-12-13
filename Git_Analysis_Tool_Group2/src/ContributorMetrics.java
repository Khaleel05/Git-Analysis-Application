import java.text.SimpleDateFormat;
import java.util.*;

public class ContributorMetrics {
    private String name; // Contributor's name
    private int totalCommits; // Total commits made by the contributor
    private int linesAdded; // Total lines of code added
    private int linesDeleted; // Total lines of code deleted

    private List<Date> commitDates; // Store commit timestamps

    // Constructor
    public ContributorMetrics(String name) {
        this.name = name;
        this.totalCommits = 0;
        this.linesAdded = 0;
        this.linesDeleted = 0;
        this.commitDates = new ArrayList<>();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public int getTotalCommits() {
        return totalCommits;
    }

    public int getLinesAdded() {
        return linesAdded;
    }

    public int getLinesDeleted() {
        return linesDeleted;
    }

    // Increment methods
    public void incrementCommits() {
        totalCommits++;
    }

    public void addLinesAdded(int lines) {
        linesAdded += lines;
    }

    public void addLinesDeleted(int lines) {
        linesDeleted += lines;
    }

    // Add commit date to the list
    public void addCommitDate(Date date) {
        commitDates.add(date);
    }
    public int getTotalDaysActive() {
        // Use a Set to count unique days
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Set<String> uniqueDays = new HashSet<>();
        for (Date date : commitDates) {
            uniqueDays.add(dateFormat.format(date));
        }
        return uniqueDays.size();
    }
    public double getAverageCommitsPerDay() {
        int totalDaysActive = getTotalDaysActive();
        return totalDaysActive == 0 ? 0 : (double) totalCommits / totalDaysActive;
    }

    @Override
    public String toString() {
        return String.format(
                "Contributor: %s\nTotal Commits: %d\nLines Added: %d\nLines Deleted: %d\nTotal Days Active: %d\nAverage Commits Per Day: %.2f\n",
                name, totalCommits, linesAdded, linesDeleted, getTotalDaysActive(), getAverageCommitsPerDay()
        );
    }

    // Static method to organize contributors
    public static Map<String, ContributorMetrics> organizeByContributors(List<CommitData> commitDataList) {
        Map<String, ContributorMetrics> contributorMetricsMap = new HashMap<>();

        for (CommitData commit : commitDataList) {
            String author = commit.getAuthor();
            contributorMetricsMap.putIfAbsent(author, new ContributorMetrics(author));
            ContributorMetrics metrics = contributorMetricsMap.get(author);

            metrics.incrementCommits();
            metrics.addLinesAdded(commit.getLinesAdded());
            metrics.addLinesDeleted(commit.getLinesDeleted());

            // Add commit timestamp
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                metrics.addCommitDate(dateFormat.parse(commit.getTimestamp()));
            } catch (Exception e) {
                System.err.println("Error parsing timestamp: " + commit.getTimestamp());
            }
        }

        return contributorMetricsMap;
    }

    public static List<ContributorMetrics> sortContributorsByMetric(
            Map<String, ContributorMetrics> contributorMetricsMap, String metric) {
        List<ContributorMetrics> sortedContributors = new ArrayList<>(contributorMetricsMap.values());

        switch (metric.toLowerCase()) {
            case "commits":
                sortedContributors.sort((a, b) -> Integer.compare(b.getTotalCommits(), a.getTotalCommits()));
                break;
            case "linesadded":
                sortedContributors.sort((a, b) -> Integer.compare(b.getLinesAdded(), a.getLinesAdded()));
                break;
            case "linesdeleted":
                sortedContributors.sort((a, b) -> Integer.compare(b.getLinesDeleted(), a.getLinesDeleted()));
                break;
        }

        return sortedContributors;
    }
}
