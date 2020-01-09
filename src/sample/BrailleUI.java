package sample;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BrailleUI extends Application {
    double x, y;

    public static void main(String[] args) {


        Application.launch(args);

    }

    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("BrailleUI-big.fxml"));
        Parent root = (Parent) loader.load();
        BrailleController controllerObj = (BrailleController) loader.getController();
        controllerObj.setStage(primaryStage);

        Scene scene = new Scene(root);

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);


        root.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                x = event.getSceneX();
                y = event.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - x);
                primaryStage.setY(event.getScreenY() - y);
            }
        });


        primaryStage.setScene(scene);
        primaryStage.show();


    }
}


