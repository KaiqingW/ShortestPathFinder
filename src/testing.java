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
    public int _rad = 50;      // radius = side distance
    public double calchpdist(){ // rad determines radius of hex
        int r = _rad/2;
        double d = Math.sqrt(3.0*r*r/4.0);
        return d;
    }

    public double[] hexagonX(int x){
        System.out.println("x");

        double[] ans = new double[6];
        int r = _rad;
        double d = calchpdist();
        double[] DX = {0,d,d,0,-d,-d};
        for (int i=0;i<6;i++){
            ans[i] = x+DX[i];
            System.out.println(ans[i]);

        }
        return ans;
    }

    public double[] hexagonY(int y){
        System.out.println("y");

        double[] ans = new double[6];
        int r = _rad;
        double d = calchpdist();
        double[] DY = {r/2,r/4,-r/4,-r/2,-r/4,r/4};
        for (int i=0;i<6;i++){
            ans[i] = y + DY[i];

            System.out.println(ans[i]);
        }

        return ans;
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("testing");
        HBox box = new HBox();
        Canvas canvas = new Canvas(400,400);
        GraphicsContext gc = canvas.getGraphicsContext2D();


        InputStream is = getClass().getResourceAsStream("/resources/map/grass1.gif");
        Image img = new Image(is);
        gc.setFill(new ImagePattern(img));
        int x = 100;
        int y = 100;
        gc.fillPolygon(hexagonX(x),hexagonY(y),6);

        box.getChildren().add(canvas);
        Scene newScene = new Scene(box,400,400);
        primaryStage.setScene(newScene);

        primaryStage.show();

    }

    public static void main(String args[]){
        launch(args);
    }
}
