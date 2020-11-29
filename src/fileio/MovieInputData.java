package fileio;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a movie, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class MovieInputData extends ShowInput {
    /**
     * Duration in minutes of a season
     */
    private final int duration;

    private List<Double> ratings;

    private int favoriteNo;

    private int views;

    public int getViews() {
        return views;
    }

    public void setViews(final int views) {
        this.views = views;
    }

    public int getFavoriteNo() {
        return favoriteNo;
    }

    public void setFavoriteNo(final int favoriteNo) {
         this.favoriteNo = favoriteNo;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public void setRatings(final List<Double> ratings) {
        this.ratings = ratings;
    }

    public MovieInputData(final String title, final ArrayList<String> cast,
                          final ArrayList<String> genres, final int year,
                          final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
        this.ratings = new ArrayList<>();
        this.favoriteNo = 0;
        this.views = 0;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "MovieInputData{" + "title= "
                + super.getTitle() + "year= "
                + super.getYear() + "duration= "
                + duration + "cast {"
                + super.getCast() + " }\n"
                + "genres {" + super.getGenres() + " }\n ";
    }
    /**
     * Adds the rating int the ratings list.
     *
     * @param username the user that made the rating
     * @param grade the grade that is given
     * @return a success message with the title of the movie, grade and user of the rating
     */
    public String addRating(final String username, final Double grade) {
        this.ratings.add(grade);
        return "success -> " + this.getTitle() + " was rated with " + grade + " by " + username;
    }
    /**
     * Computes the grade of the MovieInputData object, which is the mean of its seasons.
     *
     * @return the computed grade
     */
    public double computeGrade() {
        if (ratings.size() == 0) {
            return 0;
        }

        double sum = 0;
        for (Double d : ratings) {
            sum += d;
        }
        return sum / ratings.size();
    }
}
