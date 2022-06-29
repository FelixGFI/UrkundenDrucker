package com.example.urkundendrucker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.time.LocalDate;

public class TurnierErstellenGUIController {

    @FXML Label lbUeberschrift;

    @FXML TextField tfTurnierName;
    @FXML TextField tfSportart;

    @FXML DatePicker dpDatum;

    @FXML Button btBestaetigen;
    @FXML Button btAbbrechen;

   private Turnier turnier;

    public Turnier showDialog(Stage stage, Window window) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TurnierUndTeilnehmerGUI.class.getResource("TurnierErstellenGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(window);
        stage.showAndWait();

        System.out.println("just before return" + turnier);

        return turnier;
    }

    public void initialize() {

    }

    @FXML
    protected void onBtAbbrechenClick() {
        turnier = null;
        Stage stage = (Stage) lbUeberschrift.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onBtBestaetigenClick() {
        turnier = new Turnier("Regionalmeisterschaften im 100 Meter Lauf Klein Britanien", "100 Meter Sprint", LocalDate.now());
        System.out.println(turnier);

        Stage stage = (Stage) lbUeberschrift.getScene().getWindow();
        stage.close();
    }
}