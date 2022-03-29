import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Darren Wang
 */
public class BSTStringSet implements StringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        if (_root == null) {
            _root = new Node(s);
        } else {
            Node curr = _root;
            while (true) {
                if (s.compareTo(curr.s) < 0) {
                    if (curr.left == null) {
                        curr.left = new Node(s);
                        break;
                    } else {
                        curr = curr.left;
                    }
                } else if (s.compareTo(curr.s) > 0) {
                    if (curr.right == null) {
                        curr.right = new Node(s);
                        break;
                    } else {
                        curr = curr.right;
                    }
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public boolean contains(String s) {
        Node curr = _root;
        while (curr != null) {
            if (s.compareTo(curr.s) < 0) {
                curr = curr.left;
            } else if (s.compareTo(curr.s) > 0) {
                curr = curr.right;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> asList() {
        List<String> all = new ArrayList<>();
        if (_root == null) {
            return all;
        }
        all.add(_root.s);
        Node rightAll = _root.right;
        while (rightAll != null) {
            all.add(rightAll.s);
            rightAll = rightAll.right;
        }
        Node leftAll = _root.left;
        while (leftAll != null) {
            all.add(0, leftAll.s);
            leftAll = leftAll.left;
        }
        return all;
    }

    /** Left of the Node n.
     * @param n current node. */
    public String Left(Node n) {
        if (n != null && n.left != null) {
            return n.left.s;
        }
        return "null";
    }

    /** Right of the Node n.
     * @param n current node. */
    public String Right(Node n) {
        if (n != null && n.right != null) {
            return n.right.s;
        }
        return "null";
    }

    /** Root String. */
    public String RootString() {
        if (_root != null) {
            return _root.s;
        }
        return "null";
    }

    /** Root. */
    public Node Root() {
        return _root;
    }

    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    public Iterator<String> iterator(String low, String high) {
        return new BSTBoundIterator(new BSTIterator(_root), low, high);
    }

    /** An iterator over BSTs with bounds. */
    private static class BSTBoundIterator implements Iterator<String> {
        public BSTBoundIterator(Iterator iterator, String low, String high) {
            _iterator = iterator;
            _low = low;
            _high = high;
        }

        public void setNext() {
            while (_iterator.hasNext()) {
                String next = (String) _iterator.next();
                if (next.compareTo(_low) >= 0 && next.compareTo(_high) <= 0) {
                    _next = next;
                    break;
                }
                _next = " ";
            }
        }

        @Override
        public boolean hasNext() {
            setNext();
            return !_next.equals(" ");
        }

        @Override
        public String next() {
            return _next;
        }

        private String _low;
        private String _high;
        private Iterator _iterator;
        private String _next = " ";
    }

    /** Root node of the tree. */
    private Node _root;
}
