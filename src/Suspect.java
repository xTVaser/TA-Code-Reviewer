import java.util.ArrayList;

/**
 * Created by Tyler Wilding on 05/06/16.
 * TA-Code-Reviewer - COSC
 */
public class Suspect {

    private String name;
    private ArrayList<ExaminedFile> solutions = new ArrayList<ExaminedFile>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ExaminedFile> getSolutions() {
        return solutions;
    }

    public void setSolutions(ArrayList<ExaminedFile> solutions) {
        this.solutions = solutions;
    }

    public String toString() {

        return name;
    }
}
