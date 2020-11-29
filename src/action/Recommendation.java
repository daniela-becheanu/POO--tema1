package action;

import compare.CompareVideos;
import fileio.Input;
import fileio.SerialInputData;
import fileio.ShowInput;
import fileio.UserInputData;
import fileio.MovieInputData;
import fileio.ActionInputData;
import convert.Video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public final class Recommendation {
    private Recommendation() {
    }
    /**
     * Finds the videos from a list of videos that are not seen by the user.
     *
     * @param input the database which contains the movies and serials where the search occurs
     * @param user the user which the search is for
     * @return the list of videos not seen by the user
     */
    public static List<ShowInput> notSeen(final Input input, final UserInputData user) {
        List<ShowInput> result = input.getMovies().stream()
                .filter(movie -> !user.getHistory().containsKey(movie.getTitle()))
                .collect(Collectors.toList());

        List<SerialInputData>  serialsNotSeen = input.getSerials().stream()
                .filter(movie -> !user.getHistory().containsKey(movie.getTitle()))
                .collect(Collectors.toList());
        result.addAll(serialsNotSeen);
        return result;
    }

    public static class BasicUser {
        /**
         * Finds the title of the video from database which is not already seen by the user.
         *
         * @param input the database which contains the information
         * @param user the user which the recommendation is applied for
         * @return a message with the corresponding video or if the recommendation cannot be
         * applied
         */
        public static String standard(final Input input, final UserInputData user) {
            for (MovieInputData movie : input.getMovies()) {
                if (!user.getHistory().containsKey(movie.getTitle())) {
                    return "StandardRecommendation result: " + movie.getTitle();
                }
            }

            for (SerialInputData serial : input.getSerials()) {
                if (!user.getHistory().containsKey(serial.getTitle())) {
                    return "StandardRecommendation result: " + serial.getTitle();
                }
            }

            return "StandardRecommendation cannot be applied!";
        }
        /**
         * Finds the title of the video with the biggest grade.
         *
         * @param input the database which contains the information
         * @param user the user which the recommendation is applied for
         * @return a message with the corresponding video or if the recommendation cannot be
         * applied
         */
        public static String bestUnseen(final Input input, final UserInputData user) {
            String message;
            List<Video> videos = new ArrayList<>();

            for (MovieInputData movie : input.getMovies()) {
                if (!user.getHistory().containsKey(movie.getTitle())) {
                    Video video = new Video(movie.getTitle(), -movie.computeGrade(),
                            input.getMovies().indexOf(movie), "");
                    videos.add(video);
                }
            }
            for (SerialInputData serial : input.getSerials()) {
                if (!user.getHistory().containsKey(serial.getTitle())) {
                    Video video = new Video(serial.getTitle(), -serial.computeGrade(),
                            input.getSerials().indexOf(serial), "");
                    videos.add(video);
                }
            }

            videos.sort(new CompareVideos());

            if (videos.size() == 0) {
                return "BestRatedUnseenRecommendation cannot be applied!";
            }

            message = "BestRatedUnseenRecommendation result: " + videos.get(0).getTitle();

            return message;
        }
    }

    public static class PremiumUser {
        /**
         * Finds the title of the video which is the most popular, meaning it is the most viewed.
         *
         * @param input the database which contains the information
         * @param user the user which the recommendation is applied for
         * @return a message with the corresponding video or if the recommendation cannot be
         * applied
         */
        public static String popular(final Input input, final UserInputData user) {
            if (!user.getSubscriptionType().equals("PREMIUM")) {
                return "PopularRecommendation cannot be applied!";
            }

            Map<String, Integer> genres;
            genres = new HashMap<>();

            for (MovieInputData movie : input.getMovies()) {
                for (String s : movie.getGenres()) {
                    if (genres.containsKey(s)) {
                        genres.put(s, genres.get(s) - 1);
                    } else {
                        genres.put(s, -1);
                    }
                }
            }
            for (SerialInputData serial : input.getSerials()) {
                for (String s : serial.getGenres()) {
                    if (genres.containsKey(s)) {
                        genres.put(s, genres.get(s) - 1);
                    } else {
                        genres.put(s, -1);
                    }
                }
            }

            Map<String, Integer> sorted = genres.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
                for (MovieInputData movie : input.getMovies()) {
                    if (!user.getHistory().containsKey(movie.getTitle())
                    && movie.getGenres().contains(entry.getKey())) {
                        return "PopularRecommendation result: " + movie.getTitle();
                    }
                }
                for (SerialInputData serial : input.getSerials()) {
                    if (!user.getHistory().containsKey(serial.getTitle())
                            && serial.getGenres().contains(entry.getKey())) {
                        return "PopularRecommendation result: " + serial.getTitle();
                    }
                }

            }
            return "PopularRecommendation cannot be applied!";
        }
        /**
         * Finds the title of the video which is the most common in the favorite lists of al the
         * users. The second criterion is the order of occurrence in database.
         *
         * @param input the database which contains the information
         * @param user the user which the recommendation is applied for
         * @return a message with the corresponding video or if the recommendation cannot be
         * applied
         */
        public static String favorite(final Input input, final UserInputData user) {
            if (!user.getSubscriptionType().equals("PREMIUM")) {
                return "FavoriteRecommendation cannot be applied!";
            }

            List<Video> videos = new ArrayList<>();
            int favNo;
            List<ShowInput> notSeen = notSeen(input, user);

            for (ShowInput s : notSeen) {
                favNo = 0;
                for (UserInputData u : input.getUsers()) {
                    if (u.getFavoriteMovies().contains(s.getTitle())) {
                        favNo++;
                    }
                }
                Video video;
                if (s instanceof MovieInputData) {
                    MovieInputData movie = (MovieInputData) s;
                    video = new Video(s.getTitle(), -favNo, input.getMovies().indexOf(movie), "");
                } else {
                    SerialInputData serial = (SerialInputData) s;
                    video = new Video(s.getTitle(), -favNo, input.getSerials().indexOf(serial),
                            "");
                }
                videos.add(video);
            }

            videos.sort(new CompareVideos());

            if (videos.size() == 0) {
                return "FavoriteRecommendation cannot be applied!";
            }

            return "FavoriteRecommendation result: " + videos.get(0).getTitle();
        }
        /**
         * Searches for all the unseen videos of a user of a specific genre.
         *
         * @param input the database which contains the information
         * @param action the parameter which contains the genre
         * @param user the user which the recommendation is applied for
         * @return a message with the titles of all the corresponding videos or if the
         * recommendation cannot be applied
         */
        public static String search(final Input input, final ActionInputData action,
                                    final UserInputData user) {
            if (!user.getSubscriptionType().equals("PREMIUM")) {
                return "SearchRecommendation cannot be applied!";
            }

            StringBuilder message = new StringBuilder("SearchRecommendation result: [");
            List<Video> videos = new ArrayList<>();
            ArrayList<ShowInput> notSeen = (ArrayList<ShowInput>) notSeen(input, user);

            notSeen = (ArrayList<ShowInput>) notSeen.stream().filter(s -> s.getGenres()
                    .contains(action.getGenre())).collect(Collectors.toList());

            for (ShowInput s : notSeen) {
                if (s instanceof MovieInputData) {
                    Video video = new Video(s.getTitle(), ((MovieInputData) s).computeGrade(), 0,
                            s.getTitle());
                    videos.add(video);

                }
                if (s instanceof SerialInputData) {
                    Video video = new Video(s.getTitle(), ((SerialInputData) s).computeGrade(), 0,
                            s.getTitle());
                    videos.add(video);
                }
            }

            videos.sort(new CompareVideos());

            for (Video video : videos) {
                message.append(video.getTitle()).append(", ");
            }

            if (videos.size() == 0) {
                return "SearchRecommendation cannot be applied!";
            }
            message = new StringBuilder(message.substring(0, message.length() - 2));
            message.append("]");
            return message.toString();

        }

    }

}
