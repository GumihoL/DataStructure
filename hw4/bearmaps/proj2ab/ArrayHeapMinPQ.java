package bearmaps.proj2ab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T>{

    private class PriorityNode implements Comparable<PriorityNode>{
        private T item;
        private Double priority;
        private int index;  // The index of this node in ArrayHeapMinPQ.

        PriorityNode(T item, Double priority, int index){
            this.item = item;
            this.priority = priority;
            this.index = index;
        }

        @Override
        public int compareTo(PriorityNode other){
            return Double.compare(this.priority, other.priority);
        }
    }

    private ArrayList<PriorityNode> nodesArray; // 3b implementation. position 0 is null;
    private int size;                           // nodesArray所拥有的结点个数，不计入第0个结点（哨兵）。
    private HashMap<T,Integer> itemHashMap; // 存放item和该item所在的结点在nodesArray中的索引。

    /**
     * return the index of left child.
     */
    private int left(int index){ return 2 * index; }

    /**
     * return the index of right child.
     */
    private int right(int index){return 2 * index + 1;}

    /**
     * return the index of parent.
     */
    private int parent(int index){return  index/2;}

    /**
     * return the left child node, if no left child return null;
     */
    private PriorityNode getLeft(PriorityNode node){
        int leftIndex = left(node.index);
        if (leftIndex > size) return null;
        return nodesArray.get(leftIndex);
    }

    /**
     * return the right child node, if no right child return null;
     */
    private PriorityNode getRight(PriorityNode node){
        int rightIndex = right(node.index);
        if (rightIndex > size) return null;
        return nodesArray.get(rightIndex);
    }

    /**
     * return the parent node, if no parent node return null;
     */
    private PriorityNode getParent(PriorityNode node){
        int parentIndex = parent(node.index);
        if (parentIndex < 1) return null;
        return nodesArray.get(parentIndex);
    }

    public ArrayHeapMinPQ(){
        nodesArray = new ArrayList<PriorityNode>();
        nodesArray.add(new PriorityNode(null, null,0));
        itemHashMap = new HashMap<>();
        size = 0;
    }

    // 交换两个结点的内容，并且交换nodesArray中指向node1和node2的指针，修改itemHashMap中item对应的索引值。
    private void swap(PriorityNode node1, PriorityNode node2){
        if (node1 == node2) return;
        int tempIndex = node1.index;
        node1.index = node2.index;
        node2.index = tempIndex;
        nodesArray.set(node1.index, node1);
        nodesArray.set(node2.index, node2);
        itemHashMap.put(node1.item, node1.index);
        itemHashMap.put(node2.item, node2.index);
    }

    /* Adds an item with the given priority value. Throws an
     * IllegalArgumentExceptionb if item is already present.
     * You may assume that item is never null. */
    @Override
    public void add(T item, double priority){
        if (contains(item)) throw new IllegalArgumentException("Item is already present.");
        size++;
        nodesArray.add( new PriorityNode(item, priority, size) );
        int index = size;
        PriorityNode addedNode = nodesArray.get(index);
        PriorityNode parentNode = getParent(addedNode);
        if (parentNode != null) {int cmp = addedNode.compareTo(parentNode);}
        while(parentNode != null && addedNode.compareTo(parentNode) < 0){
            swap(addedNode, parentNode);
            parentNode = getParent(addedNode);
        }
        itemHashMap.put(item, addedNode.index);
    }

    /* Returns true if the PQ contains the given item. */
    @Override
    public boolean contains(T item){
        return itemHashMap.get(item) != null;
    }
    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */

    @Override
    public T getSmallest(){
        if (size == 0) throw new NoSuchElementException("PQ is empty");
        return nodesArray.get(1).item;
    }

    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty.
    * @Override */
    public T removeSmallest() {
        if (size < 1) throw new NoSuchElementException("PQ is empty");
        T toBeReturned = nodesArray.get(1).item;
        itemHashMap.remove(toBeReturned);
        swap(nodesArray.get(1), nodesArray.get(size));
        nodesArray.set(size, null);
        size--;
        // 只有根节点及其子结点可能违反heap的性质，故只需将根结点向下交换。
        downSwap(nodesArray.get(1));
        return toBeReturned;
    }

    /* Returns the number of items in the PQ. */
    @Override
    public int size(){ return size;}

    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */
    @Override
    public void changePriority(T item, double priority){
        if (itemHashMap.get(item) == null) throw new NoSuchElementException("This item does not exist");
        int index = itemHashMap.get(item);
        PriorityNode node = nodesArray.get(index);
        node.priority = priority;
        downSwap(node);
        UpSwap(node);
    }

    // 假设传入的node可能大于其子结点而违反了堆的性质，需要将node向下交换。
    private void downSwap(PriorityNode node){
        PriorityNode subRoot = node;
        PriorityNode leftChild = getLeft(subRoot);
        PriorityNode rightChild = getRight(subRoot);

        while (true) {
            // 如果左子结点为空，说明已经到了最底层，应该退出。
            if (leftChild == null)
                break;
            // 如果右子结点为空，而左子结点非空，说明已经到了倒数第二层，所以至多交换一次，便可以退出。
            if (rightChild == null) {
                if (subRoot.compareTo(leftChild) > 0) swap(subRoot, leftChild);
                break;
            }
            // 如果左右子结点皆非空
            int cmp1 = subRoot.compareTo(leftChild);
            int cmp2 = subRoot.compareTo(rightChild);
            int cmp3 = leftChild.compareTo(rightChild);
            // 左子结点最小，则与左子结点交换，然后更新结点。
            if (cmp1 > 0 && cmp3 < 0) {
                swap(leftChild, subRoot);
                leftChild = getLeft(subRoot);
                rightChild = getRight(subRoot);
            }
            // 右子结点最小，则与右子结点交换， 然后更新结点。
            else if (cmp2 > 0 && cmp3 > 0) {
                swap(rightChild, subRoot);
                leftChild = getLeft(subRoot);
                rightChild = getRight(subRoot);
            }
            // 否则，subRoot最小，不需要继续交换。
            else { break; }
        }
    }

    // 假设传入的node可能小于其父结点而违反了堆的性质，需要将node向上交换。
    private void UpSwap(PriorityNode node){
        if(node.index == 1 || node.compareTo(getParent(node)) > 0){ return; }
        swap(node, getParent(node));
        UpSwap(node);
    }

}
