import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * Created by Tyler Wilding on 05/06/16.
 * TA-Code-Reviewer - COSC
 */
public class Comparer extends Application {

    private ArrayList<Suspect> suspects = new ArrayList<Suspect>();

    private File directory;

    private Button beginCompare = new Button("Begin Comparison");
    private Button exportPDFButton = new Button("Export PDF");

    private TextArea log = new TextArea("Log\n");

    private ProgressBar pb = new ProgressBar(0);
    private Label barLabel = new Label("Pending...");
    private int numFilesChecked = 0;
    private int numFilesTotal = 0;

    StringProperty textProperty = new SimpleStringProperty("Pending...");

    @Override
    public void start(Stage stage) {

        barLabel.textProperty().bind(textProperty);

        pb.setMinWidth(500);

        ScrollPane scrollpane = new ScrollPane();
        scrollpane.setContent(log);
        scrollpane.setFitToWidth(true);
        scrollpane.setFitToHeight(true);

        beginCompare.setDisable(true);
        exportPDFButton.setDisable(true);
        Button chooseDir = new Button("Select Assignment Path...");

        DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setTitle("Select Folder containing Files to Check");

        HBox controls = new HBox(50);
            controls.setPadding(new Insets(20));
            controls.setAlignment(Pos.CENTER);
            controls.getChildren().addAll(chooseDir, beginCompare, exportPDFButton);

        HBox bar = new HBox(50);
            bar.setPadding(new Insets(20));
            bar.setAlignment(Pos.CENTER);
            bar.getChildren().addAll(barLabel, pb);

        BorderPane layout = new BorderPane();
            layout.setTop(controls);
            layout.setCenter(scrollpane);
            layout.setBottom(bar);

        //Event Actions
        chooseDir.setOnAction( e -> gatherSuspectsAndFiles(dirChooser, stage));
        beginCompare.setOnAction( e -> suspectIterator());
        exportPDFButton.setOnAction( e -> exportPDF(stage));

        layout.getChildren().addListener( ( ListChangeListener.Change<? extends Node> c ) -> {

            scrollpane.layout();
            scrollpane.setVvalue( 1.0d );

        });

        Scene scene = new Scene(layout, 1000, 500);
            stage.setTitle("TA Code Reviewer - Comparer");
            stage.setScene(scene);
            stage.show();
    }

    private void exportPDF(Stage stage) {

        log.appendText("Begin Exporting PDF!\n");

        Dialog dialog = new Dialog();
        dialog.setTitle("PDF Detail Entry");
        dialog.setHeaderText("Enter the following fields and hit the 'Confirm' button");
        dialog.setResizable(false);

        Label assignmentName = new Label("Assignment Name: ");
        Label courseCode = new Label("Course Code: ");
        Label professor = new Label("Professor: ");
        Label ta = new Label("TA Name: ");

        TextField assignmentNameField = new TextField();
        TextField courseCodeField = new TextField();
        TextField professorField = new TextField();
        TextField taField = new TextField();

        GridPane grid = new GridPane();
        grid.add(assignmentName, 1, 1);
        grid.add(assignmentNameField, 1, 2);
        grid.add(courseCode, 2, 1);
        grid.add(courseCodeField, 2, 2);
        grid.add(professor, 3, 1);
        grid.add(professorField, 3, 2);
        grid.add(ta, 4, 1);
        grid.add(taField, 4, 2);

        dialog.getDialogPane().setContent(grid);
        ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(confirm);

        ArrayList<String> info = new ArrayList<>();

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent()) {

            info.add(assignmentNameField.getText());
            info.add(courseCodeField.getText());
            info.add(professorField.getText());
            info.add(taField.getText());
        }

        Document pdf = new Document();

        log.appendText("Information Entered, Finding File Location...\n");
        String[] folder = directory.getAbsolutePath().split("/");
        String destination = "";
        for(int i = 0; i < folder.length-1; i++)
            destination += folder[i]+"/";

        log.appendText("File Location found, creating PDF...\n");

        try {

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 42.0f, Font.BOLD, BaseColor.BLACK);
            Font linkFont = new Font(Font.FontFamily.HELVETICA, 14.0f, Font.NORMAL, BaseColor.BLUE);
            Font paraFontBold = new Font(Font.FontFamily.HELVETICA, 14.0f, Font.BOLD, BaseColor.BLACK);
            Font paraFont = new Font(Font.FontFamily.HELVETICA, 14.0f, Font.NORMAL, BaseColor.BLACK);


            PdfWriter.getInstance(pdf, new FileOutputStream(destination+"pdfResult"));
            pdf.open();

