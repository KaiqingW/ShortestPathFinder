package map;

// each smallest unit on the map is a node
public class Node implements HeapVal<Node> {
    // grass, water or fire. has diff cost
    public int _type;
    // position
    public int _x, _y;
    // used for search node in FlexQ in constant time
    int _index = -1;
    // total cost with estimate (h function)
    public int _estCost;
    // cost of the type
    public int _cost;
    // accumulate real cost
    public int _accumulateCost;
    // predecessor on map
    public Node _prev;

    public Node(int y, int x){
        _x = x;
        _y = y;
    }//constructor

    public void copy(Node other){
        _x = other._x;
        _y = other._y;
        _estCost = other._estCost;
        _accumulateCost = other._accumulateCost;
        _prev = other._prev;
    }// copy

    public void setType(int t){
        _type = t;
        _cost = Map.COST[_type];
    }// setType

    public int getType(){return _type;} //getType

    @Override
    public int compareTo(Node n){
        return _estCost - n._estCost;
    }// compareTo

    @Override
    public void setIndex(int i){
        _index = i;
    }// setIndex

    @Override
    public int getIndex(){
        return _index;
    }// getIndex

}
