package com.example.urkundendrucker;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

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
        turnier = createTurnierInNewWindow();
        if(turnier != null) {



            Teilnehmer erwin = new Teilnehmer("Freigraf Erwin Eduard Eckbert der Edle von Eichenteich-Eeilwasser", turnier, 8000.001);
            Teilnehmer auguste = new Teilnehmer("Freifrau Auguste Adelheit Alberta die Außergewöhnlich zu Augsburg-Aue", turnier, 8000);
            Teilnehmer felix = new Teilnehmer("Graf Felix von Wolfsburg", turnier, 9500);
            Teilnehmer gustav = new Teilnehmer("Gutsherr Gustav Gunther Gerald der Große von Gießen-Gutenfels", turnier, 5000);

            showTurnierInTabelView();

        }
    }

    private void showTurnierInTabelView() {
        if(turnier != null) {
            lbUeberschrift.setText("Teilnehmerliste zu " + turnier.getTurnierName());

            btTNLoeschen.setDisable(false);
            btTNEditieren.setDisable(false);
            btTNErstellen.setDisable(false);
            btSpeichern.setDisable(false);
            btDrucken.setDisable(false);

            turnier.berechnePlatzierung();

            tbTeilnehmertabelle.getItems().clear();
            if(turnier.getTeilnehmerListe() != null && !turnier.getTeilnehmerListe().isEmpty()) {
                tbTeilnehmertabelle.getItems().addAll(turnier.getTeilnehmerListe());
            }
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
            if (turnier != null && !tfLaufzeit.getText().equals("") && !tfVollerName.getText().equals("")) {
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
                    //TODO Fehlermeldung
                }
            }
        } else {
            if(!tfLaufzeit.getText().equals("") && !tfVollerName.getText().equals("")) {
                if (turnier != null &&! tfLaufzeit.getText().equals("") && !tfVollerName.getText().equals("")) {
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
                //TODO Error Message
            }
        }


        System.out.println("tnLoeschenButtonPrssed");
    }

    @FXML
    protected void onBtSpeichernClick() {
        if(turnier != null) {
            File file = chooseFile(true);
            try {
                FileWriter fileWriter = new FileWriter(file);
                CSVWriter csvWriter = new CSVWriter(fileWriter);

                String[] toWriteTurnierData = {turnier.getTurnierName(), turnier.getSportart(), turnier.getDatum() + ""};

                csvWriter.writeNext(toWriteTurnierData);

                if(!turnier.getTeilnehmerListe().isEmpty() && turnier.getTeilnehmerListe() != null) {
                    for (Teilnehmer teilnehmer: turnier.getTeilnehmerListe()) {
                        String[] toWriteTeilnehmerData = {teilnehmer.getVollerName(), teilnehmer.getLaufzeit() + ""};
                        csvWriter.writeNext(toWriteTeilnehmerData);
                    }
                }

                csvWriter.close();
            } catch (IOException e) {
                //TODO Print error Message
            }
        }
        System.out.println("speichernPrssed");
    }

    @FXML
    protected void onBtLadenClick() throws IOException {

        File file = chooseFile(false);
        FileReader fileReader = new FileReader(file);
        CSVReader csvReader = new CSVReader(fileReader);

        List<String[]> dataList = csvReader.readAll();

        if(!dataList.isEmpty()) {

            boolean turnierErfolgreichErstellt = false;

            for (String[] data : dataList) {
                if(dataList.indexOf(data) == 0) {
                    turnierErfolgreichErstellt = createTurnierFromStringArrayData(data);
                } else {
                    if(turnierErfolgreichErstellt) {
                        createTeilnehmerFromStringArrayDataAndAddToTurnier(data);
                    }
                }
            }
            if(turnierErfolgreichErstellt) {
                showTurnierInTabelView();
            }
        }
    }

    private boolean createTurnierFromStringArrayData(String[] turnierData) {
        boolean turnerErfolgreichErstellt = false;
        try{
            turnier = new Turnier(turnierData[0], turnierData[1], LocalDate.parse(turnierData[2]));
            turnerErfolgreichErstellt = true;
        } catch (Exception e) {
            //TODO fehlermeldung
        }
        return turnerErfolgreichErstellt;
    }

    private void createTeilnehmerFromStringArrayDataAndAddToTurnier(String[] teilnehmerData) {
        try{
            //the Variable teilnehmer IS NECCESERRY !DO NOT DELETE! the constructor of Class Teilnehmer atomaticaly adds the Teilnehmer to the given Turniers Teilnehmerliste
            Teilnehmer teilnehmer = new Teilnehmer(teilnehmerData[0], turnier, Double.parseDouble(teilnehmerData[1]));
        } catch (Exception e) {

            //TODO fehlermeldung
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

    private File chooseFile(boolean methodeAufgerufenUmDatenZuspeichern) {

        FileChooser fileChooserDat = new FileChooser();
        File defaultPath = new File("src/urkundenOrdner");
        fileChooserDat.setInitialDirectory(defaultPath);
        fileChooserDat.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        Stage stage = (Stage) lbUeberschrift.getScene().getWindow();

        File file;
        if(methodeAufgerufenUmDatenZuspeichern) {
            file = fileChooserDat.showSaveDialog(stage);
        } else {
            file = fileChooserDat.showOpenDialog(stage);
        }
        return file;
    }

}