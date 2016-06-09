/**
 * Created by Tyler Wilding on 05/06/16.
 * TA-Code-Reviewer - COSC
 */
public class ExaminedFile {

    private String fileName;
    private String matchedFileName;

    private String[][] storedFile;
    private String[][] matchedFile;

    private double matchScore = 0;
    private int numberOfMatches = 0;

    private int numVarMatches = 0;
    private int numComMatches = 0;
    private int numLineMatches = 0;

    private int numberOfLines = 0;
    private int numberOfUselessLines = 0;

    public ExaminedFile() {

    }

    public int getNumberOfMatches() {
        return numberOfMatches;
    }

    public void setNumberOfMatches(int numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
    }

    public String getMatchedFileName() {
        return matchedFileName;
    }

    public void setMatchedFileName(String matchedFileName) {
        this.matchedFileName = matchedFileName;
    }

    public String[][] copyStoredFile() {

        String[][] copy = new String[storedFile.length][2];

        for(int i = 0 ; i < copy.length; i++) {

            copy[i][0] = storedFile[i][0];
            copy[i][1] = "false";
        }

        return copy;
    }

    public String[][] getStoredFile() {
        return storedFile;
    }

    public void setStoredFile(String[][] originalFile) {
        this.storedFile = originalFile;
    }

    public int getNumberOfUselessLines() {
        return numberOfUselessLines;
    }

    public void setNumberOfUselessLines(int numberOfUselessLines) {
        this.numberOfUselessLines = numberOfUselessLines;
    }

    public String[][] getMatchedFile() {
        return matchedFile;
    }

    public void setMatchedFile(String[][] matchedFile) {
        this.matchedFile = matchedFile;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }

    public int getNumVarMatches() {
        return numVarMatches;
    }

    public void setNumVarMatches(int numVarMatches) {
        this.numVarMatches = numVarMatches;
    }

    public int getNumComMatches() {
        return numComMatches;
    }

    public void setNumComMatches(int numComMatches) {
        this.numComMatches = numComMatches;
    }

    public int getNumLineMatches() {
        return numLineMatches;
    }

    public void setNumLineMatches(int numLineMatches) {
        this.numLineMatches = numLineMatches;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String toString() {

        return fileName;
    }
}
