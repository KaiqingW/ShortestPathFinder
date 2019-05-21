package map;
import javafx.scene.image.Image;
import java.io.InputStream;
import java.util.*;

public class Map {
    // types
    public static final int GRASS = 0;
    public static final int WATER = 1;
    public static final int FIRE = 2;
    public static final String ROOTPATH = "/resources/map/";
    // map pics path
    public static final String[] PICPATH = {"grass1.gif","water.gif","flames.jpeg"};
    // pics of route
    public static final String[] ROUTEPATH = {"man15.gif","boat.gif","dragon.gif"};
    // map pics
    public Image[] IMAGE;
    public Image START = new Image(getClass().getResourceAsStream("/resources/item/man15.gif"));
    public Image DEST = new Image(getClass().getResourceAsStream("/resources/item/gem1.gif"));
    // route pics
    public Image[] ROUTE;
    // cost of diff types
    protected static int[] COST = {1,2,8};

    // random parameters for generating different types of land
    protected static double RandFactor = 0.10; // must be between 0 and .166
    protected static double[] BaseRF = {1,0.004,0.004};
    public static void setRandFactor(double x)
    {
        if (x>0 && x<0.166) RandFactor = x;
    }


    // diameter of hexagon
    public static int _rad = 40;      // radius = side distance
    // distance from center to sides
    public static double calchpdist(){
        int r = _rad/2;
        double d = Math.sqrt(3.0*r*r/4.0);
        return d;
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

    //constructor
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
        for(int g=0;g<GENS;g++) { generateType(WATER); }        // generate waters
        for(int g=0;g<GENS-1;g++) { generateType(FIRE); }       // generate fires

        generateSD();                                           // generate start and destination
        OPENLIST = new ArrayList<Node>();             //initialize OPENLIST to record all nodes used to be in OPENLIST
        search();                                               // find shortest path A*

        // initialize all pictures needed
        IMAGE = new Image[PICPATH.length];
        ROUTE = new Image[PICPATH.length];
        for (int i=0;i<PICPATH.length;i++){
            InputStream is = getClass().getResourceAsStream(ROOTPATH + PICPATH[i]);
            IMAGE[i] = new Image(is);
            InputStream isRoute = getClass().getResourceAsStream("/resources/item/" + ROUTEPATH[i]);
            ROUTE[i] = new Image(isRoute);
        }
    } // constructor

    // generate start and destination randomly on Grass
    private void generateSD(){
        int sX,sY, tX, tY;
        do{
            sX = (int)(Math.random() * _cols);
            sY = (int)(Math.random() * _rows);
        } while (MAP[sY][sX].getType() != 0);
        do{
            tX = (int)(Math.random() * _cols);
            tY = (int)(Math.random() * _rows);
        } while (MAP[tY][tX].getType() != 0);

        _start = MAP[sY][sX];
        _dest = MAP[tY][tX];
    }//generateSD

    // algorithm used to decide the places for a certain type
    // if it is surrounded by the type, it is more likely to be this type
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
    }//generateType

    public int getRows(){return _rows;}//getRows
    public int getCols(){return _cols;}//getCols

    public Node[][] getMap() {
        return MAP;
    }//getMap

    //copy constructor used to record the map generated randomly
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
    }// copy

    // return all neighbour nodes if available
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
    }//findNeighbours

    // calculate x coordinates of the six points on hexagon
    public double[] hexagonX(double x){
        double[] ans = new double[6];
        int r = _rad;
        double d = calchpdist();
        double[] DX = {0,d,d,0,-d,-d};
        for (int i=0;i<6;i++){
            ans[i] = x+DX[i];
        }
        return ans;
    }//hexagonX

    // calculate y coordinates of the six points on hexagon
    public double[] hexagonY(double y){
        double[] ans = new double[6];
        int r = _rad;
        double d = calchpdist();
        double[] DY = {r/2,r/4,-r/4,-r/2,-r/4,r/4};
        for (int i=0;i<6;i++){
            ans[i] = y + DY[i];
        }
        return ans;
    }//hexagonY

    // heuristic function of diagonal distance
    private int hDiagonal(int y1, int x1, int y2, int x2) {
        int dx = Math.abs(x1-x2);
        int dy = Math.abs(y1-y2);
        int dd = (int)(Math.pow(dx,2) + Math.pow(dy, 2));
        return (int)(Math.sqrt(dd));
    }//hDiagonal

    // another heuristic function
    private static int h(int y1, int x1, int y2, int x2) {
        int dx = x1-x2, dy = y1-y2;
        int dd = Math.abs(dx - dy);
        dy = Math.abs(dy);
        int max = Math.abs(dx);
        if (dy>max) max = dy;
        if (dd>max) max = dd;
        return max;
    }//h

    public void search(){
        search(_start._x,_start._y,_dest._x,_dest._y);
    }//search

    public void search(int sX, int sY, int tX, int tY){
        // close list record
        HashSet<Node> interior = new HashSet<>();
        // open list record
        FlexQ<Node> openList = new FlexQ<Node>(_rows * _cols, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o2.compareTo(o1);
            }
        });

        Node current = _start;
        openList.insert(current);

        while (!interior.contains(MAP[tY][tX])) {   // destination is not in close list
            current = openList.pop();
            interior.add(current);

            for (Node n : findNeighbours(current)) {
                if (n != null && !interior.contains(n)) {   // if node exist and not in close list
                    Node temp = new Node(n._y, n._x);
                    temp.setType(n._type);
                    temp._accumulateCost = current._accumulateCost + temp._cost;
                    temp._estCost = temp._accumulateCost + hDiagonal(temp._y, temp._x, _dest._y, _dest._x);
                    temp._prev = current;
                    if (n.getIndex() == -1) {               // if node is not in close list or open list
                        n.copy(temp);
                        openList.insert(n);
                        OPENLIST.add(n);
                    } else if (n.compareTo(temp) > 0) {     // if the new cost is smaller than the record on open list
                        n.copy(temp);
                        openList.adjust(n.getIndex());
                    }
                }
            }
        }
    }//search

}
