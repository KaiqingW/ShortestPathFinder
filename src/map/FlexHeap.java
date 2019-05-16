// Priority Queue using Heap, largest value on top
package map;

import java.util.Comparator;

public class FlexHeap<T extends HeapVal<? super T>> {
    protected T[] H; // underlying array
    protected int size;
    public int size() { return size; }
    public int capacity() { return H.length; }

    // Comparator instead of Comparable allows comparsion function to
    // be changed easily
    Comparator<T> cmp;    
    class defaultcomparator implements Comparator<T> {
		public int compare(T x, T y) { return x.compareTo(y); }
    }
    public void changeCmp(Comparator<T> newcmp)
    {
	if (newcmp!=null) cmp = newcmp;
    }

    public FlexHeap(int cap) {
		H = (T[]) new HeapVal[cap]; // expect compiler warning
		cmp = new defaultcomparator();
    }
    public FlexHeap(T[] H, int size) {
		if (H==null || size<0 || size>H.length)
	    throw new RuntimeException("invalid params");
		this.H = H;
		if (size<=H.length) this.size = size;
		cmp = new defaultcomparator();
    }

    // index of left, right, child of index i and parent of i
    // parent is always at index 0 (not index 1 like in many textbooks)
    protected int left(int i) { return i*2+1; }
    protected int right(int i) { return i*2+2; } // (i+1)*2
    protected int parent(int i) { return (i-1)/2; }

    protected int propagate(int i){ // propagate value at i up or down tree// return new position of value.
		if (i<0 || i>size) return i; // invalid value, do nothing.
		// check if larger than parent:
		H[i].setIndex(i);
		while (i>0 && cmp.compare(H[i],H[parent(i)])>0) { // swap upwards
			int pi = parent(i);
			T tmp = H[i];
			H[i] = H[pi];  H[pi] = tmp;
			H[i].setIndex(i);  H[pi].setIndex(pi);
			i = pi;
	    }
		int si = -1; // swap index, -1 means no need to swap
		do {
			// check if H[i] less than either children
			int li = left(i), ri = right(i);
			si = -1; // default no swap
			if (li<size && cmp.compare(H[i],H[li])<0) si = li;
			if (ri<size && cmp.compare(H[i],H[ri])<0 && cmp.compare(H[ri],H[li])>0) si = ri;
			if (si !=-1) // swap down
				{
				T tmp = H[i]; H[i]=H[si]; H[si]=tmp;
				H[i].setIndex(i);  H[si].setIndex(si);
				i = si;
				}
		} while (si != -1);
		return i;
    }//propagate
    
    public void enqueue(T x) {
		if (size>=H.length) throw new RuntimeException("heap full");
		// place at bottom of heap and propagate upwards
		H[size] = x;  // size is index of next available slot.
		size++;
		propagate(size-1);
    }
    public void insert(T x) { enqueue(x); }

    public T dequeue() {// remove highest priority element
		if (size<1) throw new RuntimeException("queue empty");
		T answer = H[0];  // largest element always on top.
		// place last element on top, then propagate down if necessary
		H[0] = H[--size]; // size--; H[0]=H[size];
		propagate(0);
		return answer;
    }
    public T deletetop() { return dequeue(); }
    public T peek() { if (size>0) return H[0]; else return null; }

    public boolean contains(T x) { // use .equals
		for(int i=0;i<size;i++)
			if (H[i].equals(x)) return true;
		return false;
    }

    // Correct position when priority changes:
    public int requeue(T x) {
		if (H[x.getIndex()]!=x) throw new RuntimeException("FlexHeap index inconsisitent: "+x.getIndex());
		return propagate(x.getIndex());
    }
    
}//FlexHeap