            Chunk title = new Chunk(info.get(0), titleFont);
            Chunk code = new Chunk(info.get(1), titleFont);
            Chunk prof = new Chunk(info.get(2), titleFont);
            Chunk tainfo = new Chunk(info.get(3), titleFont);

            log.appendText("Creating Title Page...\n");
            Paragraph p = new Paragraph(title);
            p.setAlignment(Element.ALIGN_CENTER);
            pdf.add(p);
            pdf.add(new Chunk(new LineSeparator()));
            p = new Paragraph(code);
            p.setAlignment(Element.ALIGN_CENTER);
            pdf.add(p);
            p = new Paragraph(prof);
            p.setAlignment(Element.ALIGN_CENTER);
            pdf.add(p);
            p = new Paragraph(tainfo);
            p.setAlignment(Element.ALIGN_CENTER);
            pdf.add(p);
            pdf.add(new Chunk(new LineSeparator()));
            p = new Paragraph(new Chunk(new Date().toString(), titleFont));
            p.setAlignment(Element.ALIGN_CENTER);
            pdf.add(p);
            pdf.newPage();

            log.appendText("Creating Table of Contents...\n");
            //Table of Contents
            pdf.add(new Paragraph(new Chunk("Table of Contents", paraFontBold)));
            pdf.add(new Chunk(new LineSeparator()));
            pdf.add(new Chunk("\n"));


            for(int i = 0; i < suspects.size(); i++) {

                Anchor link = new Anchor("  "+suspects.get(i).getName(), linkFont);
                link.setReference("#"+suspects.get(i).getName());

                pdf.add(link);
                pdf.add(new Chunk(" "+suspects.get(i).largestScore()+"\n"));
            }

            pdf.newPage();

            //Each Person
            for(int i = 0; i < suspects.size(); i++) {

                log.appendText("Printing all of ("+suspects.get(i).getName()+"'s) details\n");
                //Name and Link Target
                Anchor linkTarget = new Anchor(suspects.get(i).getName(), paraFontBold);
                linkTarget.setName(suspects.get(i).getName());
                pdf.add(linkTarget);
                pdf.add(new Chunk("\n"));
                pdf.add(new Chunk(new LineSeparator()));

                ArrayList<ExaminedFile> files = suspects.get(i).getSolutions();

                for(int j = 0; j < files.size(); j++) {

                    //Detail Table
                    PdfPTable table = new PdfPTable(2);
                    table.addCell("File Name");
                    table.addCell(files.get(j).getFileName());
                    table.addCell("Matched File Name");
                    table.addCell(files.get(j).getMatchedFileName());
                    table.addCell("Matched File Score");
                    table.addCell(Double.toString(files.get(j).getMatchScore()));
                    table.addCell("Total Number of Matches");
                    table.addCell(Integer.toString(files.get(j).getNumberOfMatches()));
                    table.addCell("Number of Variable Matches");
                    table.addCell(Integer.toString(files.get(j).getNumVarMatches()));
                    table.addCell("Number of Comment Matches");
                    table.addCell(Integer.toString(files.get(j).getNumComMatches()));
                    table.addCell("Number of General Line Matches");
                    table.addCell(Integer.toString(files.get(j).getNumLineMatches()));
                    table.addCell("Number of Lines");
                    table.addCell(Integer.toString(files.get(j).getNumberOfLines()));
                    table.addCell("Number of Disregarded Lines");
                    table.addCell(Integer.toString(files.get(j).getNumberOfUselessLines()));

                    pdf.add(table);
                    pdf.add(new Paragraph("Original File - "+ files.get(j).getFileName(), paraFontBold));
                    pdf.add(new Paragraph(new Chunk(new LineSeparator())));

                    String[][] sFile = files.get(j).getStoredFile();
                    String[][] mFile = files.get(j).getMatchedFile();

                    printFileContents(pdf, sFile);
                    pdf.add(new Paragraph("Matched File - "+ files.get(j).getMatchedFileName(), paraFontBold));
                    pdf.add(new Paragraph(new Chunk(new LineSeparator())));
                    if(mFile != null)
                        printFileContents(pdf, mFile);
                    else
                        pdf.add(new Chunk("No Matched File"));

                    pdf.newPage();
                }

                pdf.newPage();
            }

            pdf.close();
            log.appendText("PDF Exporting Complete!\n");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void printFileContents(Document pdf, String[][] file) throws Exception {

        Paragraph p = new Paragraph();
        p.setTabSettings(new TabSettings(40f));

        for(int l = 0; l < file.length; l++) {

            for(int w = 0; w < file[l][0].length(); w++) {

                if(file[l][0].charAt(w) == 9) { // 9 is a tab character
                    p.add(Chunk.TABBING);
                }

                else
                    p.add(new Chunk(file[l][0].charAt(w)+"", pickFont(file[l][1])));

            }
            p.add(new Chunk(" "));
        }

        pdf.add(p);
    }

