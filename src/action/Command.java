package action;

import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;

public class Command {
    public static class Movie {
        /**
         * Adds rating to a season of a movie that is already seen.
         *
         * @param title the title of the serial that is added
         * @param user the user that rates the movie
         * @param movie the movie which list the rating is added in
         * @param action the parameter which contains the grade (rating)
         * @return a message which indicates if the adding takes place
         */
        public static String addRating(final String title, final UserInputData user,
                                       final MovieInputData movie,
                                       final ActionInputData action) {
            if (user.getHistory().containsKey(title)) {
                if (!user.getRatedMovies().contains(title)) {
                    user.getRatedMovies().add(title);
                    return movie.addRating(action.getUsername(),
                            action.getGrade());
                }
                return "error -> " + movie.getTitle() + " has been already rated";
            }
            return "error -> " + movie.getTitle() + " is not seen";
        }
    }

    public static class Serial {
        /**
         * Adds rating to a season of a serial that is already seen.
         *
         * @param title the title of the serial that is added
         * @param seasonNumber the number of the season that is rated
         * @param user the user that rates the season of the serial
         * @param serial the serial which list the rating is added in
         * @param action the parameter which contains the grade (rating)
         * @return a message which indicates if the adding takes place
         */
        public static  String addRating(final String title, final int seasonNumber,
                                              final UserInputData user,
                                              final SerialInputData serial,
                                              final ActionInputData action) {
            final String seasonTitle = title + " - season " + seasonNumber;
            if (user.getHistory().containsKey(title)) {
                if (!user.getRatedMovies().contains(seasonTitle)) {
                      user.getRatedMovies().add(seasonTitle);
                    return serial.addRating(action.getUsername(),
                            action.getSeasonNumber(), action.getGrade());
                }
                return "error -> " + serial.getTitle() + " has been already rated";
            }
            return "error -> " + serial.getTitle() + " is not seen";
        }
    }
}
