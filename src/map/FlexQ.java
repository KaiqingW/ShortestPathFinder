package map;

import java.util.Comparator;

public class FlexQ<T extends HeapVal<T>> {
    T[] _storage;
    int _size;
    int _capacity;
    Comparator<T> _cmp;

    //constructor with default comparator (the bigger the priority is higher)
    public FlexQ(int cap) {
        _capacity = cap;
        _storage = (T[]) new HeapVal[_capacity];
        _cmp = new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return o1.compareTo(o2);
            }
        };
    } // constructor
    //constructor with customized comparator
    public FlexQ(int cap, Comparator<T> cmp){
        _capacity = cap;
        _storage = (T[]) new HeapVal[_capacity];
        _cmp = cmp;
    } // constructor

    private int parent(int index){ return (index - 1)/2;}//parent

    private int left(int index){ return 2*index +1;}//left

    private int right(int index){ return 2*index +2;}//right

    public void insert(T element){
        if (_size >= _capacity) throw new RuntimeException("out of capacity");
        _storage[_size] = element;
        _size++;
        adjust(_size-1);
    }//insert

    public T pop(){
        if (_size > 0) {
            T temp = _storage[0];
            _storage[0] = _storage[_size - 1];
            _size--;
            adjust(0);
            return temp;
        }
        return null;
    }//pop

    // reorder the queue
    public void adjust(int i){
        if (i<0 || i>=_size) return;
        _storage[i].setIndex(i);
        // if priority is higher than its parent, go upside
        while (i > 0 && _cmp.compare(_storage[i],_storage[parent(i)]) > 0){
            int iParent = parent(i);
            T temp = _storage[iParent];                 // swap
            _storage[iParent] = _storage[i];
            _storage[i] = temp;
            _storage[i].setIndex(iParent);          // reset index after swap
            _storage[iParent].setIndex(i);
            i = iParent;
        }
        // otherwise, check the value of left and right, go downside
        int swapMark;
        do {
            int iLeft = left(i);
            int iRight = right(i);
            swapMark = -1;
            if (iLeft < _size && _cmp.compare(_storage[i],_storage[iLeft]) < 0) swapMark = iLeft;
            if (iRight < _size && _cmp.compare(_storage[i],_storage[iRight]) < 0 && _cmp.compare(_storage[iRight],_storage[iLeft]) > 0){
                swapMark = iRight;
            }

            if (swapMark != -1){
                T temp = _storage[i];                 // swap
                _storage[i] = _storage[swapMark];
                _storage[swapMark] = temp;
                _storage[i].setIndex(swapMark);          // reset index after swap
                _storage[swapMark].setIndex(i);
                i = swapMark;
            }
        }while (swapMark != -1);
    }//adjust

}
