package map;
import java.util.Hashtable;

public class Map {
    public static final int GRASS = 0;
    public static final int WATER = 1;
    public static final int FIRE = 2;
    protected int[] costof = {0,8,8000,8};
    public static final String ROOTPATH = "/resources/map/";
    public static final Hashtable<Integer,String> PICPATH= new Hashtable<Integer,String>(){{
        put(0,"grass1.gif");
        put(1,"water.gif");
        put(2,"flames.jpeg");
    }};

    protected static double RandFactor = 0.10; // must be between 0 and .166 // affects amount of water/fire
    public static void setRandFactor(double x)
    {
        if (x>0 && x<0.166) RandFactor = x;
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

    public static int _rad;      // radius = side distance
    public static int _hpdist;   // 1/2 distance from center to side
    public static void setRad(int r){
        _rad = r;
        _hpdist = calchpdist(_rad);
    }
    public static int calchpdist(int r){ // rad determines radius of hex
        double d = Math.sqrt(3.0*r*r/4.0);
        int hpdist = (int)(d+0.5);
        return hpdist;
    }

    Node[][] MAP;
    Node _dest;
    Node _start;
    int _cols;
    int _rows;

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
                MAP[i][j] = new Node(j,i);
                MAP[i][j].setType(GRASS);
            }
        }

        int GENS = 10;  // number of generations
        double pWater = 0.004;  // for random probability calculation
        double pFire = 0.004;
        int generation;
        for (generation = 0; generation < GENS; generation++) {
            generateMap(pWater,WATER);
        } // for each generation

        for (generation = 0; generation < GENS - 1; generation++) {
            generateMap(pFire,FIRE);
        } // for each generation

        for (Node[] nR: MAP){
            for(Node n: nR){
                n._cost = costof[n._type];
            }
        }
    }

    private void generateMap(double p, int t){
        for(int i=0;i<_rows;i++) {
            for (int j = 0; j < _cols; j++) {
                // calculate probability of Type t based on surrounding cells
                for (Node n : findNeighbours(MAP[i][j])) {
                    if (n != null && n._type == t) {
                        p += RandFactor;
                    }
                }
                double rand = Math.random();
                if (rand <= p) MAP[i][j].setType(t);
            }
        }
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

}
