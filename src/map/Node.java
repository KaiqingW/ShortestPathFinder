package map;

public class Node implements HeapVal<Node> {

    public int _type;
    public int _x, _y;
    int _index = -1;
    public int _estCost;
    public int _cost;
    public int _accumulateCost;
    public Node _prev;

    public Node(int y, int x){
        _x = x;
        _y = y;
    }

    public void copy(Node other){
        _x = other._x;
        _y = other._y;
        _estCost = other._estCost;
        _accumulateCost = other._accumulateCost;
        _prev = other._prev;
    }

    public void setType(int t){
        _type = t;
        _cost = Map.COST[_type];
    }

    public int getType(){return _type;}

    @Override
    public int compareTo(Node n){
        return _estCost - n._estCost;
    }

    @Override
    public void setIndex(int i){
        _index = i;
    }

    @Override
    public int getIndex(){
        return _index;
    }

}
