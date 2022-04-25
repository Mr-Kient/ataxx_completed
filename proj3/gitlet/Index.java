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


    /* To detect if the Sha1 hashcodes are the same for this instance
     * and the given Object. */
    @Override
    public boolean equals(Object obj) {
        //this one is so hard i do not think we learned how to use instanceof.
        //so i have to learn it from others.
        if (obj instanceof Index){
            return (((Index) obj).filename.hashCode() == this.filename.hashCode());
        }
        return false;
    }

    public boolean verCompare(Index other) {
        return sha1.equals(other.sha1);
    }

    /* The file name. */
    private final String filename;

    /* The Sha1 Hashcode. */
    private final String sha1;
}
