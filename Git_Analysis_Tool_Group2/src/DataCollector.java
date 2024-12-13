import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.text.SimpleDateFormat;
import java.util.*;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.patch.FileHeader;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
public class DataCollector {
    private Repository repository;

    public DataCollector(String repoPath) {
        try {
            this.repository = new FileRepositoryBuilder()
                    .setGitDir(new File(repoPath + "/.git"))
                    .readEnvironment()
                    .findGitDir()
                    .build();
            System.out.println("Repository initialized: " + repository.getDirectory());
        } catch (Exception e) {
            System.err.println("Error initializing repository: " + e.getMessage());
        }
    }

    public Repository getRepository() {
        return repository;
    }
    public List<CommitData> collectCommitData() {
        List<CommitData> commitDataList = new ArrayList<>();
        try (Git git = new Git(repository)) {
            Iterable<RevCommit> commits = git.log().call();
            RevCommit previousCommit = null;

            for (RevCommit commit : commits) {
                String author = commit.getAuthorIdent().getName();
                String timestamp = commit.getAuthorIdent().getWhen().toString();
                String message = commit.getFullMessage();

                int linesAdded = 0;  // Initialize here
                int linesDeleted = 0;  // Initialize here

                if (previousCommit != null) {
                    try (DiffFormatter diffFormatter = new DiffFormatter(new ByteArrayOutputStream())) {
                        diffFormatter.setRepository(repository);
                        List<DiffEntry> diffs = diffFormatter.scan(previousCommit.getTree(), commit.getTree());

                        for (DiffEntry diff : diffs) {
                            FileHeader fileHeader = diffFormatter.toFileHeader(diff);

                            // Lines added
                            linesAdded += fileHeader.toEditList().stream()
                                    .mapToInt(edit -> edit.getEndA() - edit.getBeginA())
                                    .sum();

                            // Lines deleted
                            linesDeleted += fileHeader.toEditList().stream()
                                    .mapToInt(edit -> edit.getEndB() - edit.getBeginB())
                                    .sum();
                        }
                    }
                }

                // Add the previous commit with calculated lines added/deleted
                if (previousCommit != null) {
                    commitDataList.add(new CommitData(
                            previousCommit.getAuthorIdent().getName(),
                            previousCommit.getAuthorIdent().getWhen().toString(),
                            previousCommit.getFullMessage(),
                            linesAdded,
                            linesDeleted
                    ));
                }

                previousCommit = commit; // Move to the next commit
            }

            // Handle the last commit (root commit has no parent to compare with)
            if (previousCommit != null) {
                commitDataList.add(new CommitData(
                        previousCommit.getAuthorIdent().getName(),
                        previousCommit.getAuthorIdent().getWhen().toString(),
                        previousCommit.getFullMessage(),
                        0, // Root commit has no diffs
                        0
                ));
            }
        } catch (Exception e) {
            System.err.println("Error collecting commit data: " + e.getMessage());
        }
        return commitDataList;
    }

    // Add the getCommitsPerDay method
    public static Map<String, Integer> getCommitsPerDay(List<CommitData> commitDataList) {
        Map<String, Integer> commitsPerDay = new TreeMap<>(); // TreeMap to maintain sorted order by date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (CommitData commit : commitDataList) {
            try {
                // Extract date from timestamp
                String date = dateFormat.format(new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
                        .parse(commit.getTimestamp()));
                commitsPerDay.put(date, commitsPerDay.getOrDefault(date, 0) + 1);
            } catch (Exception e) {
                System.err.println("Error parsing timestamp: " + commit.getTimestamp());
            }
        }

        return commitsPerDay;
    }
    public static Map<String, Map<String, Integer>> getCommitsPerPersonPerDay(List<CommitData> commitDataList) {
        Map<String, Map<String, Integer>> commitsPerPersonPerDay = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        TreeSet<String> allDates = new TreeSet<>(); // Sorted set to store all unique dates

        for (CommitData commit : commitDataList) {
            try {
                // Extract the date from the timestamp
                String date = dateFormat.format(new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
                        .parse(commit.getTimestamp()));

                // Add the date to the set of all unique dates
                allDates.add(date);

                // Initialize contributor's data if not present
                commitsPerPersonPerDay.putIfAbsent(commit.getAuthor(), new TreeMap<>());

                // Increment the commit count for the contributor on that date
                Map<String, Integer> personData = commitsPerPersonPerDay.get(commit.getAuthor());
                personData.put(date, personData.getOrDefault(date, 0) + 1);
            } catch (Exception e) {
                System.err.println("Error parsing timestamp: " + commit.getTimestamp());
            }
        }

        // Fill missing dates with zeroes for each contributor
        for (Map.Entry<String, Map<String, Integer>> entry : commitsPerPersonPerDay.entrySet()) {
            Map<String, Integer> personData = entry.getValue();
            for (String date : allDates) {
                personData.putIfAbsent(date, 0); // Add missing dates with a value of 0
            }
        }

        return commitsPerPersonPerDay;
    }
    public static Map<String, int[]> getLinesAddedAndDeletedPerDay(List<CommitData> commitDataList) {
        Map<String, int[]> linesPerDay = new TreeMap<>(); // TreeMap to maintain sorted order by date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (CommitData commit : commitDataList) {
            try {
                // Extract the date from the timestamp
                String date = dateFormat.format(new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
                        .parse(commit.getTimestamp()));

                // Initialize the array if not already present
                linesPerDay.putIfAbsent(date, new int[2]);

                // Increment lines added and deleted
                linesPerDay.get(date)[0] += commit.getLinesAdded();
                linesPerDay.get(date)[1] += commit.getLinesDeleted();
            } catch (Exception e) {
                System.err.println("Error parsing timestamp: " + commit.getTimestamp());
            }
        }

        return linesPerDay;
    }

    public static Map<String, Integer> getCommitsByContributor(List<CommitData> commitDataList) {
        Map<String, Integer> commitsByContributor = new HashMap<>();

        for (CommitData commit : commitDataList) {
            String author = commit.getAuthor();
            commitsByContributor.put(author, commitsByContributor.getOrDefault(author, 0) + 1);
        }

        return commitsByContributor;
    }

}

