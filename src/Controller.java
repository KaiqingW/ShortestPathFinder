
import javafx.scene.paint.ImagePattern;
import map.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Controller{
    Map _map;
    Canvas _canvas;
    GraphicsContext _gc;

    public Controller(int w, int h,Canvas c){
        //since one unit on map is not on pix, so convert from size of canvas to number of map units is necessary
        int rows = 4 * h / Map._rad / 3;
        int cols = (int)(w / Map.calchpdist() / 2 - 1);
        _map = new Map(rows,cols);
        _canvas = c;
        _gc = _canvas.getGraphicsContext2D();
        init();
    }//constructor
    private void init(){
        drawMap();
        findPath();
    }

    public void drawMap(){
        for (int i=0;i<_map.getRows();i++){
            for (int j=0;j<_map.getCols();j++){
                // the type of the node on this position
                int picIndex = _map.getMap()[i][j].getType();
                // find corresponding picture by node type
                _gc.setFill(new ImagePattern(_map.IMAGE[picIndex]));
                // convert coordinate
                double x = j * Map.calchpdist() * 2 + Map.calchpdist() * (i % 2 +1);
                double y = i * Map._rad * 3/4 + Map._rad / 2;
                // draw hexagon on by on
                _gc.fillPolygon(_map.hexagonX(x),_map.hexagonY(y),6);
            }
        }
        // draw start and destination pictures
        int size = 20;
        _gc.drawImage(_map.START,drawPos(_map._start._x,_map._start._y,size)[0],drawPos(_map._start._x,_map._start._y,size)[1],size,size);
        _gc.drawImage(_map.DEST,drawPos(_map._dest._x,_map._dest._y,size)[0],drawPos(_map._dest._x,_map._dest._y,size)[1],size,size);
}//drawMap

    // calculate draw coordinates by position in map and size
    private double[] drawPos(int x,int y, int size){
        double drawX = x * Map.calchpdist() * 2 + Map.calchpdist() * (y % 2 +1) - size/2;
        double drawY = y * Map._rad * 3/4 + Map._rad / 2 - size/2;
        double[] ans = {drawX,drawY};
        return ans;
    }// drawPos

    // draw shortest path calculated
    public void findPath(){
        int size = 10;
        Node cur = _map._dest;
        while(cur != _map._start){
            cur = cur._prev;
            //try{Thread.sleep(500);} catch (Exception e){};
            Image img = _map.ROUTE[cur.getType()];
            _gc.drawImage(img,drawPos(cur._x,cur._y,size)[0],drawPos(cur._x,cur._y,size)[1],size,size);
        }
        // mark out the nodes involved
        _gc.setFill(Color.ALICEBLUE);
        for (Node n: _map.OPENLIST){
            _gc.fillText("o",drawPos(n._x,n._y,5)[0],drawPos(n._x,n._y,5)[1],5);
        }
    }//findPath
}
