/**
 * Created by Tyler Wilding on 05/06/16.
 * TA-Code-Reviewer - COSC
 */
public class ExaminedFile {

    private String fileName;

    private String[][] originalFile;
    private String[][] matchedFile;

    private int matchScore;
    private int numberOfMatches;

    private int numberOfLines;

    public ExaminedFile() {


    }

    public String[][] getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(String[][] originalFile) {
        this.originalFile = originalFile;
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

    public int getNumberOfMatches() {
        return numberOfMatches;
    }

    public void setNumberOfMatches(int numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
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

        return fileName+" "+originalFile[0][0]+"..."+originalFile[originalFile.length-1][0];
    }
}
