package bearmaps.lab9;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Set;

public class MyTrieSet<Value> implements TrieSet61B {

    private Node root;

    private class Node{
        public Character c;
        public HashMap<Character, Node> children;
        public boolean isKey;
        public Value value;

        Node(Character c, boolean isKey, Value value){
            this.c = c;
            children = new HashMap<>();
            this.isKey = isKey;
            this.value = value;
        }
    }

    public MyTrieSet(){
        root = new Node(null, false, null);
    }

    /** Clears all items out of Trie */
    @Override
    public void clear(){
        root = null;
    }

    /** Returns true if the Trie contains KEY, false otherwise */
    @Override
    public boolean contains(String key){
        if (key == null || root == null) return false;
        Node n = root;
        for (int i = 0; i < key.length(); i++){
            if(n.children.get(key.charAt(i)) == null){
                return false;
            }
            n = n.children.get(key.charAt(i));
        }
        if(!n.isKey) return false;
        return true;
    }

    /** Inserts string KEY into Trie */
    @Override
    public void add(String key){
        if (key == null) throw new IllegalArgumentException(" key is a null pointer in add()"   );
        Node n = root;
        for (int i = 0; i < key.length(); i++){
            char ch = key.charAt(i);
            if (!n.children.containsKey(ch)){
                n.children.put(ch, new Node(ch, false, null));
            }
            n = n.children.get(ch);

        }
        n.isKey = true;
    }

    /** Returns a list of all words that start with PREFIX */
    @Override
    public List<String> keysWithPrefix(String prefix){
        if (prefix == null) throw new IllegalArgumentException(" prefix is a null pointer in keysWithPrefix");
        Node n = root;
        List<String> forReturn = new ArrayList<>();
        for (int i = 0; i < prefix.length(); i++){
            char ch = prefix.charAt(i);
            if (!n.children.containsKey(ch)) return null;
            n = n.children.get(ch);
        }
        Set s = n.children.keySet();
        for (Object ch : s){
            ch = (Character)ch;
            keysWithPrefix(prefix + ch, n.children.get(ch), forReturn);
        }
        return forReturn;
    }

    private void keysWithPrefix(String prefix, Node n, List<String> list){
        if (n.isKey){
            list.add(prefix);
        }
        Set s = n.children.keySet();
        if (s != null) {
            for (Object ch : s) {
                ch = (Character) ch;
                keysWithPrefix(prefix + ch, n.children.get(ch), list);
            }
        }

    }
    /** Returns the longest prefix of KEY that exists in the Trie
     * Not required for Lab 9. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public String longestPrefixOf(String key){
        String longestPrefix = "";
        Node n = root;
        for (int i = 0; i < key.length() && n.children.containsKey(key.charAt(i)); i++){
            Character ch = key.charAt(i);
            longestPrefix += ch;
            n = n.children.get(ch);
        }
        return longestPrefix;
    }
}