    private Font pickFont(String type) {

        if(type.equals("variable"))
            return new Font(Font.FontFamily.COURIER, 10.0f, Font.BOLD, BaseColor.ORANGE);
        else if(type.equals("comment"))
            return new Font(Font.FontFamily.COURIER, 10.0f, Font.BOLD, BaseColor.RED);
        else if(type.equals("line"))
            return new Font(Font.FontFamily.COURIER, 10.0f, Font.BOLD, BaseColor.MAGENTA);
        else
            return new Font(Font.FontFamily.COURIER, 10.0f, Font.NORMAL, BaseColor.BLACK);
    }

    private void suspectIterator() {

        for(int i = 0; i < suspects.size(); i++) { //For each suspect, we compare to all other suspects

            for(int j = 0; j < suspects.size(); j++) {

                if(suspects.get(i) == suspects.get(j)) { //Don't compare it to itself.
                    System.out.println(suspects.get(i)+" and "+suspects.get(j)+" Same, skipping.");
                    continue;
                }


                System.out.println(suspects.get(i)+" and "+suspects.get(j));
                //Else we will compare that persons files with the other persons
                fileIterator(suspects.get(i).getSolutions(), suspects.get(j).getSolutions());
            }
        }

        exportPDFButton.setDisable(false);
    }

    private void fileIterator(ArrayList<ExaminedFile> sFiles, ArrayList<ExaminedFile> mFiles) {

        for(int i = 0; i < sFiles.size(); i++) {

            for(int j = 0; j < mFiles.size(); j++) {

                //Compare eachfile to each other file, the lowest.
                compareAlgorithm(sFiles.get(i), mFiles.get(j));
                numFilesChecked++;
                pb.setProgress((double)numFilesChecked/(double)numFilesTotal);
                textProperty.set("[ "+numFilesChecked+" out of "+numFilesTotal+"]");
            }
        }
    }

    private void calculateTotalFiles() {

        int total = 0;

        for(int i = 0; i < suspects.size(); i++) {

            for(int j = 0; j < suspects.size(); j++) {

                if(suspects.get(i) != suspects.get(j))
                    total += suspects.get(j).getSolutions().size()*suspects.get(i).getSolutions().size();
            }
        }

        numFilesTotal = total;
    }

    private void compareAlgorithm(ExaminedFile sFile, ExaminedFile mFile) {

        log.appendText("Comparing-"+sFile+" to "+mFile+"\n");

        //Copy both files, and reset their color information.
        String[][] sString = sFile.copyStoredFile();
        String[][] mString = mFile.copyStoredFile();

        int[] matchArray = {0,0,0,0};

        String line = "";
        int beginIndex = 0;

        int counter = 0;

        for(int i = 0; i < sString.length; i++) {

            if (sString[i][0].equals("\n")) {
                //Check to see if it is a waste of time, just a brace or an ending block comment.
                if (line.matches("[\t ]*[{}][\t ]*|[\t ]*(\\*/)[\t ]*") || line.equals("") || line.matches("\\s*\t*\\s*")) {
                    line = "";
                    matchArray[USELESS_CHECK]++;
                    continue;
                }

                //Check to see if it is a variable line.
                else if(line.matches(".+\\s(.+?)(;|=).")) {

                    int matches = lineMatcher(line, mString, VARIABLE_CHECK);
                    if(matches > 0)
                        colorLine(sString, beginIndex, i, VARIABLE_CHECK);

                    matchArray[VARIABLE_CHECK] += matches;
                }

                //Check to see if it is a comment line.
                else if(line.matches(".*(//|/\\*\\*|/\\*|\\*).*")) {

                    int matches = lineMatcher(line, mString, COMMENT_CHECK);
                    if(matches > 0)
                        colorLine(sString, beginIndex, i, COMMENT_CHECK);

                    matchArray[COMMENT_CHECK] += matches;
                }

                //Check the normal line
                else {

                    int matches = lineMatcher(line, mString, LINE_CHECK);
                    if(matches > 0)
                        colorLine(sString, beginIndex, i, LINE_CHECK);

                    matchArray[LINE_CHECK] += matches;
                }

                line = "";
                beginIndex = i+1;
            }
            else {

                line += sString[i][0] + " ";
            }
        }

        double likelyhood = calculateScore(sFile, matchArray);

        if(likelyhood > sFile.getMatchScore()) {

            log.appendText("Better Match Found!\n");

            sFile.setStoredFile(sString);
            sFile.setMatchedFile(mString);
            sFile.setMatchedFileName(mFile.getFileName());
            sFile.setMatchScore(likelyhood);

            sFile.setNumVarMatches(matchArray[VARIABLE_CHECK]);
            sFile.setNumComMatches(matchArray[COMMENT_CHECK]);
            sFile.setNumLineMatches(matchArray[LINE_CHECK]);
            sFile.setNumberOfUselessLines(matchArray[USELESS_CHECK]);

            if(sFile.getMatchScore() > 50)
                sFile.setNumberOfMatches(sFile.getNumberOfMatches()+1);
        }
    }

