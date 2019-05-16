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


public class View extends Application{

    public static void main(String[] args) {
        launch(args);
    }


    public void start(Stage primaryStage){
        primaryStage.setTitle("A*                                                                                                                                                  Dijkstra’s");
        HBox root = new HBox();

        int height = 600;
        int width = 600;
        int cols = width/10;
        int rows = height/10;
        Map map = new Map(rows,cols);
        Map mapD = new Map(map);
        map.search();
        mapD.searchD();

        Canvas canvas = new Canvas(width,height);
        Canvas canvasD = new Canvas(width,height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GraphicsContext gcD = canvasD.getGraphicsContext2D();


        for (int i=0;i<rows;i++) {
            for (int j = 0; j < cols; j++) {
                InputStream is = getClass().getResourceAsStream( Map.ROOTPATH + map.getMap()[i][j]._type + ".png");
                Image img = new Image(is);
                gc.drawImage(img, j * 10, i * 10,10,10);
                gcD.drawImage(img,j * 10, i * 10,10,10);
            }
        }

        VBox vboxRight = new VBox();
        vboxRight.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        root.getChildren().add(canvas);
        vboxRight.getChildren().add(canvasD);


        InputStream isStart = getClass().getResourceAsStream( Map.ROOTPATH + "start.jpeg");
        Image imgS = new Image(isStart);
        gc.drawImage(imgS, map._start._x * 10, map._start._y * 10,10,10);
        gcD.drawImage(imgS, mapD._start._x * 10, mapD._start._y * 10,10,10);

        InputStream isDest = getClass().getResourceAsStream( Map.ROOTPATH + "dest.jpeg");
        Image imgD = new Image(isDest);
        gc.drawImage(imgD, map._dest._x * 10, map._dest._y * 10,10,10);
        gcD.drawImage(imgD, mapD._dest._x * 10, mapD._dest._y * 10,10,10);

        for (Node n: map.OPENLIST){
            drawOpenList(gc,n._x * 10,n._y * 10);
        }
////
        Node route = map._dest;
        int sum = 0;
        while(route != map._start){
            drawRoute(gc,route._x * 10, route._y * 10);
            sum += route._cost;
            route = route._prev;
        }
        System.out.println(sum);
//
        for (Node n: mapD.OPENLISTD){
            drawOpenList(gcD,n._x * 10,n._y * 10);
        }
////
        Node routeD = mapD._dest;
        sum = 0;
        while(routeD != mapD._start){
            drawRoute(gcD,routeD._x * 10, routeD._y * 10);
            sum += routeD._cost;
            routeD = routeD._prevD;
        }

        System.out.println(sum);

        root.getChildren().add(vboxRight);
        Scene newScene = new Scene(root,1200,600);
        primaryStage.setScene(newScene);

        primaryStage.show();

    }

    public void drawRoute(GraphicsContext gc,int x, int y){
        gc.setFill(Color.rgb(255, 0, 0, 0.5));
        gc.fillRect( x, y, 10, 10);
    }

    private void drawOpenList(GraphicsContext gc,int x, int y){
        gc.setFill(Color.rgb(0, 0, 0, 0.2));
        gc.fillRect( x, y, 10, 10);
    }
}