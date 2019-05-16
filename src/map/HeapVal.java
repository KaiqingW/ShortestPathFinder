package map;
// used for tracking position in heap
// as a result the time complexity of searching an element in PQ is constant time

public interface HeapVal<T> extends Comparable<T> {
    public int getIndex();
    public void setIndex(int i);
}
