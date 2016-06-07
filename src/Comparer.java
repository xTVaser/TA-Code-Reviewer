import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Tyler Wilding on 05/06/16.
 * TA-Code-Reviewer - COSC
 */
public class Comparer extends Application {

    private ArrayList<Suspect> suspects = new ArrayList<Suspect>();

    private Button beginCompare = new Button("Begin Comparison");

    @Override
    public void start(Stage stage) {

        beginCompare.setDisable(true);
        Button chooseDir = new Button("Select Assignment Path...");

        DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setTitle("Select Folder containing Files to Check");

        HBox controls = new HBox(50);
        controls.setPadding(new Insets(20));
        controls.setAlignment(Pos.CENTER);
        controls.getChildren().addAll(chooseDir, beginCompare);

        BorderPane layout = new BorderPane();
        layout.setBottom(controls);



        chooseDir.setOnAction( e -> gatherSuspectsAndFiles(dirChooser, stage));
        beginCompare.setOnAction( e -> suspectIterator());
        System.out.println();


        Scene scene = new Scene(layout, 1000, 500);
        stage.setTitle("TA Code Reviewer - Extractor");
        stage.setScene(scene);
        stage.show();
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

        System.out.println("Done suspects");
    }

    private void fileIterator(ArrayList<ExaminedFile> sFiles, ArrayList<ExaminedFile> mFiles) {

        for(int i = 0; i < sFiles.size(); i++) {

            for(int j = 0; j < mFiles.size(); j++) {

                //Compare eachfile to each other file, the lowest.
                compareAlgorithm(sFiles.get(i), mFiles.get(j));

            }
        }
    }

    private void compareAlgorithm(ExaminedFile sFile, ExaminedFile mFile) {

        //We build the line, and then compare to all other built lines.
        //First analyze the first line, if its a variable, compare the lines with the variable checker method.
        //If its a comment line, compare with all other single comment lines.
        //Finally if it is a normal line, we just compare with all other lines simply no pattern matching required.

        System.out.println("Comparing-"+sFile+" to "+mFile);

        String[][] sString = sFile.getOriginalFile();
        String[][] mString = mFile.getOriginalFile();

        String line = "";

        for(int i = 0; i < sString.length; i++) {

            if (sString[i][0].equals("\n")) {

                //Check to see if it is a waste of time, just a brace or an ending block comment.
                if (line.matches("[\t ]*[{}][\t ]*|[\t ]*(\\*/)[\t ]*")) {
                    line = "";
                    sFile.setNumberOfUselessLines(sFile.getNumberOfUselessLines()+1);
                    continue;
                }

                //Check to see if it is a variable line.
                else if(line.matches(".+\\s(.+?)(;|=).")) {

                    int varMatches = variableCheck(line, mString);
                    sFile.setNumVarMatches(sFile.getNumVarMatches()+varMatches);
                }

                //Check to see if it is a comment line.
                else if(line.matches(".*(//|/\\*\\*|/\\*|\\*).*")) {

                    int comMatches = commentCheck(line, mString);
                    sFile.setNumComMatches(sFile.getNumComMatches()+comMatches);
                }

                //Check the normal line
                else {

                    int lineMatches = lineCheck(line, mString);
                    sFile.setNumLineMatches(sFile.getNumLineMatches()+lineMatches);
                }

                line = "";
            }
            else {

                line += sString[i][0] + " ";
            }
        }
    }

    //Simplify these 3 methods below into one if further modifications are never needed

    private int lineCheck(String line, String[][] file) {

        int lineMatches = 0;
        String[] words = line.split(" ");

        for(int i = 0; i < file.length; i++) {

            if(file[i][0].equals(words[0])) {

                boolean match = true;
                for(int j = 0; j < words.length; j++) {

                    if(!file[i+j][0].equals(words[j]))
                        match = false;
                }

                if(match == true) {

                    for(int j = 0; j < words.length; j++) {

                        file[i+j][1] = "true";
                    }
                    lineMatches++;
                }
            }
        }


        return lineMatches;
    }

    private int commentCheck(String line, String[][] file) {

        int comMatches = 0;
        String[] words = line.replaceAll("[\t\r]", "").split(" ");

        for(int i = 0; i < file.length; i++) {

            if(file[i][0].replaceAll("[\t\r]", "").equals(words[0])) {

                boolean match = true;
                for(int j = 0; j < words.length; j++) {

                    if(!file[i+j][0].replaceAll("[\t\r]", "").equals(words[j]))
                        match = false;
                }

                if(match == true) {

                    for(int j = 0; j < words.length; j++) {

                        file[i+j][1] = "true";
                    }
                    comMatches++;
                }
            }
        }
        return comMatches;
    }

    private int variableCheck(String line, String[][] file) {

        int varMatches = 0;
        String[] words = line.split(" ");

        for(int i = 0; i < file.length; i++) {

            if(file[i][0].equals(words[0])) {

                boolean match = true;
                for(int j = 0; j < words.length; j++) {

                    if(!file[i+j][0].equals(words[j]))
                        match = false;
                }

                if(match == true) {

                    for(int j = 0; j < words.length; j++) {

                        file[i+j][1] = "true";
                    }
                    varMatches++;
                }
            }
        }

        return varMatches;
    }


    private void gatherSuspectsAndFiles(DirectoryChooser dirChooser, Stage stage) {

        File selectedFile = dirChooser.showDialog(stage);

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
                newFile.setOriginalFile(parseFile(allLines));

                suspect.getSolutions().add(newFile);
            }
            catch(IOException e) {
                e.printStackTrace();
            }

            files.remove(0);
        }

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
