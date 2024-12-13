
import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTesting {
    CodeInspection ins = new CodeInspection();
    @Test
    public void firstTest() {

        // Testing that the methods work with correct input
        assert ins.isValidClassName("Class");
        assert ins.isValidMethodName("myMethod");
        assert ins.isValidVariableName("variable");

    }

    @Test
    public void secondTest() {
        // Testing that the methods work with wrong input
        assert ins.isValidClassName("class");
        assert ins.isValidMethodName("method");
        assert ins.isValidVariableName("VARIABLE");
    }

    //commitDataClassTest
    @Test
    void testConstructorAndGetters() {
        // Arrange
        String author = "Jane Doe";
        String timestamp = "2024-12-09 10:00:00";
        String message = "Initial commit";
        int linesAdded = 100;
        int linesDeleted = 50;

        // Act
        CommitData commitData = new CommitData(author, timestamp, message, linesAdded, linesDeleted);

        // Print intermediate results
        System.out.println("Testing CommitData Constructor and Getters...");
        System.out.println("Author: " + commitData.getAuthor());
        System.out.println("Timestamp: " + commitData.getTimestamp());
        System.out.println("Message: " + commitData.getMessage());
        System.out.println("Lines Added: " + commitData.getLinesAdded());
        System.out.println("Lines Deleted: " + commitData.getLinesDeleted());

        // Assert
        assertEquals(author, commitData.getAuthor());
        assertEquals(timestamp, commitData.getTimestamp());
        assertEquals(message, commitData.getMessage());
        assertEquals(linesAdded, commitData.getLinesAdded());
        assertEquals(linesDeleted, commitData.getLinesDeleted());
    }

    @Test
    void testToString() {
        // Arrange
        String author = "John Smith";
        String timestamp = "2024-12-09 11:30:00";
        String message = "Bug fix and optimization";
        int linesAdded = 20;
        int linesDeleted = 5;

        CommitData commitData = new CommitData(author, timestamp, message, linesAdded, linesDeleted);

        // Act
        String actualToString = commitData.toString();

        // Print intermediate results
        System.out.println("Testing CommitData toString()...");
        System.out.println("Generated toString:\n" + actualToString);

        // Expected toString format
        String expectedToString = String.format(
                "Author: %s\nTimestamp: %s\nMessage: %s\nLines Added: %d\nLines Deleted: %d\n",
                author, timestamp, message.trim(), linesAdded, linesDeleted
        );

        // Assert
        assertEquals(expectedToString, actualToString);
    }

    @Test
    void testEdgeCases() {
        // Test empty strings
        CommitData emptyCommit = new CommitData("", "", "", 0, 0);

        System.out.println("Testing Edge Cases with Empty Strings...");
        System.out.println("Author: " + emptyCommit.getAuthor());
        System.out.println("Timestamp: " + emptyCommit.getTimestamp());
        System.out.println("Message: " + emptyCommit.getMessage());
        System.out.println("Lines Added: " + emptyCommit.getLinesAdded());
        System.out.println("Lines Deleted: " + emptyCommit.getLinesDeleted());

        assertEquals("", emptyCommit.getAuthor());
        assertEquals("", emptyCommit.getTimestamp());
        assertEquals("", emptyCommit.getMessage());
        assertEquals(0, emptyCommit.getLinesAdded());
        assertEquals(0, emptyCommit.getLinesDeleted());

        // Test negative values
        CommitData negativeCommit = new CommitData("Author", "2024-12-09", "Negative values", -10, -20);

        System.out.println("Testing Edge Cases with Negative Values...");
        System.out.println("Lines Added: " + negativeCommit.getLinesAdded());
        System.out.println("Lines Deleted: " + negativeCommit.getLinesDeleted());

        assertEquals(-10, negativeCommit.getLinesAdded());
        assertEquals(-20, negativeCommit.getLinesDeleted());
    }

    //ContributorMetricsClassTest
    @Test
    void testConstructor() {
        String name = "John Doe";
        ContributorMetrics metrics = new ContributorMetrics(name);

        System.out.println("Testing Constructor...");
        System.out.println("Name: " + metrics.getName());
        System.out.println("Total Commits: " + metrics.getTotalCommits());
        System.out.println("Lines Added: " + metrics.getLinesAdded());
        System.out.println("Lines Deleted: " + metrics.getLinesDeleted());

        assertEquals(name, metrics.getName());
        assertEquals(0, metrics.getTotalCommits());
        assertEquals(0, metrics.getLinesAdded());
        assertEquals(0, metrics.getLinesDeleted());
    }

    @Test
    void testMetricsCalculation() {
        ContributorMetrics metrics = new ContributorMetrics("Alice");

        System.out.println("Testing Metrics Calculation...");
        metrics.incrementCommits();
        metrics.addLinesAdded(20);
        metrics.addLinesDeleted(10);

        System.out.println("Total Commits: " + metrics.getTotalCommits());
        System.out.println("Lines Added: " + metrics.getLinesAdded());
        System.out.println("Lines Deleted: " + metrics.getLinesDeleted());

        assertEquals(1, metrics.getTotalCommits());
        assertEquals(20, metrics.getLinesAdded());
        assertEquals(10, metrics.getLinesDeleted());
    }

    @Test
    void testActiveDaysCalculation() throws Exception {
        ContributorMetrics metrics = new ContributorMetrics("Bob");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println("Testing Active Days Calculation...");
        metrics.addCommitDate(dateFormat.parse("2024-12-01"));
        metrics.addCommitDate(dateFormat.parse("2024-12-01")); // Duplicate day
        metrics.addCommitDate(dateFormat.parse("2024-12-02"));

        System.out.println("Total Days Active: " + metrics.getTotalDaysActive());

        assertEquals(2, metrics.getTotalDaysActive());
    }

    @Test
    void testAverageCommitsPerDay() throws Exception {
        ContributorMetrics metrics = new ContributorMetrics("Bob");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println("Testing Average Commits Per Day...");
        metrics.incrementCommits(); // Day 1
        metrics.addCommitDate(dateFormat.parse("2024-12-01"));
        metrics.incrementCommits(); // Day 2
        metrics.addCommitDate(dateFormat.parse("2024-12-02"));

        System.out.println("Average Commits Per Day: " + metrics.getAverageCommitsPerDay());

        assertEquals(1.0, metrics.getAverageCommitsPerDay());
    }

    @Test
    void testOrganizeByContributors() {
        List<CommitData> commits = Arrays.asList(
                new CommitData("Alice", "Sun Dec 01 10:00:00 UTC 2024", "Initial commit", 100, 50),
                new CommitData("Bob", "Mon Dec 02 11:00:00 UTC 2024", "Bug fix", 50, 20),
                new CommitData("Alice", "Sun Dec 01 12:00:00 UTC 2024", "Refactor", 40, 10)
        );

        System.out.println("Testing Organize By Contributors...");
        Map<String, ContributorMetrics> metricsMap = ContributorMetrics.organizeByContributors(commits);

        metricsMap.forEach((key, value) -> System.out.println(key + ": " + value));

        assertEquals(2, metricsMap.size());
        assertEquals(2, metricsMap.get("Alice").getTotalCommits());
        assertEquals(140, metricsMap.get("Alice").getLinesAdded());
        assertEquals(60, metricsMap.get("Alice").getLinesDeleted());
    }

    @Test
    void testOrganizeByContributorsForWrongTimesTap() {
        //this will have an error in timestamp but rest code can still work
        List<CommitData> commits = Arrays.asList(
                new CommitData("Alice", "2024-12-01 10:00:00", "Initial commit", 100, 50),
                new CommitData("Bob", "2024-12-02 11:00:00", "Bug fix", 50, 20),
                new CommitData("Alice", "2024-12-01 12:00:00", "Refactor", 40, 10)
        );

        System.out.println("Testing Organize By Contributors...");
        Map<String, ContributorMetrics> metricsMap = ContributorMetrics.organizeByContributors(commits);

        metricsMap.forEach((key, value) -> System.out.println(key + ": " + value));


        assertEquals(2, metricsMap.size());
        assertEquals(2, metricsMap.get("Alice").getTotalCommits());
        assertEquals(140, metricsMap.get("Alice").getLinesAdded());
        assertEquals(60, metricsMap.get("Alice").getLinesDeleted());
    }



    @Test
    void testSortContributorsByMetric(){
        ContributorMetrics alice = new ContributorMetrics("Alice");
        alice.incrementCommits();
        alice.addLinesAdded(200);

        ContributorMetrics bob = new ContributorMetrics("Bob");
        bob.incrementCommits();
        bob.incrementCommits(); // 2 commits
        bob.addLinesAdded(150);

        Map<String, ContributorMetrics> metricsMap = new HashMap<>();
        metricsMap.put("Alice", alice);
        metricsMap.put("Bob", bob);

        System.out.println("Testing Sort Contributors By Metric...");

        List<ContributorMetrics> sortedByCommits = ContributorMetrics.sortContributorsByMetric(metricsMap, "commits");
        List<ContributorMetrics> sortedByLinesAdded = ContributorMetrics.sortContributorsByMetric(metricsMap, "linesadded");

        System.out.println("Sorted By Commits: " + sortedByCommits);
        System.out.println("Sorted By Lines Added: " + sortedByLinesAdded);

        assertEquals("Bob", sortedByCommits.get(0).getName());
        assertEquals("Alice", sortedByLinesAdded.get(0).getName());
    }

    //DataCollectorClassTest
    @Test
    void testConstructorWithValidPath() {
        // Arrange
        String repoPath = System.getProperty("user.dir");
        System.out.println("Testing DataCollector with repository path: " + repoPath);

        File gitDir = new File(repoPath + "/.git");
        if (!gitDir.exists()) {
            fail(".git directory not found in the specified path. Test aborted.");
        }

        // Act
        DataCollector collector = new DataCollector(repoPath);

        // Assert
        assertNotNull(collector.getRepository(), "Repository should be initialized.");
        System.out.println("Repository initialized successfully.");
    }

    @Test
    void testCollectCommitData() {
        // Arrange
        String repoPath = System.getProperty("user.dir");
        System.out.println("Testing DataCollector with repository path: " + repoPath);

        File gitDir = new File(repoPath + "/.git");
        if (!gitDir.exists()) {
            fail(".git directory not found in the specified path. Test aborted.");
        }

        DataCollector collector = new DataCollector(repoPath);

        // Act
        List<CommitData> commitDataList = collector.collectCommitData();

        // Assert
        assertNotNull(commitDataList, "Commit data list should not be null.");
        assertFalse(commitDataList.isEmpty(), "Commit data list should not be empty.");

        // Print collected commit data
        System.out.println("Collected Commit Data:");
        for (CommitData data : commitDataList) {
            System.out.println(data);
        }
    }

    @Test
    void testGetCommitsPerDay() {
        // Arrange
        List<CommitData> commitDataList = List.of(
                new CommitData("Alice", "Mon Dec 11 10:00:00 UTC 2023", "Commit 1", 10, 5),
                new CommitData("Bob", "Mon Dec 11 12:00:00 UTC 2023", "Commit 2", 20, 10),
                new CommitData("Alice", "Tue Dec 12 11:00:00 UTC 2023", "Commit 3", 30, 15)
        );

        // Act
        Map<String, Integer> commitsPerDay = DataCollector.getCommitsPerDay(commitDataList);

        // Assert
        assertNotNull(commitsPerDay, "Commits per day map should not be null.");
        assertEquals(2, commitsPerDay.size());
        assertEquals(2, commitsPerDay.get("2023-12-11"));
        assertEquals(1, commitsPerDay.get("2023-12-12"));

        // Print results
        System.out.println("Commits Per Day: " + commitsPerDay);
    }

    @Test
    void testGetCommitsPerPersonPerDay() {
        // Arrange
        List<CommitData> commitDataList = List.of(
                new CommitData("Alice", "Mon Dec 11 10:00:00 UTC 2023", "Commit 1", 10, 5),
                new CommitData("Alice", "Mon Dec 11 12:00:00 UTC 2023", "Commit 2", 20, 10),
                new CommitData("Bob", "Tue Dec 12 11:00:00 UTC 2023", "Commit 3", 30, 15)
        );

        // Act
        Map<String, Map<String, Integer>> commitsPerPersonPerDay = DataCollector.getCommitsPerPersonPerDay(commitDataList);

        // Assert
        assertNotNull(commitsPerPersonPerDay, "Commits per person per day map should not be null.");
        assertEquals(2, commitsPerPersonPerDay.size());
        assertEquals(2, commitsPerPersonPerDay.get("Alice").get("2023-12-11"));
        assertEquals(1, commitsPerPersonPerDay.get("Bob").get("2023-12-12"));

        // Print results
        System.out.println("Commits Per Person Per Day: " + commitsPerPersonPerDay);
    }

    @Test
    void testGetLinesAddedAndDeletedPerDay() {
        // Arrange
        List<CommitData> commitDataList = List.of(
                new CommitData("Alice", "Mon Dec 11 10:00:00 UTC 2023", "Commit 1", 10, 5),
                new CommitData("Bob", "Mon Dec 11 12:00:00 UTC 2023", "Commit 2", 20, 10),
                new CommitData("Alice", "Tue Dec 12 11:00:00 UTC 2023", "Commit 3", 30, 15)
        );

        // Act
        Map<String, int[]> linesPerDay = DataCollector.getLinesAddedAndDeletedPerDay(commitDataList);

        // Assert
        assertNotNull(linesPerDay, "Lines per day map should not be null.");
        assertEquals(2, linesPerDay.size());
        assertArrayEquals(new int[]{30, 15}, linesPerDay.get("2023-12-11"));
        assertArrayEquals(new int[]{30, 15}, linesPerDay.get("2023-12-12"));

        // Print results
        System.out.println("Lines Added and Deleted Per Day: " + linesPerDay);
    }

    @Test
    void testGetCommitsByContributor() {
        // Arrange
        List<CommitData> commitDataList = List.of(
                new CommitData("Alice", "Mon Dec 11 10:00:00 UTC 2023", "Commit 1", 10, 5),
                new CommitData("Bob", "Mon Dec 11 12:00:00 UTC 2023", "Commit 2", 20, 10),
                new CommitData("Alice", "Tue Dec 12 11:00:00 UTC 2023", "Commit 3", 30, 15)
        );

        // Act
        Map<String, Integer> commitsByContributor = DataCollector.getCommitsByContributor(commitDataList);

        // Assert
        assertNotNull(commitsByContributor, "Commits by contributor map should not be null.");
        assertEquals(2, commitsByContributor.size());
        assertEquals(2, commitsByContributor.get("Alice"));
        assertEquals(1, commitsByContributor.get("Bob"));

        // Print results
        System.out.println("Commits By Contributor: " + commitsByContributor);
    }

    //PairProgrammingDetectorClassTest
    @Test
    void testDetectRoles_withValidRoles() {
        // Arrange
        String commitMessage = """
                [Driver: Alice]
                [Navigator: Bob]
                Refactored the login module.
                """;

        // Act
        Map<String, List<String>> roles = PairProgrammingDetector.detectRoles(commitMessage);

        // Assert
        assertNotNull(roles.get("Driver"));
        assertNotNull(roles.get("Navigator"));
        assertEquals(1, roles.get("Driver").size());
        assertEquals(1, roles.get("Navigator").size());
        assertEquals("Alice", roles.get("Driver").get(0));
        assertEquals("Bob", roles.get("Navigator").get(0));

    }

    @Test
    void testDetectRoles_withValidMisspellings() {
        // Arrange
        String commitMessage = """
            [Driver Alice]
            [Naviagtor: Charlie]
            Fixed bug in payment processing.
            """;

        // Act
        Map<String, List<String>> roles = PairProgrammingDetector.detectRoles(commitMessage);

        // Assert
        assertTrue(roles.get("Driver").isEmpty(), "Driver role should not be detected due to missing colon.");
        assertFalse(roles.get("Navigator").isEmpty(), "Navigator role should be detected for 'Naviagtor'.");
        assertEquals(1, roles.get("Navigator").size());
        assertEquals("Charlie", roles.get("Navigator").get(0), "Navigator should be detected as 'Charlie'.");
    }



    @Test
    void testDetectRoles_withMultipleRoles() {
        // Arrange
        String commitMessage = """
                [Driver: Alice]
                [Navigator: Bob]
                [Driver: Charlie]
                Improved error handling.
                """;

        // Act
        Map<String, List<String>> roles = PairProgrammingDetector.detectRoles(commitMessage);

        // Assert
        assertEquals(2, roles.get("Driver").size());
        assertEquals(1, roles.get("Navigator").size());
        assertEquals("Alice", roles.get("Driver").get(0));
        assertEquals("Charlie", roles.get("Driver").get(1));
        assertEquals("Bob", roles.get("Navigator").get(0));
    }

    @Test
    void testDetectPairProgrammingCommits_withValidCommits() {
        // Arrange
        List<CommitData> commitDataList = Arrays.asList(
                new CommitData("Alice", "2024-12-01 10:00:00", "[Driver: Alice] Updated module X.", 50, 10),
                new CommitData("Bob", "2024-12-02 12:00:00", "[Navigator: Bob] Fixed bug Y.", 20, 5),
                new CommitData("Charlie", "2024-12-03 15:00:00", "Refactored module Z.", 30, 15)
        );

        // Act
        List<CommitData> pairProgrammingCommits = PairProgrammingDetector.detectPairProgrammingCommits(commitDataList);

        // Assert
        assertEquals(2, pairProgrammingCommits.size());
        assertEquals("Alice", pairProgrammingCommits.get(0).getAuthor());
        assertEquals("Bob", pairProgrammingCommits.get(1).getAuthor());
    }

    @Test
    void testDetectPairProgrammingCommits_withNoRoles() {
        // Arrange
        List<CommitData> commitDataList = Arrays.asList(
                new CommitData("Alice", "2024-12-01 10:00:00", "Updated module X.", 50, 10),
                new CommitData("Bob", "2024-12-02 12:00:00", "Fixed bug Y.", 20, 5)
        );

        // Act
        List<CommitData> pairProgrammingCommits = PairProgrammingDetector.detectPairProgrammingCommits(commitDataList);

        // Assert
        assertTrue(pairProgrammingCommits.isEmpty(), "No pair programming commits should be detected.");
    }

    @Test
    void testDetectPairProgrammingCommits_withMixedCommits() {
        // Arrange
        List<CommitData> commitDataList = Arrays.asList(
                new CommitData("Alice", "2024-12-01 10:00:00", "[Driver: Alice] Updated module X.", 50, 10),
                new CommitData("Bob", "2024-12-02 12:00:00", "Fixed bug Y.", 20, 5),
                new CommitData("Charlie", "2024-12-03 15:00:00", "[Navigator: Charlie] Improved UI.", 30, 15)
        );

        // Act
        List<CommitData> pairProgrammingCommits = PairProgrammingDetector.detectPairProgrammingCommits(commitDataList);

        // Assert
        assertEquals(2, pairProgrammingCommits.size());
        assertEquals("Alice", pairProgrammingCommits.get(0).getAuthor());
        assertEquals("Charlie", pairProgrammingCommits.get(1).getAuthor());
    }

    //PairProgrammingSummaryClassTest
    @Test
    void testGetDriverFrequencies() {
        // Arrange
        List<CommitData> pairProgrammingCommits = Arrays.asList(
                new CommitData("Alice", "2024-12-01 10:00:00", "[Driver: Alice] Refactored code.", 50, 10),
                new CommitData("Bob", "2024-12-02 12:00:00", "[Driver: Bob] Fixed bugs.", 20, 5),
                new CommitData("Charlie", "2024-12-03 15:00:00", "[Driver: Alice] Improved performance.", 30, 15)
        );

        // Act
        Map<String, Integer> driverFrequencies = PairProgrammingSummary.getDriverFrequencies(pairProgrammingCommits);

        // Assert
        assertEquals(2, driverFrequencies.size());
        assertEquals(2, driverFrequencies.get("Alice"));
        assertEquals(1, driverFrequencies.get("Bob"));
    }

    @Test
    void testGetNavigatorFrequencies() {
        // Arrange
        List<CommitData> pairProgrammingCommits = Arrays.asList(
                new CommitData("Alice", "2024-12-01 10:00:00", "[Navigator: Bob] Refactored code.", 50, 10),
                new CommitData("Bob", "2024-12-02 12:00:00", "[Navigator: Alice] Fixed bugs.", 20, 5),
                new CommitData("Charlie", "2024-12-03 15:00:00", "[Navigator: Bob] Improved performance.", 30, 15)
        );

        // Act
        Map<String, Integer> navigatorFrequencies = PairProgrammingSummary.getNavigatorFrequencies(pairProgrammingCommits);

        // Assert
        assertEquals(2, navigatorFrequencies.size());
        assertEquals(1, navigatorFrequencies.get("Alice"));
        assertEquals(2, navigatorFrequencies.get("Bob"));
    }

    @Test
    void testGetPairFrequencies() {
        // Arrange
        List<CommitData> pairProgrammingCommits = Arrays.asList(
                new CommitData("Alice", "2024-12-01 10:00:00", "[Driver: Alice] [Navigator: Bob] Refactored code.", 50, 10),
                new CommitData("Bob", "2024-12-02 12:00:00", "[Driver: Bob] [Navigator: Alice] Fixed bugs.", 20, 5),
                new CommitData("Charlie", "2024-12-03 15:00:00", "[Driver: Alice] [Navigator: Bob] Improved performance.", 30, 15)
        );

        // Act
        Map<String, Integer> pairFrequencies = PairProgrammingSummary.getPairFrequencies(pairProgrammingCommits);

        // Assert
        assertEquals(2, pairFrequencies.size());
        assertEquals(2, pairFrequencies.get("Alice - Bob"));
        assertEquals(1, pairFrequencies.get("Bob - Alice"));
    }

    @Test
    void testEmptyCommitList() {
        // Arrange
        List<CommitData> pairProgrammingCommits = List.of();

        // Act
        Map<String, Integer> driverFrequencies = PairProgrammingSummary.getDriverFrequencies(pairProgrammingCommits);
        Map<String, Integer> navigatorFrequencies = PairProgrammingSummary.getNavigatorFrequencies(pairProgrammingCommits);
        Map<String, Integer> pairFrequencies = PairProgrammingSummary.getPairFrequencies(pairProgrammingCommits);

        // Assert
        assertTrue(driverFrequencies.isEmpty(), "Driver frequencies should be empty.");
        assertTrue(navigatorFrequencies.isEmpty(), "Navigator frequencies should be empty.");
        assertTrue(pairFrequencies.isEmpty(), "Pair frequencies should be empty.");
    }

    @Test
    void testCommitsWithoutRoles() {
        // Arrange
        List<CommitData> pairProgrammingCommits = Arrays.asList(
                new CommitData("Alice", "2024-12-01 10:00:00", "Updated documentation.", 50, 10),
                new CommitData("Bob", "2024-12-02 12:00:00", "Refactored tests.", 20, 5)
        );

        // Act
        Map<String, Integer> driverFrequencies = PairProgrammingSummary.getDriverFrequencies(pairProgrammingCommits);
        Map<String, Integer> navigatorFrequencies = PairProgrammingSummary.getNavigatorFrequencies(pairProgrammingCommits);
        Map<String, Integer> pairFrequencies = PairProgrammingSummary.getPairFrequencies(pairProgrammingCommits);

        // Assert
        assertTrue(driverFrequencies.isEmpty(), "Driver frequencies should be empty.");
        assertTrue(navigatorFrequencies.isEmpty(), "Navigator frequencies should be empty.");
        assertTrue(pairFrequencies.isEmpty(), "Pair frequencies should be empty.");
    }
}
