import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

class TDDIdentifierTest {

    private TDDIdentifier tddIdentifier;

    @BeforeEach
    void setUp() {
        // Initialize the TDDIdentifier class
        tddIdentifier = new TDDIdentifier();
    }

    @Test
    void testDetectTDDCommits_withTDDPattern() {
        // Create a list of commits with a TDD pattern: test commit followed by non-test commit
        List<CommitData> commitDataList = new ArrayList<>();
        commitDataList.add(new CommitData("Author1", "2024-12-03T10:00:00", "Added unit test for bug fix", 20, 5));
        commitDataList.add(new CommitData("Author2", "2024-12-03T10:05:00", "Fixed bug in authentication", 10, 2));
        commitDataList.add(new CommitData("Author3", "2024-12-03T10:10:00", "Refactored code", 15, 3));
        commitDataList.add(new CommitData("Author4", "2024-12-03T10:15:00", "Added unit test for feature", 25, 6));

        List<String> result = tddIdentifier.detectTDDCommits(commitDataList);

        // Expected TDD pattern: test commit at index 0, non-test commit at index 1
        assertEquals(1, result.size(), "TDD pattern should be detected once.");
        assertTrue(result.contains("TDD Detected: 2024-12-03T10:00:00 -> 2024-12-03T10:05:00"));
    }

    @Test
    void testDetectTDDCommits_noTDDPattern() {//
        // Create a list of commits with no TDD pattern (test and non-test commits are interspersed)
        List<CommitData> commitDataList = new ArrayList<>();
        commitDataList.add(new CommitData("Author1", "2024-12-03T10:00:00", "Fixed bug in authentication", 20, 5));
        commitDataList.add(new CommitData("Author2", "2024-12-03T10:05:00", "Added unit test for feature", 25, 6));
        commitDataList.add(new CommitData("Author3", "2024-12-03T10:10:00", "Improved UI", 15, 3));
        commitDataList.add(new CommitData("Author4", "2024-12-03T10:15:00", "Refactored code", 30, 5));

        List<String> result = tddIdentifier.detectTDDCommits(commitDataList);

        // No TDD pattern should be detected
        assertTrue(result.isEmpty(), "There should be no TDD pattern detected.");
    }

    @Test
    void testDetectTDDCommits_emptyList() {
        // Create an empty list
        List<CommitData> commitDataList = new ArrayList<>();

        List<String> result = tddIdentifier.detectTDDCommits(commitDataList);

        // No commits should result in no TDD patterns detected
        assertTrue(result.isEmpty(), "No TDD patterns should be detected for an empty list.");
    }

    @Test
    void testDetectTDDCommits_singleCommit() {
        // List with a single commit (no TDD pattern possible)
        List<CommitData> commitDataList = new ArrayList<>();
        commitDataList.add(new CommitData("Author1", "2024-12-03T10:00:00", "Initial commit", 20, 5));

        List<String> result = tddIdentifier.detectTDDCommits(commitDataList);

        // No TDD pattern should be detected with a single commit
        assertTrue(result.isEmpty(), "No TDD patterns should be detected with a single commit.");
    }

    @Test
    void testIsTestCommit_withTestMessage() {
        // Commit message contains "test"
        CommitData commitData = new CommitData("Author1", "2024-12-03T10:00:00", "Added unit test for bug fix", 20, 5);

        assertTrue(TDDIdentifier.isTestCommit(commitData), "Commit should be identified as a test commit.");
    }

    @Test
    void testIsTestCommit_withUnitMessage() {
        // Commit message contains "unit"
        CommitData commitData = new CommitData("Author2", "2024-12-03T10:05:00", "Refactored unit tests", 15, 3);

        assertTrue(TDDIdentifier.isTestCommit(commitData), "Commit should be identified as a test commit.");
    }

    @Test
    void testIsTestCommit_withoutTestOrUnitMessage() {
        // Commit message doesn't contain "test" or "unit"
        CommitData commitData = new CommitData("Author3", "2024-12-03T10:10:00", "Implemented new feature", 10, 2);

        assertFalse(TDDIdentifier.isTestCommit(commitData), "Commit should not be identified as a test commit.");
    }

    @Test
    void testIsTestCommit_withMixedCaseTestMessage() {
        // Commit message contains "Test" (case insensitive)
        CommitData commitData = new CommitData("Author4", "2024-12-03T10:15:00", "Added Test for bug fix", 30, 0);

        assertTrue(TDDIdentifier.isTestCommit(commitData), "Commit should be identified as a test commit.");
    }

    @Test
    void testIsTestCommit_withMixedCaseUnitMessage() {
        // Commit message contains "UNIT" (case insensitive)
        CommitData commitData = new CommitData("Author5", "2024-12-03T10:20:00", "Refactored UNIT tests", 25, 5);

        assertTrue(TDDIdentifier.isTestCommit(commitData), "Commit should be identified as a test commit.");
    }

    @Test
    void testIsTestCommit_withEmptyMessage() {
        // Commit message is empty
        CommitData commitData = new CommitData("Author6", "2024-12-03T10:25:00", "", 5, 1);

        assertFalse(TDDIdentifier.isTestCommit(commitData), "Commit should not be identified as a test commit.");
    }

    @Test
    void testIsTestCommit_withNullMessage() {
        // Commit message is null
        CommitData commitData = new CommitData("Author7", "2024-12-03T10:30:00", null, 12, 4);

        assertFalse(TDDIdentifier.isTestCommit(commitData), "Commit should not be identified as a test commit.");
    }
}
