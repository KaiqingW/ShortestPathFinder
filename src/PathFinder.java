import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PathFinder extends Application {
    public void start(Stage primaryStage){
        primaryStage.setTitle("testing");
        HBox box = new HBox();
        Canvas canvas = new Canvas(1400,800);

        // generate map, calculate the path, and draw everything on canvas
        Controller c = new Controller(1400,800, canvas);
        box.getChildren().add(canvas);
        Scene newScene = new Scene(box,1400,800);
        primaryStage.setScene(newScene);
        primaryStage.show();
    }//start

    public static void main(String[] args) {
        launch(args);
    }//main
}
