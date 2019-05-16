package map;
import java.util.*;

public class Map {
    public static final int GRASS = 0;
    public static final int WATER = 1;
    public static final int FIRE = 2;
    public static final String ROOTPATH = "/resources/map/";
    public static final String[] PICPATH= {"grass.gif","water.gif","flames.jpeg"};
    protected static int[] COST = {1,2,3};

    protected static double RandFactor = 0.10; // must be between 0 and .166
    protected static double[] BaseRF = {1,0.004,0.004};

    public static int _rad = 20;      // radius = side distance
    public static int calchpdist(){ // rad determines radius of hex
        double d = Math.sqrt(3.0*_rad*_rad/4.0);
        int hpdist = (int)(d+0.5);
        return hpdist;
    }

    // unit related define
    public static final int West = 0;
    public static final int NorthWest = 1;
    public static final int NorthEast = 2;
    public static final int East = 3;
    public static final int SouthEast = 4;
    public static final int SouthWest = 5;
    //movement vectors for w,nw,ne,e,se,sw, DX different for even,odd rows
    public static int[] DY = {0,-1,-1,0,1,1};
    public static int[][] DX = {{-1,-1,0,1,0,-1},{-1,0,1,1,1,0}};

    public ArrayList<Node> OPENLIST;
    Node[][] MAP;
    public Node _dest;
    public Node _start;
    int _cols;
    int _rows;

    public static void setRandFactor(double x)
    {
        if (x>0 && x<0.166) RandFactor = x;
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
                MAP[i][j].setType(GRASS);
            }
        }

        int GENS = 10;  // times of generations
        for(int g=0;g<GENS;g++) { generateType(WATER); }
        for(int g=0;g<GENS-1;g++) { generateType(WATER); }

        generateSD();
        OPENLIST = new ArrayList<Node>();
    } // constructor

    // generate start and destination randomly
    private void generateSD(){
        int sX,sY, tX, tY;
        do{
            sX = (int)(Math.random() * _cols);
            sY = (int)(Math.random() * _rows);
        } while (MAP[sY][sX]._cost == -1);
        do{
            tX = (int)(Math.random() * _cols);
            tY = (int)(Math.random() * _rows);
        } while (MAP[tY][tX]._cost == -1);

        _start = MAP[sY][sX];
        _dest = MAP[tY][tX];
    }
    private void generateType(int type){
        for(int i=0;i<_rows;i++) {
            for (int j = 0; j < _cols; j++) {
                double p = BaseRF[type];
                for (Node n : findNeighbours(MAP[i][j])) {
                    if (n != null && n.getType() == type) p += RandFactor;
                }
                double r = Math.random();
                if (r <= p) MAP[i][j].setType(type);
            }
        }
    }

    public int getRows(){return _rows;}
    public int getCols(){return _cols;}

    public Node[][] getMap() {
        return MAP;
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
    }

    public Node[] findNeighbours(Node n){
        Node[] neis = new Node[6];
        for(int k=0;k<6;k++) {
            int nextY = n._y + DY[k];
            int nextX = n._x + DX[n._y % 2][k];
            if (nextX >= 0 && nextX < _cols && nextY >= 0 && nextY < _rows){
                neis[k] = MAP[nextY][nextX];
            }
        }
        return neis;
    }

    private int hDiagonal(int y1, int x1, int y2, int x2) {
        int dx = Math.abs(x1-x2);
        int dy = Math.abs(y1-y2);
        int dd = (int)(Math.pow(dx,2) + Math.pow(dy, 2));
        return (int)(Math.sqrt(dd));
    }

    private static int h(int y1, int x1, int y2, int x2) {
        int dx = x1-x2, dy = y1-y2;
        int dd = Math.abs(dx - dy);
        dy = Math.abs(dy);
        int max = Math.abs(dx);
        if (dy>max) max = dy;
        if (dd>max) max = dd;
        return max;
    }

    public void search(){
        search(_start._x,_start._y,_dest._x,_dest._y);
    }

    public void search(int sX, int sY, int tX, int tY){

        HashSet<Node> interior = new HashSet<>();

        FlexQ<Node> openList = new FlexQ<Node>(_rows * _cols, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o2.compareTo(o1);
            }
        });

        Node current = _start;
        openList.insert(current);

        while (!interior.contains(MAP[tY][tX])) {
            current = openList.pop();
            interior.add(current);

            for (Node n : findNeighbours(current)) {
                if (n != null && !interior.contains(n)) {
                    Node temp = new Node(n._y, n._x);
                    temp.setType(n._type);
                    temp._accumulateCost = current._accumulateCost + temp._cost;
                    temp._estCost = temp._accumulateCost + h(temp._y, temp._x, _dest._y, _dest._x);
                    temp._prev = current;
                    if (n.getIndex() == -1) {
                        n.copy(temp);
                        openList.insert(n);
                        OPENLIST.add(n);
                    } else if (n.compareTo(temp) > 0) {
                        n.copy(temp);
                        openList.adjust(n.getIndex());
                    }
                }
            }
        }
    }

}
