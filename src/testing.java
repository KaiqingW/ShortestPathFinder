import javafx.scene.layout.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import map.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.InputStream;

public class testing extends Application{
    public int _rad = 40;      // radius = side distance
    public double calchpdist(){ // rad determines radius of hex
        int r = _rad/2;
        double d = Math.sqrt(3.0*r*r/4.0);
        return d;
    }

    public double[] hexagonX(double x){
        double[] ans = new double[6];
        int r = _rad;
        double d = calchpdist();
        double[] DX = {0,d,d,0,-d,-d};
        for (int i=0;i<6;i++){
            ans[i] = x+DX[i];
        }
        return ans;
    }

    public double[] hexagonY(double y){
        double[] ans = new double[6];
        int r = _rad;
        double d = calchpdist();
        double[] DY = {r/2,r/4,-r/4,-r/2,-r/4,r/4};
        for (int i=0;i<6;i++){
            ans[i] = y + DY[i];
        }
        return ans;
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("testing");
        HBox box = new HBox();
        Canvas canvas = new Canvas(1000,1000);
        GraphicsContext gc = canvas.getGraphicsContext2D();

//        InputStream is = getClass().getResourceAsStream("/resources/map/grass1.gif");
//        Image img = new Image(is);
//        gc.setFill(new ImagePattern(img));
//        int x = 100;
//        int y = 100;
//        gc.fillPolygon(hexagonX(x),hexagonY(y),6);

        Image[] imgs = new Image[3];
        InputStream is0 = getClass().getResourceAsStream("/resources/map/grass1.gif");
        Image img0 = new Image(is0);

        InputStream is1 = getClass().getResourceAsStream("/resources/map/water.gif");
        Image img1 = new Image(is1);

        InputStream is2 = getClass().getResourceAsStream("/resources/map/flames.jpeg");
        Image img2 = new Image(is2);

        imgs[0] = img0;
        imgs[1] = img1;
        imgs[2] = img2;

        Map m = new Map(20,20);
        for (int i=0;i<m.getRows();i++){
            for (int j=0;j<m.getCols();j++){
                int picIndex = m.getMap()[i][j].getType();
//                InputStream is = getClass().getResourceAsStream("/resources/map/grass.gif");
//                Image img = new Image(is);
                gc.setFill(new ImagePattern(imgs[picIndex]));
                double x = j * calchpdist() *2 + (i%2+1) * calchpdist();
                double y = i * _rad*3/4 + _rad;
                gc.fillPolygon(hexagonX(x),m.hexagonY(y),6);
            }
        }

        box.getChildren().add(canvas);
        Scene newScene = new Scene(box,1000,1000);
        primaryStage.setScene(newScene);

        primaryStage.show();

    }

    public static void main(String args[]){
        launch(args);
    }
}
