
import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
    /** Internal private Node class. */
    private class Node{
        K key;
        V value;
        int N;  // size of the subtree rooted at this node.
        Node left;
        Node right;

        Node(K k, V v, int n){
            key = k;
            value = v;
            N = n;
        }
    }
    /** Instance variable. */
    private Node root;

    public BSTMap(){} // Construct an empty map.

    /** Removes all of the mappings from this map. */
    @Override
    public void clear(){ root = null;}

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K k){ return get(k) != null; }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K k){ return get(root, k);}

    private V get(Node x, K k){
        if (x == null) return null;
        int cmp = k.compareTo(x.key);
        if (cmp < 0) return get(x.left, k);
        if (cmp > 0) return get(x.right, k);
        return x.value;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size(){return size(root);}

    private int size(Node x){
        if (x == null) return 0;
        return x.N;
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K k, V v){
        root = put(root, k, v);
    }

    private Node put(Node x, K k, V v){
        if (x == null) return new Node(k, v, 1);
        int cmp = k.compareTo(x.key);
        if (cmp == 0) x.value = v;
        if (cmp < 0) x.left = put(x.left, k, v);    //*****
        if (cmp > 0) x.right = put(x.right, k, v);     //*******
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet(){
        Set<K> BSTSet = new HashSet<>();
        for (int i = 0; i < size(); i += 1) {
            BSTSet.add(select(i));
        }
        return BSTSet;
        }

    /** returns the smallest key in this BST. If the BST is empty, return null*/
    public K min(){
        if (root == null) return null;
        return min(root).key;
    }

    // Node is recursive structure, but key not.
    private Node min(Node x){
         if (x.left == null) return x;
         return min(x.left);
    }

    /** delete the item with smallest key. */
    public void deleteMin(){
        root = deleteMin(root);
    }

    private Node deleteMin(Node x){
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }
    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K k){
        if(!containsKey(k)) return null;
        V toRemove = get(k);
        root = remove(root, k); //********
        return toRemove;
    }

    private Node remove(Node x, K k){
        if (x == null) return null;
        int cmp = k.compareTo(x.key);
        if (cmp < 0) x.left = remove(x.left, k);    //******
        else if (cmp > 0) x.right = remove(x.right, k);     //*******
        else{
            if (x.left == null) return x.right;
            if (x.right == null) return  x.left;
            Node t = x;
            x = min(x.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K k, V v){
        if (!containsKey(k)) return null;
        if (get(k) != v) return null;
        return remove(k);
    }

    public void printInOrder(){
        printInOrder(root);
    }

    private void printInOrder(Node x){
        if (x == null) return;
        printInOrder(x.left);
        System.out.println(x.key + ": " + x.value);
        printInOrder(x.right);
    }

    /** Returns the key of the node at rank k. If k < 0 or k >= root.size, returns null.*/
    public K select(int k){
        if (k < 0 || k >= size(root)){
            return null;
        }
        return select(root, k).key;
    }
    /** Returns the node at rank k, rank starts from 0. */
    private Node select(Node x, int k){
        if (k == size(x.left)){ return x;}
        else if (k < size(x.left)) {return select(x.left, k);}
        else {return select(x.right, k - (size(x.left) + 1));}
    }
    @Override
    public Iterator<K> iterator(){ return new BSTIterator();}

    private class BSTIterator implements Iterator{
        private int curRank;
        private K curKey;

        BSTIterator(){
            curKey = select(0);
            curRank = 0;
        }

        public boolean hasNext(){return curRank + 1 <= size(root);}
        public K next(){
            K temp = curKey;
            curRank += 1;
            curKey = select(curRank);
            return temp;
        }
    }
}