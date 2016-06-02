import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Tyler Wilding on 01/06/16.
 * TA-Code-Reviewer - COSC
 */
public class Extractor extends Application {

    Label assignmentPath = new Label();
    Label assignmentName = new Label();
    Hyperlink assignmentLink = new Hyperlink();
    TextArea log = new TextArea("Log\n");

    @Override
    public void start(Stage stage) {

        Button chooseFile = new Button("Select Zip File...");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Zip File Containing Assignments");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Zip Files", "*.zip"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));


        log.setEditable(false);
        ScrollPane scrollpane = new ScrollPane();
        scrollpane.setContent(log);
        scrollpane.setFitToWidth(true);


        VBox info = new VBox(50);
        info.setPadding(new Insets(20));
        info.setAlignment(Pos.CENTER);
        info.getChildren().addAll(assignmentPath, assignmentName, assignmentLink, scrollpane);

        chooseFile.setOnAction( e -> getFileContents(fileChooser, stage));
        assignmentLink.setOnAction( e -> getHostServices().showDocument(assignmentLink.getText()));


        HBox controls = new HBox(50);
        controls.setPadding(new Insets(20));
        controls.setAlignment(Pos.CENTER);
        controls.getChildren().addAll(chooseFile);


        BorderPane layout = new BorderPane();
        layout.setCenter(info);
        layout.setBottom(controls);

        Scene scene = new Scene(layout, 1000, 500);
        stage.setTitle("TA Code Reviewer - Extractor");
        stage.setScene(scene);
        stage.show();
    }

    public void getFileContents(FileChooser fileChooser, Stage stage) {

        File selectedFile = fileChooser.showOpenDialog(stage);
        String[] path = selectedFile.getAbsolutePath().split(Pattern.quote("/"));

        String newFolder = "/";
        for(int i = 0; i < path.length-1; i++) {

            newFolder += path[i]+"/";
        }

        String[] assignmentInfo = path[path.length-1].split("-");
        String folderName = assignmentInfo[0] + "_" + assignmentInfo[1];

        newFolder += folderName;

        File dir = new File(newFolder);
        boolean folderCreated = dir.mkdir();
        if(folderCreated)
            System.out.println("Folder Successfully Created");
        else
            System.out.println("Folder already exists.");


        try {
            ZipFile mainZip = new ZipFile(selectedFile);
            mainZip.extractAll(newFolder+"/");
        }
        catch(ZipException e) {
            e.printStackTrace();
        }

        assignmentPath.setText("Working With: "+selectedFile.getAbsolutePath());
        assignmentName.setText(folderName);
        assignmentLink.setText("https://courses.algomau.ca/moodle/mod/assign/view.php?id="+assignmentInfo[2]);

        firstPass(newFolder);
    }

    /**
     * Submissions have the following format:
     *  Name_ID_assignsubmission_file_<file name>
     * @param folderName
     */
    public void firstPass(String folderName) {

        //Unzip and Unrar all the files first, put them into directories with the name and such.
        ArrayList<File> rootFiles = new ArrayList<File>();
        getAllFiles(folderName, rootFiles, false);

        String secondPassDir = "";

        for(int i = 0; i < rootFiles.size(); i++) {

            File file = rootFiles.get(i);

            if(file.getName().matches(".+(.zip)$")) {

                log.appendText("Found Zip File: "+file.getName()+"\n");

                String name = file.getName().split("_")[0];

                File dir = new File(folderName+"/"+name);
                dir.mkdir();

                secondPassDir = dir.toString();

                try {
                    log.appendText("Extracting: "+file.getName()+"\n");
                    ZipFile zip = new ZipFile(file);
                    zip.extractAll(folderName+"/"+name+"/");
                }
                catch (ZipException e) {

                    e.printStackTrace();
                }
                log.appendText("Deleting: "+file.getName()+"\n");
                file.delete();
            }
            else if(file.getName().matches(".+(.rar)$")) {

                log.appendText("Found Rar File: "+file.getName()+"\n");

                String name = file.getName().split("_")[0];

                File dir = new File(folderName+"/"+name+"/");
                dir.mkdir();

                Archive archive = null;
                try {
                    log.appendText("Extracting: "+file.getName()+"\n");
                    archive = new Archive(new FileVolumeManager(file));
                }
                catch (RarException e) { e.printStackTrace(); }
                catch (IOException e) { e.printStackTrace(); }

                FileHeader fileHead = archive.nextFileHeader();

                while(fileHead != null) {

                    try {

                        FileOutputStream os = new FileOutputStream(dir.getAbsolutePath()+"/"+fileHead.getFileNameString().trim()+"/");
                        archive.extractFile(fileHead, os);
                        os.close();
                    }
                    catch (FileNotFoundException e) { e.printStackTrace(); }
                    catch (RarException e) { e.printStackTrace(); }
                    catch (IOException e) { e.printStackTrace(); }

                    fileHead = archive.nextFileHeader();
                }
                log.appendText("Deleting: "+file.getName()+"\n");
                file.delete();
            }
            else if(file.getName().matches(".+(.java)$")) {

                log.appendText("Found Java File: "+file.getName()+"\n");

                String[] fileDetails = file.getName().split("_");

                try {
                    log.appendText("Renaming: "+file.getName()+"\n");
                    Files.move(file.toPath(), file.toPath().resolveSibling(fileDetails[0]+"_"+fileDetails[fileDetails.length-1]), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else if(file.getName().matches(".+(.txt)$")) {

                log.appendText("Found Text File: "+file.getName()+"\n");

                String[] fileDetails = file.getName().split("_");

                try {
                    log.appendText("Renaming: "+file.getName()+"\n");
                    Files.move(file.toPath(), file.toPath().resolveSibling(fileDetails[0]+"_"+fileDetails[fileDetails.length-1].replaceAll(".txt", ".java")), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else {
                file.delete();
            }
        }

        secondPass(folderName, secondPassDir);
    }

    public void secondPass(String folderName, String newFolderName) {

        File dir = new File(folderName);
        File[] folderList = dir.listFiles();

        File newDir = new File(newFolderName);

        for(int i = 0; i < folderList.length; i++) {

            if(folderList[i].isDirectory()) {

                ArrayList<File> files = new ArrayList<File>();
                getAllFiles(folderList[i].getAbsolutePath(), files, true);

                for(int j = 0; j < files.size(); j++) {

                    File file = files.get(j);

                    if(file.getName().matches(".+(.java)$")) {

                        log.appendText("Found Java File: "+file.getName()+"\n");

                        try {
                            log.appendText("Moving and Renaming to: "+newDir.toPath().resolveSibling(folderList[i].getName()+"_"+file.getName());
                            System.out.println(dir.toPath().resolveSibling(folderList[i].getName()+"_"+file.getName()));
                            Files.move(file.toPath(), newDir.toPath().resolveSibling(folderList[i].getName()+"_"+file.getName()), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    else if(file.getName().matches(".+(.txt)$")) {

                        log.appendText("Found Text File: "+file.getName()+"\n");

                        try {
                            log.appendText("Moving and Renaming to: "+file.getName()+"\n");
                            System.out.println(dir.toPath().resolveSibling(folderList[i].getName()+"_"+file.getName().replaceAll(".txt", ".java")));
                            Files.move(file.toPath(), newDir.toPath().resolveSibling(folderList[i].getName()+"_"+file.getName().replaceAll(".txt", ".java")), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    else {
                        file.delete();
                    }
                }
                folderList[i].delete();
            }
        }
    }

    public void getAllFiles(String directory, ArrayList<File> files, Boolean searchSubDirs) {

        File dir = new File(directory);

        File[] fileList = dir.listFiles();
        for(int i = 0; i < fileList.length; i++) {

            if(fileList[i].isFile())
                files.add(fileList[i]);
            else if (fileList[i].isDirectory() && searchSubDirs)
                getAllFiles(fileList[i].getAbsolutePath(), files, searchSubDirs);
        }
    }

    public static void main(String[] args) {

        launch(args);
    }


}
