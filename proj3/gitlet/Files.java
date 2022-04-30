package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static gitlet.Utils.*;

/* Files class. For all commands and files that utilized for the
 * file control of the system. */
public class Files {

    /* Initialize working directory. */
    static void initDir() {
        CWD.mkdirs();
        OBJECTS.mkdirs();
        BRANCHES.mkdirs();
        Objects fileList = new Objects("stage");
        Utils.writeObject(INDEX, fileList);
        Objects fileListRm = new Objects("stage");
        Utils.writeObject(INDEX_REMOVE, fileListRm);
        writeContents(CURR_HEAD, "master");
    }

    /* Write Objects in the OBJECTS directory.
     * Save by the first 2 letters of sha1 (same as Git). */
    static void writeObject(Objects object) {
        String sha1 = sha1(serialize(object));
        join(OBJECTS, getHeadHash(sha1)).mkdir();
        File file = getObjectsFile(sha1);

        if (object.getType().equals("commit")) {
            String currBranch
                    = Utils.readContentsAsString(CURR_HEAD);
            writeHead(currBranch);
            updateBranchHead(currBranch, sha1);
            Utils.writeObject(file, object);
        } else {
            if (writeStagedToIndex(sha1, object.getFileName())) {
                Utils.writeObject(file, object);
            }
        }
    }

    /* Write the files that wait for staging into the INDEX file. */
    static boolean writeStagedToIndex(String sha1, String filename) {
        Objects fileList;
        fileList = readObject(INDEX, Objects.class);

        if (fileList.index.containsKey(filename)) {
            if (sha1.equals(fileList.index.get(filename).getSha1())) {
                return false;
            }
        }

        Index updated = new Index(sha1, filename);
        fileList.index.put(filename, updated);
        Utils.writeObject(INDEX, fileList);
        return true;
    }

    /* Write / update the current HEAD. */
    static void writeHead(String sha1) {
        writeContents(CURR_HEAD, sha1);
    }

    /* Update the sha1 code of the Head of the very branch. */
    static void updateBranchHead(String branch, String sha1) {
        writeContents(join(BRANCHES, branch), sha1);
    }

    /* Put the content of given blob into a new one.
     * For checkout use. */
    static void updateRepoFile(File newBlobLoc, String blobHash) {
        File repo = getObjectsFile(blobHash);
        String content = Utils.readObject(repo, Objects.class).getContent();
        Utils.writeContents(newBlobLoc, content);
    }

    /* For updating the stage of removal, INDEX_REMOVE.
     * Utilized for command rm. */
    static void updateRemoveStage(Objects removeBlob) {
        File currHead = getObjectsFile(getCurrHead());
        Objects currHeadBlob = readObject(currHead, Objects.class);
        Objects stage = readObject(INDEX, Objects.class);
        String removeSha1 = sha1(removeBlob);
        if (currHeadBlob.index.containsKey(removeBlob.getFileName())) {
            Objects removeStage = readObject(INDEX_REMOVE, Objects.class);
            if (removeStage == null) {
                removeStage = new Objects("stage");
            }
            Index entry = new Index(removeSha1, removeBlob.getFileName());
            removeStage.index.put(removeBlob.getFileName(), entry);
            Utils.writeObject(INDEX_REMOVE, removeStage);
            restrictedDelete(new File(removeBlob.getFileName()));
        } else if (stage.index.containsKey(removeBlob.getFileName())) {
            stage.index.remove(removeBlob.getFileName());
            Utils.writeObject(INDEX, stage);
        } else {
            System.out.println("No reason to remove the file.");
        }
    }

    /* The strategy to save files in this system is to use
     * its sha1 code like Git does.
     * E.g., we have a new file whose sha1 code is
     * 0b2a40250fe9328e751984616...
     * The system would save the file in
     * CWD -> OBJECTS -> 0b -> 2a40250fe9328e751984616...
     */

    /* Get the head of the sha1 code from the provided sha1.
     * Use for saving files by Head order in the OBJECTS directory. */
    static String getHeadHash(String sha1) {
        return sha1.substring(0, 2);
    }

