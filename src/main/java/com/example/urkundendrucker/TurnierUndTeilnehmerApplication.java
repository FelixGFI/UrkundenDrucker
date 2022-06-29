package com.example.urkundendrucker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TurnierUndTeilnehmerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TurnierUndTeilnehmerApplication.class.getResource("TurnierUndTeilnehmerGUIView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Teilnehmerliste");
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}