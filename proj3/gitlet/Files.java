package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static gitlet.Utils.*;
import static gitlet.Objects.*;
import static gitlet.Index.*;

public class Files {

    /* Initialize working directory. */
    static void initDir() {
        CWD.mkdirs();
        OBJECTS.mkdirs();
        BRANCHES.mkdirs();
        Objects fileList = new Objects("index");
        Utils.writeObject(INDEX, fileList);
        Objects fileListRm = new Objects("index");
        Utils.writeObject(INDEX_REMOVE, fileListRm);
        writeContents(CURR_HEAD, "master");
    }

    /* Write Objects in the OBJECTS directory.
     * Save by the first 2 letters of sha1 (same as Git).
     * @return sha1 of the object.
     */
    static String writeObject(Objects Object) {
        String sha1 = sha1(Object);
        join(OBJECTS, getHeadHash(sha1)).mkdir();
        File file = getObjectsFile(sha1);

        if (Object.getType().equals("commit")) {
            String currBranch
                    = Utils.readContentsAsString(CURR_HEAD);
            writeHead(currBranch);
            updateBranchHead(currBranch, sha1);
            Utils.writeObject(file, Object);
        } else {
            if (writeStagedToIndex(sha1, Object.getFileName())) {
                Utils.writeObject(file, Object);
            }
        }
        return sha1;
    }

    /* Write / update the current HEAD. */
    static void writeHead(String branch) {
        writeContents(CURR_HEAD, branch);
    }

    static boolean writeStagedToIndex(String sha1, String filename) {
        Objects fileList;
        fileList = readObject(INDEX, Objects.class);
        /* If already exists, we do not have to rewrite it. */
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

    /* Record the sha1 code of the very branch. */
    static void updateBranchHead(String branch, String sha1) {
        writeContents(join(BRANCHES, branch), sha1);
    }

    static void updateRepoFile(File cwdNew, String blobHash) {
        File repo = getObjectsFile(blobHash);
        String content = Utils.readObject(repo, Objects.class).getContent();
        Utils.writeContents(cwdNew, content);
    }


    /* The strategy to save files in this system is to use its sha1 code.
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

    static Objects getObjectsHash(String hash) {
        File obj = getObjectsFile(hash);
        return readObject(obj, Objects.class);
    }



    static String getCurrHead() {
        String curr = readContentsAsString(CURR_HEAD);
        return getHeadGeneral(curr);
    }

    static Objects getCurrCommit() {
        File commit = getObjectsFile(getCurrHead());
        return readObject(commit, Objects.class);
    }

    static String getHeadGeneral(String location) {
        File branchHead = join(BRANCHES, location);
        return readContentsAsString(branchHead);
    }

    static Objects getCommitGeneral(String commitLoc) {
        File commit = getObjectsFile(getHeadGeneral(commitLoc));
        return readObject(commit, Objects.class);
    }

    static List<String> pastCommits(String currBranch) {
        String currHash = readContentsAsString(join(BRANCHES, currBranch));
        List<String> pastCommits = new ArrayList<>();
        String commitHash = currHash;
        while (!commitHash.equals("")) {
            pastCommits.add(commitHash);
            Objects currCommit = getObjectsHash(commitHash);
            commitHash = currCommit.getParent();
        }
        return pastCommits;
    }






    /* Current working directory. */
    static final File CWD = new File(".gitlet");

    /* Logs of Gitlet. */
    static final File LOGS = join(CWD, "logs");

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
