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
import java.util.Optional;

/**
 * Created by Tyler Wilding on 05/06/16.
 * TA-Code-Reviewer - COSC
 */
public class Comparer extends Application {

    private ArrayList<Suspect> suspects = new ArrayList<Suspect>();
    @Override
    public void start(Stage stage) {

        Button chooseDir = new Button("Select Assignment Path...");

        DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setTitle("Select Folder containing Files to Check");

        HBox controls = new HBox(50);
        controls.setPadding(new Insets(20));
        controls.setAlignment(Pos.CENTER);
        controls.getChildren().addAll(chooseDir);

        BorderPane layout = new BorderPane();
        layout.setBottom(controls);



        chooseDir.setOnAction( e -> gatherSuspectsAndFiles(dirChooser, stage));
        System.out.println();


        Scene scene = new Scene(layout, 1000, 500);
        stage.setTitle("TA Code Reviewer - Extractor");
        stage.setScene(scene);
        stage.show();
    }

    public void gatherSuspectsAndFiles(DirectoryChooser dirChooser, Stage stage) {

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
                String fullText = new String(Files.readAllBytes(Paths.get(currentFile.getAbsolutePath()))).replaceAll("\t","");
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
    }

    public int checkSuspects(String name) {

        for(int i = 0; i < suspects.size(); i++) {

            if(suspects.get(i).getName().equals(name))
                return i;
        }
        return -1;
    }

    public int countLines(String[] words) {

        int count = 0;
        for(int i = 0; i < words.length; i++) {

            if(words[i].equals("\n"))
                count++;
        }
        return count;
    }
    public String[][] parseFile(String[] words) {

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
