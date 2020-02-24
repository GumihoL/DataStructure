/** Implement a double ended queue by circular double linked list. */
public class LinkedListDeque<Type> {
    private class StuffNode{
        public Type item;
        public StuffNode next;
        public StuffNode prev;
        /** Constructor for StuffNode*/
        public StuffNode(Type item, StuffNode prev, StuffNode next){
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    private StuffNode first;
    private StuffNode last;
    private int size;
    private StuffNode sentinel; // We need only one sentinel for circular list.

    /** Constructor */
    public LinkedListDeque(Type item){
        sentinel = new StuffNode(null, null, null); // item of sentinel has no business
        first = new StuffNode(item, sentinel, sentinel);
        last = first;
        sentinel.prev = this.last;
        sentinel.next = this.first;
        size = 1;
    }

    /** Construct an empty deque. */
    public LinkedListDeque(){
        sentinel = new StuffNode(null,null,null);
        size = 0;
        first = sentinel;
        last = first;
    }

    /** Construct a new deque from an existed deque. The same as deep copy */
    public LinkedListDeque(LinkedListDeque other){
        sentinel = new StuffNode(null, null, null);
        size = 0;
        first = sentinel;
        last = first;
        StuffNode iterator = other.first;
        for (int i = 0; i < other.size; i++){
            this.addLast(iterator.item);
            iterator = iterator.next;
        }
    }
    /** Adds an item of Type to the front of deque. */
    public void addFirst(Type item){
        StuffNode newFirst = new StuffNode(item, sentinel, first);
        first.prev = newFirst;
        first = newFirst;
        size += 1;
    }

    /** Adds an item of Type to the back of deque. */
    public void addLast(Type item){
        StuffNode newLast = new StuffNode(item, last, sentinel);
        last.next = newLast;
        last = newLast;
        size += 1;
    }

    /** Return True if the list is empty. */
    public boolean isEmpty(){
        if (size == 0){
            return true;
        }
        return false;
    }

    /** Returns the number of items in this deque. */
    public int size(){
        return this.size;
    }

    /** Prints the items in the deque from first to
     * last, separated by a space. Once all the items
     * have been printed, print out a new line.
     */
    public void printDeque(){
        StuffNode iterator = first;
        for (int i = 0; i < this.size(); i++) {

            System.out.print(iterator.item);
            System.out.print(' ');
            iterator = iterator.next;
        }
        System.out.println();
    }

    /** Removes and returns the item at the front of the
     * deque. If no such item exists, returns null.
     * @return : the item of the first node.
     */
    public Type removeFirst(){
        if (size == 0){
            return null;
        }
        Type toBeReturned = first.item;
        first = first.next;
        first.prev = sentinel;
        size -= 1;
        return toBeReturned;
    }

    /** Removes and returns the item at the back of
     * the deque. If no such item exists, return null.
     * @return : the item of the last node.
     */
    public Type removeLast(){
        if(size == 0){
            return null;
        }
        Type toBeReturned = last.item;
        last = last.prev;
        last.next = sentinel;
        return toBeReturned;
    }

    /** Gets the item at the given index, where 0 is the
     * front item, 1 is the next, and so forth. If no
     * such item exists, return null.
     * @return : the item at the given index.
     */
    public Type get(int index){
        if (index < 0 || index > this.size - 1){
            return null;
        }
        StuffNode iterator = first;
        for (int i = 0; i != index; i++){
            if (i != index){
                iterator = iterator.next;
            }
        }
        return iterator.item;
    }
    private Type getRecursive_help(int index, StuffNode iterator){
        if (index == 0){
            return iterator.item;
        }
        return getRecursive_help(index - 1,iterator.next);
    }

    /** The same as get(), but by recursive. */
    public Type getRecursive(int index){
        if (index < 0 || index > this.size - 1){
            return null;
        }
        return getRecursive_help(index, first);
    }
}
