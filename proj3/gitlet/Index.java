package gitlet;

import java.io.Serializable;

public class Index implements Serializable {

    public Index(String sha1Code, String fileName) {
        filename = fileName;
        sha1 = sha1Code;
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

    /* To detect if the Sha1 hashcodes are the same for this instance
     * and the given Object. */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Index) {
            return (((Index) obj).filename.hashCode()
                    == this.filename.hashCode());
        }
        return false;
    }

    public boolean verCompare(Index other) {
        return sha1.equals(other.sha1);
    }

    /** The file name. */
    private final String filename;

    /** The Sha1 Hashcode. */
    private final String sha1;
}
