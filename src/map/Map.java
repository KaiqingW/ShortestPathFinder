package map;
import java.util.*;

public class Map {
    public static final int GRASS = 0;
    public static final int WATER = 1;
    public static final int FIRE = 2;
    public static final String ROOTPATH = "/resources/map/";
    public static final Hashtable<Integer,String> PICPATH= new Hashtable<Integer,String>(){{
        put(0,"1.png");
        put(1,"2.png");
        put(2,"3.png");
    }};

    // unit related define
    public static final int West = 0;
    public static final int North = 1;
    public static final int East = 2;
    public static final int South = 3;
    //movement vectors for w,nw,ne,e,se,sw, DX different for even,odd rows
    public static int[] DY = {0,-1,0,1};
    public static int[] DX = {-1,0,1,0};

    public static int _size = 10;      // radius = side distance

    public ArrayList<Node> OPENLIST;
    public ArrayList<Node> OPENLISTD;
    Node[][] MAP;
    public Node _dest;
    public Node _start;
    int _cols = 600;
    int _rows = 600;

    public int getRows(){return _rows;}
    public int getCols(){return _cols;}
    public Node[][] getMap() {
        return MAP;
    }
    //construction
    public Map(int r, int c) {
        _cols = c;
        _rows = r;
        MAP = new Node[_rows][_cols];
        // initial all nodes as grass
        for (int i=0;i<_rows;i++){
            for (int j=0;j<_cols;j++){
                MAP[i][j] = new Node(i,j);
                Random rand = new Random();
                MAP[i][j].setType(rand.nextInt(3)+1);
            }
        }
        int sX = 1;
        int sY = 1;
        int tX = 57;
        int tY = 57;
//        int sX = (int)(Math.random() * _cols);
//        int sY = (int)(Math.random() * _rows);
//        int tX = (int)(Math.random() * _cols);
//        int tY = (int)(Math.random() * _rows);
        _start = MAP[sY][sX];
        _dest = MAP[tY][tX];
        OPENLIST = new ArrayList<Node>();
        OPENLISTD = new ArrayList<Node>();
    }

    public Map(Map other){
        _cols = other._cols;
        _rows = other._rows;
        MAP = new Node[_rows][_cols];

        for (int i=0;i<_rows;i++){
            for (int j=0;j<_cols;j++){
                MAP[i][j] = new Node(i,j);
                MAP[i][j].setType(other.getMap()[i][j]._type);
            }
        }
        _start = MAP[other._start._y][other._start._x];
        _dest = MAP[other._dest._y][other._dest._x];
        OPENLIST = new ArrayList<Node>();
        OPENLISTD = new ArrayList<Node>();
    }

    public Node[] findNeighbours(Node n){
        Node[] neis = new Node[4];
        for(int k=0;k<4;k++) {
            int nextY = n._y + DY[k];
            int nextX = n._x + DX[k];
            if (nextX >= 0 && nextX < _cols && nextY >= 0 && nextY < _rows){
                neis[k] = MAP[nextY][nextX];
            }
    }
        return neis;
    }

    public int hexdist(int y1, int x1, int y2, int x2) {
        int dx = Math.abs(x1-x2);
        int dy = Math.abs(y1-y2);
        int dd = (int)(Math.pow(dx,2) + Math.pow(dy, 2));
        return (int)(Math.sqrt(dd));
    }

    public void search(){
        search(_start._x,_start._y,_dest._x,_dest._y);
    }

    public void search(int sX, int sY, int tX, int tY){

        HashSet<Node> interior = new HashSet<>();

        FlexHeap<Node> openList = new FlexHeap<Node>(_rows * _cols);
        openList.changeCmp(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o2.compareTo(o1);
            }
        });

        Node current = _start;
        openList.insert(current);

        while (!interior.contains(MAP[tY][tX])) {
            current = openList.deletetop();
            interior.add(current);

            for (Node n : findNeighbours(current)) {
                if (n != null && !interior.contains(n)) {
                    Node temp = new Node(n._y, n._x);
                    temp.setType(n._type);
                    temp._accumulateCost = current._accumulateCost + temp._cost;
                    temp._estCost = temp._accumulateCost + hexdist(temp._y, temp._x, _dest._y, _dest._x);
                    temp._prev = current;
                    if (n.getIndex() == -1) {
                        n.copy(temp);
                        openList.insert(n);
                        OPENLIST.add(n);
                    } else if (n.compareTo(temp) > 0) {
                        n.copy(temp);
                        openList.requeue(n);
                    }
                }
            }
        }
    }

    public void searchD(){
        HashSet<Node> interior = new HashSet<>();
        FlexHeap<Node> openList = new FlexHeap<Node>(_rows * _cols);
        openList.changeCmp(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o2.compareTo(o1);
            }
        });

        Node current = _start;
        openList.insert(current);

        while (!interior.contains(_dest)) {
            current = openList.deletetop();
            interior.add(current);

            for (Node n : findNeighbours(current)){
                if (n != null && !interior.contains(n)) {
                    Node temp = new Node(n._y, n._x);
                    temp.setType(n._type);
                    temp._accumulateCost = current._accumulateCost + temp._cost;
                    temp._estCost = temp._accumulateCost;
                    temp._prevD = current;
                    if (n.getIndex() == -1) {
                        n.copy(temp);
                        openList.insert(n);
                        OPENLISTD.add(n);
                    } else if (n.compareTo(temp) > 0) {
                        n.copy(temp);
                        openList.requeue(n);
                    }
                }
            }
        }
    }

}
