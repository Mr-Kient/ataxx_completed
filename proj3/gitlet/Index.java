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

    /* Return the hashcode of the String filename. */
    @Override
    public int hashCode() {
        return filename.hashCode();
    }

    public boolean verCompare(Index other) {
        return sha1.equals(other.sha1);
    }

    /* The file name. */
    private final String filename;

    /* The Sha1 Hashcode. */
    private final String sha1;
}
