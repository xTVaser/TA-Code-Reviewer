/**
 * Created by Tyler Wilding on 05/06/16.
 * TA-Code-Reviewer - COSC
 */
public class ExaminedFile {

    private String fileName;

    private String[][] originalFile;
    private String[][] matchedFile;

    private int matchScore = 0;
    private int numVarMatches = 0;
    private int numComMatches = 0;
    private int numLineMatches = 0;

    private int numberOfLines = 0;
    private int numberOfUselessLines = 0;

    public ExaminedFile() {


    }

    public String[][] getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(String[][] originalFile) {
        this.originalFile = originalFile;
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

    public int getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(int matchScore) {
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
