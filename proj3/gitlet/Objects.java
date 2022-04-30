package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import static gitlet.Files.*;

public class Objects implements Serializable {

    /* For initialization.
     * Time format: Thu Nov 9 20:00:05 2017 -0800
     */
    Objects() {
        type = "commit";
        Date date = new Date(0);

        timestamp = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z",
                new Locale("en")).format(date);
        msg = "initial commit";
        index = new HashMap<>();
        parent = new LinkedList<>();
    }

    /* For storing the list of files waiting for be staged. */
    Objects(String stage) {
        if (stage.equals("stage")) {
            index = new HashMap<>();
        }
    }

    /* Create a blob. */
    Objects(String providedContent, String file) {
        type = "blob";
        content = providedContent;
        fileName = file;
    }

    public void updateIndex(Objects stagedFiles, Objects stageRemove) {
        if (!stagedFiles.index.isEmpty()) {
            for (String file : stagedFiles.index.keySet()) {
                index.put(file, stagedFiles.index.get(file));
            }
        }
        if (!stageRemove.index.isEmpty()) {
            for (String file : stageRemove.index.keySet()) {
                index.remove(file);
            }
        }
    }

    public void makeCommit(String message) {
        type = "commit";
        Date date = new Date();

        timestamp = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z",
                new Locale("en")).format(date);
        parent = new LinkedList<>();
        parent.add(getCurrHead());

        msg = message;
    }

    String getType() {
        return type;
    }

    String getContent() {
        return content;
    }

    String getTimestamp() {
        return timestamp;
    }

    String getMsg() {
        return msg;
    }

    String getFileName() {
        return fileName;
    }

    String getParent() {
        if (parent.isEmpty()) {
            return "";
        }
        return parent.getFirst();
    }

    /* Type of the Objects, can be Commit, Blob, or Stage. */
    private String type;

    /* Timestamp of the Objects. */
    private String timestamp;

    /* Content of the Objects. For Blobs. */
    private String content;

    /* Message of the Objects. For Commits. */
    private String msg;

    /* File name of the Objects. For Blobs. */
    private String fileName;

    /* Parent LinkedList of the Objects.
     * To get all past commits of its branch. */
    private LinkedList<String> parent;

    /* Index of the Objects. For files waiting for staging. */
    public HashMap<String, Index> index;
}
