package fileio;

import entertainment.Season;

import java.util.ArrayList;

/**
 * Information about a tv show, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class SerialInputData extends ShowInput {
    /**
     * Number of seasons
     */
    private final int numberOfSeasons;
    /**
     * Season list
     */
    private final ArrayList<Season> seasons;

    private int favoriteNo; //mn

    private int views; //mn

    private int duration; //mn

    public SerialInputData(final String title, final ArrayList<String> cast,
                           final ArrayList<String> genres,
                           final int numberOfSeasons, final ArrayList<Season> seasons,
                           final int year) {
        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
        this.favoriteNo = 0;
        this.duration = 0;
        for (Season s : this.getSeasons()) {
            this.duration += s.getDuration();
        }
        this.views = 0;
    }

    public int getFavoriteNo() {
        return favoriteNo;
    }

    public void setFavoriteNo(final int favoriteNo) {
        this.favoriteNo = favoriteNo;
    }

    public int getDuration() {
        return duration;
    }

    public int getNumberSeason() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public int getViews() {
        return views;
    }

    public void setViews(final int views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return "SerialInputData{" + " title= "
                + super.getTitle() + " " + " year= "
                + super.getYear() + " cast {"
                + super.getCast() + " }\n" + " genres {"
                + super.getGenres() + " }\n "
                + " numberSeason= " + numberOfSeasons
                + ", seasons=" + seasons + "\n\n" + '}';
    }
    /**
     * Adds the rating of the season in the season's list of ratings.
     *
     * @param username the user that made the rating
     * @param seasonNumber the number of the season for which the rating is made
     * @param grade the grade that is given
     * @return a success message with the title of the serial, grade and user of the rating
     */
    public String addRating(final String username, final int seasonNumber, final Double grade) {
        seasons.get(seasonNumber - 1).getRatings().add(grade);
        return "success -> " + getTitle() + " was rated with " + grade + " by " + username;
    }
    /**
     * Computes the grade of the SerialInputData object, which is the mean of its seasons.
     *
     * @return the computed grade
     */
    public double computeGrade() {
        double sum;

        sum = 0;
        for (Season s : seasons) {
            sum += s.getMeanRatings();
        }

        if (sum == 0) {
            return 0;
        }
        return sum / seasons.size();
    }
}
