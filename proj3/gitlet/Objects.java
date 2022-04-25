package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import static gitlet.Utils.*;
import static gitlet.Files.*;

public class Objects implements Serializable {

    /* For initialization.
     * Time format: Thu Nov 9 20:00:05 2017 -0800
     */
    Objects() {
        type = "commit";
        Date date = new Date(0);
        //Because my computer system is Chinese.
        //Also, I did not get how to use the Format function,
        //thus I self learned SimpleDateFormat online.
        timestamp = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z",
                new Locale("en")).format(date);
        msg = "initial commit";
        index = new HashMap<>();
        parent = new LinkedList<>();
    }


    Objects(String name) {
        if (name.equals("index")) {
            index = new HashMap<>();
        }
    }


    Objects(String content, String file) {
        type = "blob";
        this.content = content;
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

    public void makeCommit(String msg) {
        type = "commit";
        Date date = new Date();
        //Same cause.
        timestamp = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z",
                new Locale("en")).format(date);
        parent = new LinkedList<>();
        parent.add(getCurrHead());
        //same variable name problem...
        this.msg = msg;
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

    private String type;
    private String timestamp;
    private String content;
    private String msg;
    private String fileName;
    private LinkedList<String> parent;

    /*  */
    //It turns out it have to be public because we need to read and insert
    //elements in other classes.
    public HashMap<String, Index> index;
}
