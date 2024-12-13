import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class PairProgramming {

    public File currentDir = new File(System.getProperty("user.dir"));
    private ArrayList<String> commitList;

    public static JTextArea console = new JTextArea();

    // Constructor that initializes the commit array list.
    public PairProgramming() {
        this.commitList = new ArrayList<>();
    }

    // Getter method that returns the commitList array.
    public ArrayList<String> getCommitList() {
        return commitList;
    }

    // Printing the contents of the commitList array. Used for debugging.
    public void printArrayData() {
        for (String s : getCommitList()) {
            console.append(s + "\n");
        }
    }

    // Creating a new "File" object that stores the current directory of the repo.
    public File getCurrentDir() {
        return currentDir;
    }

    // Method that uses JGit to create a Repository object "gitRepo"
    public Repository fetchRepository() {
        Repository gitRepo = null;
        try {
            gitRepo = new FileRepositoryBuilder()
                    .setGitDir(new File(String.valueOf(getCurrentDir()), ".git"))
                    .readEnvironment()
                    .findGitDir()
                    .build();
            console.append("You have opened the repository: " + gitRepo.getDirectory() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gitRepo;
    }

    // Method to iterate through the gitRepo object and fetch commit info.
    public ArrayList<String> fetchCommits() {
        try {
            Repository gitRepo = fetchRepository();
            Git gitInstance = new Git(gitRepo);
            Iterable<RevCommit> gitCommits = gitInstance.log().call();

            for (RevCommit commit : gitCommits) {
                String commitMessage = commit.getFullMessage();
                String commitDate = commit.getAuthorIdent().getWhen().toString();
                String commitHash = commit.getId().getName();
                String commitAuthor = commit.getAuthorIdent().getName();
                String authorEmail = commit.getAuthorIdent().getEmailAddress();

                // Committer's name and email
                PersonIdent committerIdent = commit.getCommitterIdent();
                String committerName = committerIdent.getName();
                String committerEmail = committerIdent.getEmailAddress();
                String committerDate = committerIdent.getWhen().toString();

                // Parent commit IDs
                StringBuilder parentIds = new StringBuilder();
                for (RevCommit parent : commit.getParents()) {
                    parentIds.append(parent.getId().getName()).append(", ");
                }
                if (parentIds.length() > 0) {
                    parentIds.setLength(parentIds.length() - 2); // Remove trailing comma and space
                }

                // Tree details
                RevTree tree = commit.getTree();
                String treeId = tree.getId().getName();

                // Constructing detailed commit info
                String commitInfo = String.format(
                        "Message: %s\nDate: %s\nCommit Hash: %s\nAuthor: %s <%s>\nCommitter: %s <%s>\nCommitter Date: %s\nParent IDs: %s\nTree ID: %s\n",
                        commitMessage, commitDate, commitHash, commitAuthor, authorEmail, committerName, committerEmail,
                        committerDate, parentIds, treeId
                );

                // Add the commit info to the list if it contains "driver" or "navigator"
                String normalizedMessage = commitMessage.toLowerCase().replace("naviagtor", "navigator");
                if (commitMessage.toLowerCase().contains("driver") || normalizedMessage.contains("navigator")) {
                    getCommitList().add(commitInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commitList;
    }

    public void checkForPairProgramming() {
        boolean pairProgrammingFlag = false;

        for (String s : getCommitList()) {
            if (s.contains("driver") || s.contains("navigator")) {
                pairProgrammingFlag = true;
                break;
            }
        }
        if (pairProgrammingFlag) {
            console.append("Instance of pair programming detected.");
        }
    }

    // Method that prints all the commit info of a specific pair based on their IDs
    public void printSpecificPairProgrammingInstances(String firstKeyword, String secondKeyword){
        ArrayList<String> specificCommitsList = new ArrayList<>();
        for(String s : getCommitList()){
            if(s.contains(firstKeyword) || s.contains(secondKeyword)){
                specificCommitsList.add(s);
            }
        }

        console.append(String.valueOf(specificCommitsList)+'\n');
    }

    public JTextArea getConsole(){
        return console;
    }
}
