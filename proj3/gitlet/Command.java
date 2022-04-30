package gitlet;

import java.io.File;
import java.util.*;
import static gitlet.Utils.*;
import static gitlet.Files.*;

public class Command {
    static void init() {
        initDir();
        Objects initCommit = new Objects();
        writeObject(initCommit);
    }

    static void add(String file) {
        File newFile = join(file);
        Objects blob = new Objects(readContentsAsString(newFile), file);
        writeObject(blob);
    }

    static void commit(String msg) {
        Objects toStageFiles = readObject(INDEX, Objects.class);
        Objects stageRemove = readObject(INDEX_REMOVE, Objects.class);
        if (toStageFiles.index.isEmpty() && stageRemove.index.isEmpty()) {
            System.out.println("No changes added to the commit.");
        }

        Objects currHead = getCurrHeadCommit();
        currHead.updateIndex(toStageFiles, stageRemove);
        currHead.makeCommit(msg);

        writeObject(currHead);
        toStageFiles.index.clear();
        stageRemove.index.clear();
        writeObject(INDEX_REMOVE, toStageFiles);
        writeObject(INDEX, toStageFiles);
    }

    static void rm(String file) {
        File removeFile = new File(file);
        Objects removeBlob = new Objects(readContentsAsString(removeFile), file);
        updateRemoveStage(removeBlob);
    }

    static void log() {
        StringBuilder content = new StringBuilder();
        List<String> pastCommits = pastCommits(readContentsAsString(CURR_HEAD));

        for (String currHead : pastCommits) {
            Objects curr = readObject(getObjectsFile(currHead), Objects.class);
            content.append("=== \n")
                    .append("commit ").append(currHead).append("\n")
                    .append("Date: ").append(curr.getTimestamp()).append("\n")
                    .append(curr.getMsg()).append("\n\n");
        }

        System.out.println(content);
    }

    static void globalLog() {
        List<String> allCommitHistory = plainFilenamesIn(BRANCHES);
        if (allCommitHistory == null) {
            return;
        }
        for (String commit : allCommitHistory) {
            StringBuilder content = new StringBuilder();
            List<String> pastCommits = pastCommits(commit);

            for (String currHead : pastCommits) {
                Objects curr = readObject(getObjectsFile(currHead), Objects.class);
                content.append("=== \n")
                        .append("commit ").append(currHead).append("\n")
                        .append("Date: ").append(curr.getTimestamp()).append("\n")
                        .append(curr.getMsg()).append("\n\n");
            }

            System.out.println(content);
        }
    }

    static void find(String commitmsg) {
        List<String> allCommitHistory = plainFilenamesIn(BRANCHES);
        if (allCommitHistory == null) {
            System.out.println("Found no commit with that message.");
            return;
        }
        boolean commitFound = false;
        for (String commit : allCommitHistory) {
            List<String> pastCommits = pastCommits(commit);
            for (String currHead : pastCommits) {
                Objects curr = readObject(getObjectsFile(currHead), Objects.class);
                String msg = curr.getMsg();
                if (msg.equals(commitmsg)) {
                    commitFound = true;
                    System.out.println(currHead + "\n");
                }
            }
        }
        if (!commitFound) {
            System.out.println("Found no commit with that message.");
        }
    }

    static void status() {

    }

    static void checkoutPastFile(String sha1, String file) {
        String currHead = readContentsAsString(CURR_HEAD);
        List<String> pastCommits = pastCommits(currHead);
        File pastFile = null;

        if (pastCommits.contains(sha1)) {
            pastFile = getObjectsFile(sha1);
        }
        if (pastFile == null) {
            System.out.println("No commit with that id exists.");
            return;
        }

        Objects commit = readObject(pastFile, Objects.class);

        if (!commit.index.containsKey(file)) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        Index ver = commit.index.get(file);
        File newBlobLoc = join(file);
        updateRepoFile(newBlobLoc, ver.getSha1());
    }

    static void checkoutHeadFile(String file) {
        Objects commit = getCurrHeadCommit();

        if (!commit.index.containsKey(file)) {
            throw error("File does not exist in that commit.");
        }

        String hash = commit.index.get(file).getSha1();
        File newBlobLoc = join(file);
        updateRepoFile(newBlobLoc, hash);
    }

    static void checkoutBranch(String branch) {

    }

    static void branch(String newBranch) {
        File newBranchFile = join(BRANCHES, newBranch);
        newBranchFile.mkdirs();
        updateBranchHead(newBranch, getCurrHead());
    }

    static void rmBranch(String branch) {

    }

    static void reset(String sha1) {

    }

    static void merge(String branch) {

    }
}
