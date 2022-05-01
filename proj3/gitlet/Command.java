package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static gitlet.Utils.*;
import static gitlet.Files.*;
import static java.util.Objects.requireNonNull;

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
        Utils.writeObject(INDEX_REMOVE, toStageFiles);
        Utils.writeObject(INDEX, toStageFiles);
    }

    static void rm(String file) {
        String removeFile
                = readContentsAsString(new File(file));
        Objects removeBlob
                = new Objects(removeFile, file);
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
        List<String> allCommitHistory
                = plainFilenamesIn(BRANCHES);
        if (allCommitHistory == null) {
            return;
        }
        for (String commit : allCommitHistory) {
            StringBuilder content = new StringBuilder();
            List<String> pastCommits = pastCommits(commit);

            for (String currHead : pastCommits) {
                File currHeadFile = getObjectsFile(currHead);
                Objects curr
                        = readObject(currHeadFile, Objects.class);
                content.append("=== \n")
                        .append("commit ").append(currHead).append("\n")
                        .append("Date: ").append(curr.getTimestamp())
                        .append("\n")
                        .append(curr.getMsg()).append("\n\n");
            }

            System.out.println(content);
        }
    }

    static void find(String commitmsg) {
        List<String> allCommitHistory
                = plainFilenamesIn(BRANCHES);
        if (allCommitHistory == null) {
            System.out.println("Found no commit"
                    + " with that message.");
            return;
        }
        boolean commitFound = false;
        for (String commit : allCommitHistory) {
            List<String> pastCommits = pastCommits(commit);
            for (String currHead : pastCommits) {
                File currHeadFile = getObjectsFile(currHead);
                Objects curr
                        = readObject(currHeadFile, Objects.class);
                String msg = curr.getMsg();
                if (msg.equals(commitmsg)) {
                    commitFound = true;
                    System.out.println(currHead + "\n");
                }
            }
        }
        if (!commitFound) {
            System.out.println("Found no commit"
                    + " with that message.");
        }
    }

    static void status() {
        Objects stage = readObject(INDEX, Objects.class);
        Objects removeStage = readObject(INDEX_REMOVE, Objects.class);
        String currHead = readContentsAsString(CURR_HEAD);
        List<String> branches = plainFilenamesIn(BRANCHES);
        List<String> stageList = new ArrayList<>(
                stage.index.keySet());
        List<String> removeStageList = new ArrayList<>(
                removeStage.index.keySet());
        List<String> modifiedList = modifiedFiles();
        List<String> untrackedList = untrackedFiles();

        StringBuilder content = new StringBuilder();

        content.append("=== Branches ===\n");
        if (branches != null) {
            for (String branch : branches) {
                if (currHead.equals(branch)) {
                    content.append("*");
                }
                content.append(branch).append("\n");
            }
        }

        content.append("\n=== Staged Files ===\n");
        for (String stageContent : stageList) {
            content.append(stageContent).append("\n");
        }

        content.append("\n=== Removed Files ===\n");
        for (String removeContent : removeStageList) {
            content.append(removeContent).append("\n");
        }

        content.append("\n=== Modifications Not Staged For Commit ===\n");
        for (String modifiedContent : modifiedList) {
            content.append(modifiedContent).append("\n");
        }

        content.append("\n=== Untracked Files ===\n");
        for (String untrackedContent : untrackedList) {
            content.append(untrackedContent).append("\n");
        }
        System.out.println(content);
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
        String currHead = readContentsAsString(CURR_HEAD);
        List<String> branches = plainFilenamesIn(BRANCHES);
        List<String> untrackedList = untrackedFiles();
        Objects stagedContent = readObject(INDEX, Objects.class);
        Objects unstagedContent = readObject(INDEX_REMOVE,
                Objects.class);

        if (!untrackedList.isEmpty()) {
            System.out.println("There is an untracked file in the way;"
                    + " delete it, or add and commit it first.");
            return;
        }
        if (currHead.equals(branch)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        if (branches == null || !branches.contains(branch)) {
            System.out.println("No such branch exists.");
            return;
        }

        writeHead(branch);
        Objects commit = readObject(getObjectsFile(getHeadGeneral(branch)),
                Objects.class);
        for (Map.Entry<String, Index> allEntry : commit.index.entrySet()) {
            File entry = join(allEntry.getKey());
            updateRepoFile(entry, allEntry.getValue().getSha1());
        }

        for (String i : untrackedList) {
            restrictedDelete(i);
        }

        stagedContent.index.clear();
        unstagedContent.index.clear();
        writeObject(INDEX, stagedContent);
        writeObject(INDEX_REMOVE, unstagedContent);
    }

    static void branch(String newBranch) {
        File newBranchFile = join(BRANCHES, newBranch);
        writeContents(newBranchFile, getCurrHead());
    }

    static void rmBranch(String branch) {

    }

    static void reset(String sha1) {
        Objects stagedContent = readObject(INDEX, Objects.class);
        Objects unstagedContent = readObject(INDEX_REMOVE,
                Objects.class);
        String currBranch
                = Utils.readContentsAsString(CURR_HEAD);
        File commitFile = getObjectsFile(sha1);
        Objects commit = getObjectsHash(sha1);

        if (!untrackedFiles().isEmpty()) {
            System.out.println("There is an untracked file in the way;"
                    + " delete it, or add and commit it first.");
            return;
        }
        if (!commitFile.exists()) {
            System.out.println("No commit with that id exists.");
            return;
        }

        updateBranchHead(currBranch, sha1);

        for (String files : requireNonNull(plainFilenamesIn(CWD))) {
            if (!files.equals(".gitignore")
                    && !files.equals("Makefile")
                    && !files.equals(".proj3.iml")) {
                restrictedDelete(files);
            }
        }
        for (Map.Entry<String, Index> allEntry : commit.index.entrySet()) {
            File entry = join(allEntry.getKey());
            updateRepoFile(entry, allEntry.getValue().getSha1());
        }

        stagedContent.index.clear();
        unstagedContent.index.clear();
        writeObject(INDEX, stagedContent);
        writeObject(INDEX_REMOVE, unstagedContent);
    }

    static void merge(String branch) {

    }
}
