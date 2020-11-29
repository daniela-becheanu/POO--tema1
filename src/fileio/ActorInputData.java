package fileio;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.Map;

/**
 * Information about an actor, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class ActorInputData {
    /**
     * actor name
     */
    private String name;
    /**
     * description of the actor's career
     */
    private String careerDescription;
    /**
     * videos starring actor
     */
    private ArrayList<String> filmography;
    /**
     * awards won by the actor
     */
    private final Map<ActorsAwards, Integer> awards;

    private int awardsNo;


    public ActorInputData(final String name, final String careerDescription,
                          final ArrayList<String> filmography,
                          final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;

        this.awardsNo = 0;
        for (Map.Entry<ActorsAwards, Integer> entry : awards.entrySet()) {
            this.awardsNo += entry.getValue();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public void setCareerDescription(final String careerDescription) {
        this.careerDescription = careerDescription;
    }

    public int getAwardsNo() {
        return awardsNo;
    }
    /**
     * Computes the grade of the ActorInputData object, which is the mean of the movies and serials
     * he played in.
     *
     * @param input the database from which the movies and serials are taken
     * @return the computed grade
     */
    public double computeGrade(final Input input) {
        double sum;
        double number;
        sum = 0;
        number = 0;

        for (MovieInputData movie : input.getMovies()) {
            if (filmography.contains(movie.getTitle()) && movie.computeGrade() != 0) {
                sum += movie.computeGrade();
                number++;
            }
        }

        for (SerialInputData serial : input.getSerials()) {
            if (filmography.contains(serial.getTitle()) && serial.computeGrade() != 0) {
                sum += serial.computeGrade();
                number++;
            }
        }

        if (number == 0) {
            return 0;
        }
        return sum / number;
    }

    @Override
    public String toString() {
        return "ActorInputData{"
                + "name='" + name + '\''
                + ", careerDescription='"
                + careerDescription + '\''
                + ", filmography=" + filmography + '}';
    }
}
