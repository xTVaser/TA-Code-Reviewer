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

    public double largestScore() {

        double largest = Double.MIN_VALUE;

        for(int i = 0; i < solutions.size(); i++) {

            if(solutions.get(i).getMatchScore() > largest)
                largest = solutions.get(i).getMatchScore();
        }

        return largest;
    }
}
