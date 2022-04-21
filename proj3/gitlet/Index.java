package gitlet;

import java.io.Serializable;

public class Index implements Serializable {

    public Index(String sha1, String filename) {
        this.filename = filename;
        this.sha1 = sha1;
    }

    public String getSha1() {
        return sha1;
    }

    public String getFileName() {
        return filename;
    }


    @Override
    public int hashCode() {
        return filename.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Index){
            return (((Index) obj).filename.hashCode() == this.filename.hashCode());
        }
        return false;
    }

    public boolean verCompare(Index other) {
        return sha1.equals(other.sha1);
    }

    private final String filename;
    private final String sha1;
}
