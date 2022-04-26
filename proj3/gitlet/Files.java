package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static gitlet.Utils.*;

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
    static void writeObject(Objects Object) {
        String sha1 = sha1(serialize(Object));
        join(OBJECTS, getHeadHash(sha1)).mkdir();
        File file = getObjectsFile(sha1);

        if (Object.getType().equals("commit")) {
            String currHead
                    = Utils.readContentsAsString(CURR_HEAD);
            writeHead(currHead);
            updateBranchHead(currHead, sha1);
        } else {
            Objects stageList = readObject(INDEX, Objects.class);
            Index update = new Index(sha1, Object.getFileName());
            stageList.index.put(Object.getFileName(), update);
            Utils.writeObject(INDEX, stageList);
        }
        Utils.writeObject(file, Object);
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


    /* The strategy to save files in this system is to use its sha1 code like Git does.
     * E.g., we have a new file whose sha1 code is 0b2a40250fe9328e751984616...
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


    /* Get current Head as a string containing its sha1. */
    static String getCurrHead() {
        String curr = readContentsAsString(CURR_HEAD);
        return getHeadGeneral(curr);
    }

    /* Get current commit as an Object. */
    static Objects getCurrHeadCommit() {
        File commit = getObjectsFile(getCurrHead());
        return readObject(commit, Objects.class);
    }

    /* Get the Head as a string containing its sha1 for the given
     * sha1 of the branch. */
    static String getHeadGeneral(String branchHash) {
        File branchHead = join(BRANCHES, branchHash);
        return readContentsAsString(branchHead);
    }

    /* Get the commit as an Object for the given sha1 of the branch. */
    static Objects getHeadCommitGeneral(String branchHash) {
        File commit = getObjectsFile(getHeadGeneral(branchHash));
        return readObject(commit, Objects.class);
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

    /* Necessary Files. */

    /* Current working directory. */
    static final File CWD = new File(".gitlet");

    /* All objects needed, including commits, blobs, and trees. */
    static final File OBJECTS = join(CWD, "objects");

    /* Current Head pointer. */
    static final File CURR_HEAD = join(CWD, "head");

    /* Recording all branches, remotes, and tags. */
    static final File REFS = join(CWD, "refs");

    /* Recording Hashmap of filename and index (sha1) of all files. */
    static final File INDEX = join(CWD, "index");

    /* Recording Hashmap of filename and index (sha1) of all files
     * to be removed. */
    static final File INDEX_REMOVE = join(CWD, "index-remove");

    /* Recording all branches. */
    static final File BRANCHES = join(REFS, "heads");

}
