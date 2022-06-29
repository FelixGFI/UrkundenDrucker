package com.example.urkundendrucker;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TurnierUndTeilnehmerController {
    @FXML
    private Label lbUeberschrift;

    @FXML
    private Button btSchliessen;
    @FXML
    private Button btDrucken;
    @FXML
    private Button btTNErstellen;
    @FXML
    private Button btNeuesTurnier;
    @FXML
    private Button btLaden;
    @FXML
    private Button btSpeichern;
    @FXML
    private Button btTNEditieren;
    @FXML
    private Button btTNLoeschen;

    @FXML
    private TableView tbTeilnehmertabelle = new TableView();

    @FXML
    private TableColumn<Teilnehmer, String> tcVollerName = new TableColumn();
    @FXML
    private TableColumn<Teilnehmer, Double> tcLaufzeit = new TableColumn<>();
    @FXML
    private TableColumn<Teilnehmer, Integer> tcPlatzierung = new TableColumn<>();

    @FXML
    private TextField tfVollerName;
    @FXML
    private TextField tfLaufzeit;

    @FXML
    private Label lblLaufzeit;

    private Turnier turnier = null;

    public void initialize() {

        //TODO evtl. Leeres Turnier erzeugen
        //TODO wenn Leeres Turnier erzeugt wird auf jeden Fall edit Projekt button
        tbTeilnehmertabelle.setEditable(true);
        TableView.TableViewSelectionModel selectionModel = tbTeilnehmertabelle.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        tcVollerName.setCellValueFactory(new PropertyValueFactory<>("vollerName"));
        tcLaufzeit.setCellValueFactory(new PropertyValueFactory<>("laufzeit"));
        tcPlatzierung.setCellValueFactory(new PropertyValueFactory<>("platzierung"));
        tbTeilnehmertabelle.setPlaceholder(new Label("keine Teilnehmer vorhanden"));

        tfLaufzeit.setPromptText("Zeit");
        tfVollerName.setPromptText("Voller Name");

        btTNErstellen.setDisable(true);
        btTNEditieren.setDisable(true);
        btTNLoeschen.setDisable(true);
        btSpeichern.setDisable(true);
        btDrucken.setDisable(true);
    }

    @FXML
    protected void onBtSchliessenClick() {
        Stage stage = (Stage) lbUeberschrift.getScene().getWindow();
        stage.close();
        System.exit(1);
    }

    @FXML
    protected void onBtNeuesTurnierClick() throws IOException {
        //TODO create warning "Alle ungespeicherten Daten werden verworfen"
        //TODO add Dialog to create Turnier
        turnier = createTurnierInNewWindow();
        if(turnier != null) {

            lbUeberschrift.setText("Teilnehmerliste zu " + turnier.getTurnierName());

            btTNLoeschen.setDisable(false);
            btTNEditieren.setDisable(false);
            btTNErstellen.setDisable(false);
            btSpeichern.setDisable(false);
            btDrucken.setDisable(false);

            turnier.berechnePlatzierung();

            tbTeilnehmertabelle.getItems().clear();
            tbTeilnehmertabelle.getItems().addAll(turnier.getTeilnehmerListe());
            tbTeilnehmertabelle.refresh();

        }
    }

    @FXML
    protected void onBtDruckenClick() throws FileNotFoundException {
        if (turnier != null) {
            turnier.berechnePlatzierung();
            UrkundenGenerator.createAllUrkunden(turnier.getTeilnehmerListe());
            System.out.println("DruckenButtonPressed");
        }

    }

    @FXML
    protected void onTNErstellenClick() {
        if (turnier != null && tfLaufzeit.getText() != "" && tfVollerName.getText() != "") {
            try {
                Teilnehmer teilnehmer = new Teilnehmer(tfVollerName.getText(), turnier, Double.valueOf(tfLaufzeit.getText()));
                tfLaufzeit.setText("");
                tfVollerName.setText("");
                tbTeilnehmertabelle.getItems().add(teilnehmer);
                turnier.berechnePlatzierung();
                tbTeilnehmertabelle.refresh();
            } catch (Exception e) {
                //TODO add Error Message
            }

        }
        System.out.println("TNErstellenButtonPressed");
    }

    @FXML
    protected void onBtTNEditierenClick() {
        try{

        } catch (Exception e) {

        }

        System.out.println("tnEditierenButtonPrssed");
    }

    @FXML
    protected void onBtTNLoeschenClick() {
        System.out.println("tnLoeschenButtonPrssed");
    }

    @FXML
    protected void onBtSpeichernClick() {
        System.out.println("speichernPrssed");
    }

    @FXML
    protected void onBtLadenClick() {
        System.out.println("LadenButtonPrssed");
    }

    protected Turnier createTurnierInNewWindow() throws IOException {
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(new Label("a Lable"));

        Stage stage = new Stage();

        Turnier turnier = TurnierErstellenController.showDialog(stage, lbUeberschrift.getScene().getWindow());

        System.out.println(turnier);

        return turnier;
    }

}