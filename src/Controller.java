
import map.*;
import javafx.scene.layout.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import map.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.InputStream;


public class Controller{
    Map _map;
    Canvas _canvas;
    GraphicsContext _gc;

    public Controller(int r, int c){
        _map = new Map(r,c);
        _canvas = new Canvas(c,r);
        _gc = _canvas.getGraphicsContext2D();
        init();
    }
    private void init(){
        drawMap();
        findPath();
    }

    public void drawMap(){
        for (int i=0;i<_map.getRows();i++){
            for (int j=0;j<_map.getCols();j++){
                int picIndex = _map.getMap()[i][j].getType();
                InputStream is = _gc.getClass().getResourceAsStream(Map.ROOTPATH + Map.PICPATH[picIndex]);
                Image img = new Image(is);
                _gc.drawImage(img,1,1,1,1);
            }
        }
    }

    public void findPath(){

    }

}
