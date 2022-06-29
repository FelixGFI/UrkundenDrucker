package com.example.urkundendrucker;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;

public class UrkundenGenerator {

    static final String FILEPATH = "src/urkundenOrdner/";
    static int urkundenummer = 1;

    public static void main(String[] args) throws FileNotFoundException {


        Turnier zehnKilometerLauf = new Turnier("Wolfsburger Zehn Kilometer Wettlauf", "Ausdauerlauf", LocalDate.now());

        Turnier huerdenlauf = new Turnier("Tyroler Landeshürdenlauf", "Hürdenlauf", LocalDate.now());

        Teilnehmer erwin = new Teilnehmer("Freigraf Erwin Eduard Eckbert der Edle von Eichenteich-Eeilwasser", zehnKilometerLauf, 8000.001);
        Teilnehmer auguste = new Teilnehmer("Freifrau Auguste Adelheit Alberta die Außergewöhnlich zu Augsburg-Aue", zehnKilometerLauf, 8000);
        Teilnehmer felix = new Teilnehmer("Graf Felix von Wolfsburg", zehnKilometerLauf, 9500);
        Teilnehmer gustav = new Teilnehmer("Gutsherr Gustav Gunther Gerald der Große von Gießen-Gutenfels", zehnKilometerLauf, 5000);

        ArrayList<Teilnehmer> urkundenListe = zehnKilometerLauf.getTeilnehmerListe();

        createAllUrkunden(urkundenListe);

    }

    public static void createAllUrkunden(ArrayList<Teilnehmer> urkundenListe) throws FileNotFoundException {

        for (Teilnehmer teilnehmer : urkundenListe) {
            teilnehmer.getTurnier().berechnePlatzierung();

            createUrkunde(teilnehmer);
        }
    }

    private static void createUrkunde(Teilnehmer teilnehmer) throws FileNotFoundException {
        // Creating a PdfWriter

        String filePathName = generateUrkundeFilePathAndName(teilnehmer);

        if(filePathName != FILEPATH + urkundenummer + "_" + "_Urkunde.pdf") {
            PdfWriter writer = new PdfWriter(filePathName);

            // Creating a PdfDocument
            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.setDefaultPageSize(PageSize.A4);
            // Adding a new page
            pdfDoc.addNewPage();

            // Creating a Document
            Document document = new Document(pdfDoc);

            Paragraph headline = new Paragraph();
            headline.setFontSize(50);
            headline.setTextAlignment(TextAlignment.CENTER);
            headline.add("URKUNDE");

            Paragraph fuer = new Paragraph();
            fuer.setFontSize(16);
            fuer.setTextAlignment(TextAlignment.CENTER);
            fuer.add("für");

            Paragraph name = new Paragraph();
            name.setTextAlignment(TextAlignment.CENTER);
            name.setFontSize(25);
            name.add(teilnehmer.getVollerName());

            Paragraph platzierung = new Paragraph();
            platzierung.setTextAlignment(TextAlignment.CENTER);
            Text platz = new Text("Platz ");
            platz.setFontSize(16);
            Text platzZahl = new Text(teilnehmer.getPlatzierung() +  "");
            platzZahl.setFontSize(50);
            platzierung.add(platz);
            platzierung.add(platzZahl);

            Paragraph haupttext = new Paragraph();
            haupttext.setFontSize(16);
            haupttext.setTextAlignment(TextAlignment.CENTER);
            haupttext.add("im Turnier: " + teilnehmer.getTurnier().getTurnierName() + " am " + teilnehmer.getTurnier().getDatum() + "\n" + "in dem eine Zeit von " + teilnehmer.getLaufzeit() + " Sekunden erreichte wurde." +
                    "\n");

            document.add(headline);
            document.add(platzierung);
            document.add(fuer);
            document.add(name);
            document.add(haupttext);

            // Closing the document
            document.close();
            System.out.println("PDF Created");
        } else {
            System.out.println("Failed to Generate Urkunde. Please make sure all Teilnehmer are properly named");
        }
    }

    private static String generateUrkundeFilePathAndName(Teilnehmer teilnehmer) {
        char[] fileNameArray = teilnehmer.getVollerName().toCharArray();
        String fileName = "";

        for (char c : fileNameArray) {
            if (c == 'ö') {
                fileName = fileName + "oe";
            } else if (c == 'ä') {
                fileName = fileName + "ae";
            } else if (c == 'ü') {
                fileName = fileName + "ue";
            } else if (c == '!' || c == '.' || c == '?' || c == ':' || c == ' ') {
                fileName = fileName + "_";
            } else {
                fileName = fileName + c;
            }
        }
        String filePathName = FILEPATH + urkundenummer + "_" + fileName + "_Urkunde.pdf";
        return filePathName;
    }
}
