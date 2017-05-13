package tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Binary Search Tree
 */
public class BinarySearchTree<K extends Comparable<K>, V> {

    private class Node {
        V value;
        K key;
        Node left;
        Node right;
        Node parent;
        boolean isLeftChild;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public void setLeft(Node node) {
            left = node;
            if (left != null) {
                left.parent = this;
                left.isLeftChild = true;
            }
        }

        public void setRight(Node node) {
            right = node;
            if (right != null) {
                right.parent = this;
                right.isLeftChild = false;
            }
        }

        @Override
        public String toString() {
            return "val=" + value
                    + " parent=" + (parent == null ? null : parent.value)
                    + " left=" + (left == null ? null : left.value)
                    + " right=" + (right == null ? null : right.value);
        }
    }

    /**
     * Root node of tree
     */
    private Node root;

    private int size;

    public int size() {
        return size;
    }

    private Node lookup(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node node = root;
        while (node != null) {
            if (node.key.equals(key)) {
                return node;
            } else if (node.key.compareTo(key) > 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return null;
    }

    public boolean contains(K key) {
        return lookup(key) != null;
    }

    public V get(K key) {
        Node node = lookup(key);
        return node == null ? null : node.value;
    }

    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return;
        }
        Node node = root;

        while (true) {
            if (node.key.equals(key)) {
                // Overwrite it
                node.value = value;
                return;
            } else if (node.key.compareTo(key) > 0) {
                if (node.left == null) {
                    node.setLeft(new Node(key, value));
                    ++size;
                    return;
                }
                node = node.left;
            } else {
                if (node.right == null) {
                    node.setRight(new Node(key, value));
                    ++size;
                    return;
                }
                node = node.right;
            }
        }
    }

    public V delete(K key) {
        Node node = lookup(key);
        if (node == null) {
            return null;
        }
        if (node.left == null || node.right == null) {
            // Node is leaf or only one child
            Node child = node.left == null ? node.right : node.left;
            setNode(node, child);
            --size;
            return node.value;
        }
        // Find the maximum node in left child tree
        Node maxLeft = node.left;
        while (maxLeft.right != null) {
            maxLeft = maxLeft.right;
        }

        // Delete successor
        setNode(maxLeft, maxLeft.left);
        --size;
        setNode(node, maxLeft);
        return node.value;
    }

    private void setNode(Node node, Node n) {
        if (node == root) {
            if ((root = n) != null) {
                root.parent = null;
            }
        } else {
            Node p = node.parent;
            if (node.isLeftChild) {
                p.setLeft(n);
            } else {
                p.setRight(n);
            }
        }
        if (node.left != null && node.left != n) {
            node.left.parent = n;
            if (n != null) {
                n.left = node.left;
            }
        }
        if (node.right != null && node.right != n) {
            node.right.parent = n;
            if (n != null) {
                n.right = node.right;
            }
        }
    }

    private void debug(Node node) {
        if (node == null) return;
        System.out.println(node.toString());
        debug(node.left);
        debug(node.right);
    }


    public static void main(String[] args) {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<>();
        List<Integer> nums = new ArrayList<>();

        int numCount = 100;

        for (int i = 0; i < numCount; i++) {
            nums.add(i);
        }
        Collections.shuffle(nums);
        System.out.println("Insert start");

        for (int n : nums) {
            tree.put(n, n);
            System.out.println("Tree contains " + n + " " + tree.contains(n));
            System.out.println("Tree lookup " + n + "=" + tree.get(n));
        }
        tree.debug(tree.root);

        System.out.println("Tree size=" + tree.size());
        for (int n : nums) {
            System.out.println("Deleting " + n);
            Integer node = tree.delete(n);
            if (node == null) {
                System.out.println("Deleting " + n + " failed. Something is wrong.");
                break;
            }
            System.out.println("Deleted " + node);
            tree.debug(tree.root);
        }
        System.out.println(tree.size());
    }
}