    /* Get the body of the sha1 code from the provided sha1.
     * Use for saving files by Body order in its HEAD directory. */
    static String getBodyHash(String sha1) {
        return sha1.substring(2);
    }

    /* Get the file directory from the provided sha1. */
    static File getObjectsFile(String sha1) {
        return join(OBJECTS, getHeadHash(sha1), getBodyHash(sha1));
    }

    /* Get the Object of the given sha1 hash. */
    static Objects getObjectsHash(String hash) {
        File obj = getObjectsFile(hash);
        return readObject(obj, Objects.class);
    }


    /* Get current Head as a string of its sha1. */
    static String getCurrHead() {
        String curr = readContentsAsString(CURR_HEAD);
        return getHeadGeneral(curr);
    }

    /* Get current commit as an Object. */
    static Objects getCurrHeadCommit() {
        File commit = getObjectsFile(getCurrHead());
        return readObject(commit, Objects.class);
    }

    /* Get the Head as a string of its sha1 for the given
     * name of the branch. */
    static String getHeadGeneral(String branchName) {
        File branchHead = join(BRANCHES, branchName);
        return readContentsAsString(branchHead);
    }

    /* Get history of commits under current Branch. Saved as a List. */
    static List<String> pastCommits(String headHash) {
        String currHash = readContentsAsString(join(BRANCHES, headHash));
        List<String> pastCommits = new ArrayList<>();
        while (!currHash.equals("")) {
            pastCommits.add(currHash);
            Objects currCommit = getObjectsHash(currHash);
            currHash = currCommit.getParent();
        }
        return pastCommits;
    }

    static List<String> modifiedFiles(Objects stagedContent, Objects unstagedContent) {
        Objects currHeadCommit = getCurrHeadCommit();
        List<String> modified = new ArrayList<>();

        for (String staged : stagedContent.index.keySet()) {
            System.out.println(staged);
            String sha1 = stagedContent.index.get(staged).getSha1();
            String content = readContentsAsString(Utils.join(staged));
            if (!Utils.sha1(new Objects(content, staged)).equals(sha1)) {
                modified.add(staged + " (modified)");
            }
        }

        for (String currCommit : currHeadCommit.index.keySet()) {
            String sha1 = currHeadCommit.index.get(currCommit).getSha1();
            File commitVer = new File(currCommit);
            String content = readContentsAsString(commitVer);

            if (!stagedContent.index.containsKey(currCommit)) {
                if (!sha1(new Objects(content, currCommit)).equals(sha1)) {
                    modified.add(currCommit + " (modified)");
                } else if (!commitVer.exists()) {
                    modified.add(currCommit + " (deleted)");
                }
            }
        }

        for (String unstaged : unstagedContent.index.keySet()) {
            modified.remove(unstaged + " (deleted)");
        }

        return modified;
    }

    static List<String> untrackedFiles(Objects stagedContent, Objects removedContent) {
        Objects currHeadCommit = getCurrHeadCommit();
        List<String> untracked = new ArrayList<>();

        for (String files : java.util.Objects.requireNonNull(plainFilenamesIn(CWD))) {
            if (!currHeadCommit.index.containsKey(files)) {
                untracked.add(files);
            }
        }

        untracked.removeIf(file -> stagedContent.index.containsKey(file)
                || removedContent.index.containsKey(file));

        return untracked;
    }


    /* Necessary Files. */

    /** Current working directory. */
    static final File CWD = new File(".gitlet");

    /** All objects needed, including commits, blobs, and trees. */
    static final File OBJECTS = join(CWD, "objects");

    /** Current Head pointer. */
    static final File CURR_HEAD = join(CWD, "head");

    /** Recording all branches, remotes, and tags. */
    static final File REFS = join(CWD, "refs");

    /** Recording Hashmap of filename and index (sha1) of all files. */
    static final File INDEX = join(CWD, "index");

    /** Recording Hashmap of filename and index (sha1) of all files
     * to be removed. */
    static final File INDEX_REMOVE = join(CWD, "index-remove");

    /** Recording all branches. */
    static final File BRANCHES = join(REFS, "heads");

}
