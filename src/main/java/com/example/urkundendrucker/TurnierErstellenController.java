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

public class TurnierErstellenController {

    @FXML Label lbUeberschrift;

    @FXML TextField tfTurnierName;
    @FXML TextField tfSportart;

    @FXML DatePicker dpDatum;

    @FXML Button btBestaetigen;
    @FXML Button btAbbrechen;

    private Turnier turnier;

    public static Turnier showDialog(Stage stage, Window window) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(TurnierUndTeilnehmerApplication.class.getResource("TurnierErstellenView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        TurnierErstellenController ctrl = fxmlLoader.getController();

        stage.setTitle("Turnier Erstellung");
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(window);
        stage.showAndWait();

        return ctrl.turnier;
    }

    public void initialize() {
        tfTurnierName.setPromptText("Turnier Name");
        tfSportart.setPromptText("Sportart");
        dpDatum.setValue(LocalDate.now());
    }

    @FXML
    protected void onBtAbbrechenClick() {
        Stage stage = (Stage) lbUeberschrift.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onBtBestaetigenClick() {
        TurnierUndTeilnehmerController cntr = new TurnierUndTeilnehmerController();
        if(tfSportart.getText() != "" && tfTurnierName.getText() != "" && dpDatum.getValue() != null) {
            try {
                turnier = new Turnier(tfTurnierName.getText(), tfSportart.getText(), dpDatum.getValue());
                Stage stage = (Stage) lbUeberschrift.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                cntr.erzeugeInformationAlert("Erstellen Fehlgeschlagen", "Turnier Erstellung Fehlgeschlagen", "Bitte Stellen Sie sicher das alle Felder korrekt ausgefüllt sind");
            }
        } else {
            cntr.erzeugeInformationAlert("Erstellen Fehlgeschlagen", "Turnier Erstellung Fehlgeschlagen", "Bitte Stellen Sie sicher das alle Felder korrekt ausgefüllt sind");
        }
    }
}
