import javax.naming.PartialResultException;
import java.nio.file.FileAlreadyExistsException;
import java.security.DrbgParameters;

/** Implement a double ended queue by circular array. */
public class ArrayDeque<T> {
    private int size;
    private T[] items;
    private int capacity;
    private int first; // Front index of deque, items[first] is the 1st item logically.
    private int last;  // Back index of deque, items[last] is the last item logically.
                       // index based on 0.
    private final static int originalCapacity = 10;

    /** Constructor from an item of type T*/
    public ArrayDeque(T item){
        capacity = originalCapacity;
        items = (T[])new Object[capacity]; // Create an object array then cast
        last = capacity / 2;
        first = last;
        items[first] = item;
        size = 1;
    }

    public ArrayDeque(){
        capacity = originalCapacity;
        items = (T[]) new Object[capacity];
        last = capacity / 2;
        first = last + 1;
        items[first] = null;
        size = 0;
    }

    public ArrayDeque(ArrayDeque other){
        this.capacity = other.capacity;
        this.items = (T[]) new Object[capacity];
        this.first = other.first;
        this.last = other.last;
        for (int i = 0; i < size; i++){
            items[(this.first + i) % this.capacity] = (T)other.items[(other.first) % other.capacity];
        }
        size = other.size;
    }

    /** Expand the internal array */
    private void expand(){
        int factor = 2;
        int newCapacity = factor * capacity;
        T[] temp_items = (T[]) new Object[capacity];
        for (int i = 0; i < size; i++){
            temp_items[i] = items[(first + i) % capacity];
        }
        T[] new_items = (T[]) new Object[newCapacity];
        // It is the only method implementing array deep copy I currently know.
        System.arraycopy(temp_items,0,new_items,newCapacity/4,size);
        items = new_items;
        first = newCapacity/4;
        last = first + size - 1;
        capacity = newCapacity;
    }
    /** Shrink the internal array. */
    public void shrink(){
        int factor = 2;
        int newCapacity = capacity / factor;
        T[] temp_items = (T[]) new Object[capacity];
        for (int i = 0; i < size; i++){
            temp_items[i] = items[(first + i) % capacity];
        }
        T[] newItems = (T[]) new Object[newCapacity];
        System.arraycopy(temp_items,0,newItems,newCapacity/(factor * 2),size);
        capacity = newCapacity;
        first = capacity/(factor * 2);
        last = first + size - 1;
        items = newItems;
    }

    /** Add an item to the front of deque, check whether should expand. */

    public void addFirst(T item){ // Change first then put into item.
        if (size >= capacity) {
            this.expand();
        }
        if (first == 0){
            first = capacity - 1;
        }
        else{
            first -= 1;
        }
        items[first] = item;
        size +=1 ;
    }

    /** Add an item to the back of deque, check whether should expand. */
    public void addLast(T item){
        if (size >= capacity){
            this.expand();
        }
        if (last == capacity - 1){
            last = 0;
        }
        else{
            last -= 1;
        }
        items[last] = item;
        size += 1;
    }

    /** Get the ith item in constant time. */
    public T get(int index){
        // warning This method always work whatever index is.
        return items[(first + index) % capacity];
    }

    /** Remove the item at the front. check whether empty or should shrink. */
    public T removeFirst(){
        if (size == 0){
            return null;
        }
        T toBeReturned = items[first];
        first = (first + 1) % capacity;
        size -= 1;
        if (size < 1/10 * capacity){
            this.shrink();
        }
        return toBeReturned;
    }

    /** Remove the item at the back, check whether empty or should shrink . */
    public T removeLast(){
        if (size == 0){
            return null;
        }
        T toBeReturned = items[last];
        last = (last - 1) % capacity;
        size -= 1;
        if (size < 1/10 * capacity){
            this.shrink();
        }
        return toBeReturned;
    }

    /** Return True if deque is empty. */
    public boolean isEmpty(){
        if (size == 0){
            return true;
        }
        return false;
    }

    /** Return the number of items in the deque. */
    public int size(){
        return size;
    }
}
