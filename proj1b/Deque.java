public interface Deque<Type> {
    public void addFirst(Type item);
    public void addLast(Type item);
    default boolean isEmpty(){
        if (size() == 0){
            return true;
        }
        return  false;
    }
    public int size();
    public void printDeque();
    public Type removeFirst();
    public Type removeLast();
    public Type get(int index);
}
