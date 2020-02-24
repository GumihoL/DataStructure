import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;

public class MyHashMap<K, V> implements Map61B<K,V>{

    private BucketEntity<K, V>[] buckets; // capacity = buckets.length;
    private static final int INITIAL_CAPACITY = 7; // default initial capacity
    private static final double LOAD_FACTOR = 0.75;
    private int threshold; // Once size over this threshold, should expand.
    private int size;

    public class BucketEntity<K, V>{
        private K key;
        private V value;
        BucketEntity<K, V> next;
        int hashCode;

        private BucketEntity(K k, V v){
            key = k;
            value = v;
            next = null;
            hashCode = k.hashCode();
        }

        private BucketEntity(){}

        private void addToEnd(K k, V v){
            // Assume this is not null
            BucketEntity b = this;
            while(b.next != null){
                b = b.next;
            }
            b.next = new BucketEntity(k, v);
        }
    }

    /**
     * Constructors
     */
    public MyHashMap(){
        buckets = new BucketEntity[INITIAL_CAPACITY];
        threshold = (int)(INITIAL_CAPACITY * LOAD_FACTOR);
        size = 0;
    }

    public MyHashMap(int initialCapacity){
        buckets = (BucketEntity[]) new Object[initialCapacity];
        threshold = (int)(initialCapacity * LOAD_FACTOR);
        size = 0;
    }

    public MyHashMap(int initialCapacity, double load_factor){
        buckets = (BucketEntity[]) new Object[initialCapacity];
        threshold = (int)(initialCapacity * load_factor);
        size = 0;
    }

    // hash key to index of buckets.
    private int hash(K key, int bucketsLength){
        return (key.hashCode() & 0x7fffffff) % bucketsLength;
    }
    /** Removes all of the mappings from this map. */
    @Override
    public void clear(){
        buckets = new BucketEntity[buckets.length];
        size = 0;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key){
        return buckets[hash(key, buckets.length)] != null;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key){
        if (key == null) {
            throw new NullPointerException("key is a Null pointer in get(K key)");
        }
        BucketEntity item = buckets[hash(key, buckets.length)];
        while(item != null && !item.key.equals(key)){
            item = item.next;
        }
        if (item == null) return null;
        return (V) item.value;
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size(){return size;}

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value){
        if (key == null){
            throw new NullPointerException("Key is a null pointer in put(K key, V value)");
        }
        BucketEntity b = buckets[hash(key, buckets.length)];
        if (b != null){
            while (!b.key.equals(key) && b.next != null){
                b = b.next;
            }
        }else   {
            buckets[hash(key, buckets.length)] = new BucketEntity(key, value);
            size++;
            if (size > threshold) resize(2 * buckets.length);
            return;
        }
        if (b.key == key){ b.value = value; }
        else{
            b.next = new BucketEntity(key, value);
            size++;
        }
        // After put item in, check whether hash table need to be resized.
        if(size > threshold){
            resize(2 * buckets.length);
        }
    }
    private void resize(int newCapacity){
        BucketEntity<K, V>[] newBuckets = new BucketEntity[newCapacity];
        for(int i = 0; i < buckets.length; i++){
            BucketEntity<K, V> b = buckets[i];
            while(b != null){
                int hashCode = hash(b.key, newCapacity);
                if (newBuckets[hashCode] == null){
                    newBuckets[hashCode] = new BucketEntity<>(b.key, b.value);
                } else  {
                    newBuckets[hashCode].addToEnd(b.key, b.value);
                }
                b = b.next;
            }
        }
        buckets = newBuckets;
        threshold = (int)(buckets.length * LOAD_FACTOR);
    }

    /** Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet(){
        Set<K> set = new HashSet<K>();
        for(int i = 0; i < buckets.length; i++){
            BucketEntity b = buckets[i];
            while (b != null){
                set.add((K) b.key);
                b = b.next;
            }
        }
        return set;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public V remove(K key){
        if(key == null){throw new IllegalArgumentException("key is null in remove(K key)");}
        if(!containsKey(key)) {return null;}
        V toBeReturned = get(key);
        int hashCode = hash(key, buckets.length);
        buckets[hashCode] = remove(key, buckets[hashCode]);
        return toBeReturned;
    }

    private BucketEntity remove(K key, BucketEntity b){
         if (b == null) return null;
         if (b.key.equals(key)) {
             size--;
             return b.next;
         }
         return remove(key, b.next);
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    @Override
    public V remove(K key, V value){
        if (!containsKey(key)) return null;
        V toBeReTurned = get(key);
        if (!toBeReTurned.equals(value)) return null;
        return remove(key);
    }

    /** Just use the iterator of java.util.HashSet.*/
    @Override
    public Iterator iterator(){return new HashMapIterator();}

    private class HashMapIterator implements Iterator{
        BucketEntity curEntity;
        int count; // <= size
        int index; // The index of buckets where contains curEntity

        HashMapIterator(){
            if (size > 0){
                for (int i = 0; i < buckets.length && curEntity == null; i++){
                    if (buckets[i] != null) {
                        curEntity = buckets[i];
                        index = i;
                        count = 1;
                    }
                }
            }else {
                count = 0;
                curEntity = null;
            }
        }

        public boolean hasNext(){ return count < size; }

        // Assume hasNext() is true before next() is called.
        public BucketEntity next(){
            if (curEntity.next != null){
                curEntity = curEntity.next;
            }else {
                index++;
                int i = index;
                for (; i < buckets.length && buckets[i] == null; i++);
                curEntity = buckets[i];
                index = i;
            }
            count++;
            return curEntity;
        }

    }
}
