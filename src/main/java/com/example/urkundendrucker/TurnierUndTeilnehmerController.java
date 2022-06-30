package com.example.urkundendrucker;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

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
    private Label lblPlatzierung;

    private Turnier turnier = null;

    private boolean editierenProcessOngoing = false;
    private Teilnehmer teilnehmerBeingEditiert;

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

            Teilnehmer erwin = new Teilnehmer("Freigraf Erwin Eduard Eckbert der Edle von Eichenteich-Eeilwasser", turnier, 8000.001);
            Teilnehmer auguste = new Teilnehmer("Freifrau Auguste Adelheit Alberta die Außergewöhnlich zu Augsburg-Aue", turnier, 8000);
            Teilnehmer felix = new Teilnehmer("Graf Felix von Wolfsburg", turnier, 9500);
            Teilnehmer gustav = new Teilnehmer("Gutsherr Gustav Gunther Gerald der Große von Gießen-Gutenfels", turnier, 5000);


            turnier.berechnePlatzierung();

            tbTeilnehmertabelle.getItems().clear();
            tbTeilnehmertabelle.getItems().addAll(turnier.getTeilnehmerListe());
            tbTeilnehmertabelle.refresh();

        }
    }

    @FXML
    protected void onBtDruckenClick() throws FileNotFoundException {

        //TODO add Feature to Print only specific Teilnehmer Urkunden (Select Multiple, Only activate TN Editieren Button wenn only a single Teilnehmer is ausgewählt

        if (turnier != null) {
            turnier.berechnePlatzierung();
            UrkundenGenerator.createAllUrkunden(turnier.getTeilnehmerListe());
            System.out.println("DruckenButtonPressed");
        }

    }

    @FXML
    protected void onTNErstellenClick() {
        if(editierenProcessOngoing) {

            lblPlatzierung.setText("");
            tfLaufzeit.setText("");
            tfVollerName.setText("");
            btTNErstellen.setText("TN Erstellen");
            btTNEditieren.setText("TN Editieren");

            btTNLoeschen.setDisable(false);
            btSpeichern.setDisable(false);
            btDrucken.setDisable(false);
            btLaden.setDisable(false);
            btSpeichern.setDisable(false);
            btNeuesTurnier.setDisable(false);

            editierenProcessOngoing = false;

        } else {
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
        }
        System.out.println("TNErstellenButtonPressed");

    }

    @FXML
    protected void onBtTNEditierenClick() {

        if(!editierenProcessOngoing) {

            Teilnehmer teilnehmer = (Teilnehmer) tbTeilnehmertabelle.getSelectionModel().getSelectedItem();
            if(teilnehmer != null) {
                try{
                    teilnehmerBeingEditiert = teilnehmer;

                    editierenProcessOngoing = true;

                    btTNLoeschen.setDisable(true);
                    btSpeichern.setDisable(true);
                    btDrucken.setDisable(true);
                    btLaden.setDisable(true);
                    btSpeichern.setDisable(true);
                    btNeuesTurnier.setDisable(true);

                    btTNEditieren.setText("Fertig");
                    btTNErstellen.setText("Abbrechen");

                    tfVollerName.setText(teilnehmer.getVollerName());
                    tfLaufzeit.setText(teilnehmer.getLaufzeit() + "");
                    lblPlatzierung.setText(teilnehmer.getPlatzierung() + "");

                } catch (Exception e) {

                }
            }
        } else {
            if(tfLaufzeit.getText() != "" && tfVollerName.getText() != "") {
                if (turnier != null && tfLaufzeit.getText() != "" && tfVollerName.getText() != "") {
                    try {
                        Teilnehmer teilnehmer = new Teilnehmer(tfVollerName.getText(), turnier, Double.valueOf(tfLaufzeit.getText()));

                        tbTeilnehmertabelle.getItems().remove(teilnehmerBeingEditiert);
                        tbTeilnehmertabelle.getItems().add(teilnehmer);
                        turnier.getTeilnehmerListe().remove(teilnehmerBeingEditiert);
                        turnier.berechnePlatzierung();
                        tbTeilnehmertabelle.refresh();

                        lblPlatzierung.setText("");
                        tfLaufzeit.setText("");
                        tfVollerName.setText("");
                        btTNErstellen.setText("TN Erstellen");
                        btTNEditieren.setText("TN Editieren");

                        btTNLoeschen.setDisable(false);
                        btSpeichern.setDisable(false);
                        btDrucken.setDisable(false);
                        btLaden.setDisable(false);
                        btSpeichern.setDisable(false);
                        btNeuesTurnier.setDisable(false);

                        editierenProcessOngoing = false;

                    } catch (Exception e) {
                        //TODO add Error Message
                    }
                }
            }
        }
    }

    @FXML
    protected void onBtTNLoeschenClick() {
        Teilnehmer teilnehmer = (Teilnehmer) tbTeilnehmertabelle.getSelectionModel().getSelectedItem();
        if(teilnehmer != null) {
            try{
                turnier.getTeilnehmerListe().remove(teilnehmer);
                turnier.berechnePlatzierung();
                tbTeilnehmertabelle.getItems().remove(teilnehmer);
                tbTeilnehmertabelle.refresh();
            } catch (Exception e) {

            }
        }


        System.out.println("tnLoeschenButtonPrssed");
    }

    @FXML
    protected void onBtSpeichernClick() throws IOException {
        if(turnier != null) {
            File file = chooseFile();
            try {
                FileWriter fileWriter = new FileWriter(file);
                CSVWriter csvWriter = new CSVWriter(fileWriter);

                String[] toWriteTurnierData = {turnier.getTurnierName(), turnier.getSportart(), turnier.getDatum() + ""};

                csvWriter.writeNext(toWriteTurnierData);

                csvWriter.close();
            } catch (IOException e) {
                //TODO Print error Message
            }
        }
        System.out.println("speichernPrssed");
    }

    @FXML
    protected void onBtLadenClick() throws IOException {

        File file = chooseFile();
        FileReader fileReader = new FileReader(file);
        CSVReader csvReader = new CSVReader(fileReader);

        String[] toReadTurnierData;
        toReadTurnierData = csvReader.readNext();
        System.out.println(Arrays.toString(toReadTurnierData));

        while ((toReadTurnierData = csvReader.readNext()) != null) {
            if (toReadTurnierData != null) {
                //Verifying the read data here
                System.out.println(Arrays.toString(toReadTurnierData));
            }



            csvReader.close();

            System.out.println("LadenButtonPrssed");
        }
    }

    protected Turnier createTurnierInNewWindow() throws IOException {
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(new Label("a Lable"));

        Stage stage = new Stage();

        Turnier turnier = TurnierErstellenController.showDialog(stage, lbUeberschrift.getScene().getWindow());

        System.out.println(turnier);

        return turnier;
    }

    private File chooseFile() throws IOException {

        FileChooser fileChooserDat = new FileChooser();
        File defaultPath = new File("src/urkundenOrdner");
        fileChooserDat.setInitialDirectory(defaultPath);
        fileChooserDat.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        Stage stage = (Stage) lbUeberschrift.getScene().getWindow();



        File file = fileChooserDat.showOpenDialog(stage);;

        //fileChooserDat.setInitialDirectory(new File(file.getParent()));

        return file;


    }

}