    private double calculateScore(ExaminedFile file, int[] matches) {

        double numLines = (double)file.getNumberOfLines()-matches[USELESS_CHECK];
        double matchedLines =   (double)matches[VARIABLE_CHECK] +
                                (double)matches[COMMENT_CHECK]*1.25 +
                                (double)matches[LINE_CHECK];

        return (matchedLines / numLines) * 100.0;
    }

    private void colorLine(String[][] file, int beginIndex, int endIndex, int type) {

        String[] keywords = {"variable", "comment", "line"};
        log.appendText("Found Similar "+keywords[type]+"\n");

        for(int i = beginIndex; i < endIndex; i++) {

            file[i][1] = keywords[type];
        }
    }

    final int VARIABLE_CHECK = 0;
    final int COMMENT_CHECK = 1;
    final int LINE_CHECK = 2;
    final int USELESS_CHECK = 3;

    private int lineMatcher(String line, String[][] file, int type) {

        int matches = 0;
        int counter = 0;
        String[] words = line.replaceAll("[\t\r]", "").split(" ");

        if(words.length == 0)
            return 0;

        for(int i = 0; i < file.length; i++) {
            //System.out.println(counter++);
            if(file[i][0].replaceAll("[\t\r]", "").equals(words[0])) {

                boolean match = true;
                for(int j = 0; j < words.length; j++) {

                    if(i+j >= file.length) {
                        match = false;
                        break;
                    }

                    if(!file[i+j][0].replaceAll("[\t\r]", "").equals(words[j])) {

                        match = false;
                        break;
                    }
                }

                if(match == true) {

                    for(int j = 0; j < words.length; j++) {

                        if(type == VARIABLE_CHECK)
                            file[i+j][1] = "variable";
                        else if(type == COMMENT_CHECK)
                            file[i+j][1] = "comment";
                        else
                            file[i+j][1] = "line";
                    }
                    matches++;
                }
            }
        }
        return matches;
    }


    private void gatherSuspectsAndFiles(DirectoryChooser dirChooser, Stage stage) {

        log.appendText("Gathering All Files into Arrays...\n");
        File selectedFile = dirChooser.showDialog(stage);
        directory = selectedFile;

        File[] allFiles = selectedFile.listFiles();

        ArrayList<File> files = new ArrayList<File>(Arrays.asList(allFiles));

        while(!files.isEmpty()) {

            if(!files.get(0).isFile()) {
                files.get(0).delete();
                try {
                    FileUtils.deleteDirectory(files.get(0));
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
                continue;
            }

            File currentFile = files.get(0);
            Suspect suspect;

            int indexOfSuspect = checkSuspects(currentFile.getName().split("_")[0]);


            if(indexOfSuspect == -1) {
                suspect = new Suspect();
                suspect.setName(currentFile.getName().split("_")[0]);
                suspects.add(suspect);
            }
            else
                suspect = suspects.get(indexOfSuspect);

            try {

                ExaminedFile newFile = new ExaminedFile();
                String fullText = new String(Files.readAllBytes(Paths.get(currentFile.getAbsolutePath()))).replaceAll("[\r]","");
                String[] allLines = fullText.split("((?<=\n)|(?=\n))|([ ])");

                newFile.setFileName(currentFile.getName().split("_")[1]);
                newFile.setNumberOfLines(countLines(allLines));
                newFile.setStoredFile(parseFile(allLines));

                suspect.getSolutions().add(newFile);
            }
            catch(IOException e) {
                e.printStackTrace();
            }

            files.remove(0);
        }

        log.appendText("Ready...\n");
        calculateTotalFiles();
        beginCompare.setDisable(false);
    }

    private int checkSuspects(String name) {

        for(int i = 0; i < suspects.size(); i++) {

            if(suspects.get(i).getName().equals(name))
                return i;
        }
        return -1;
    }

    private int countLines(String[] words) {

        int count = 0;
        for(int i = 0; i < words.length; i++) {

            if(words[i].equals("\n"))
                count++;
        }
        return count;
    }
    private String[][] parseFile(String[] words) {

        String[][] rArray = new String[words.length][2];
        for(int i = 0; i < rArray.length; i++) {
            rArray[i][0] = words[i];
            rArray[i][1] = "false";
        }
        return rArray;
    }

    public static void main(String[] args) {

        launch(args);
    }
}
