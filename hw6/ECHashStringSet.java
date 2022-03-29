import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author Darren Wang
 */
class ECHashStringSet implements StringSet {

    public ECHashStringSet() {
        size = 0;
        ecNum = 5;
        externalChain = (LinkedList<String>[]) new LinkedList[ecNum];
    }

    @Override
    public void put(String s) {
        if ((double) size / (double) ecNum > 5) {
            resize();
        }
        int index = s.hashCode() % ecNum;
        if (index < 0) {
            index = index & 0x7fffffff % ecNum;
        }
        while (index > ecNum) {
            resize();
        }
        if (externalChain[index] == null) {
            externalChain[index] = new LinkedList<>();
        }
        externalChain[index].add(s);
        size++;
    }

    @Override
    public boolean contains(String s) {
        int index = s.hashCode() % ecNum;
        if (index < 0) {
            index = index & 0x7fffffff % ecNum;
        }
        return externalChain[index] != null && externalChain[index].contains(s);
    }

    @Override
    public List<String> asList() {
        List<String> List = new ArrayList<>();
        for (LinkedList<String> all : externalChain) {
            if (all != null) {
                List.addAll(all);
            }
        }
        return List;
    }

    public void resize() {
        LinkedList<String>[] original = externalChain;
        size = 0;
        ecNum *= 2;
        externalChain = (LinkedList<String>[]) new LinkedList[ecNum];
        for (LinkedList<String> all : original) {
            if (all != null) {
                for (String s : all) {
                    this.put(s);
                }
            }
        }
    }

    private LinkedList<String>[] externalChain;

    private int ecNum;

    private int size;
}
