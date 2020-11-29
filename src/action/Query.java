package action;

import compare.CompareActors;
import compare.CompareRatingsNo;
import compare.CompareVideos;
import contain.ContainsAwards;
import contain.ContainsWords;
import fileio.Input;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.UserInputData;
import fileio.SerialInputData;
import fileio.MovieInputData;
import convert.Actor;
import convert.Video;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Query {
    public static class Actors {
        /**
         * Sorts in ascending/descending order a list of actors.
         *
         * @param actors the initial actors list that needs to be sorted
         * @param action the parameter which contains way the list needs to be sorted
         * @return a message containing a list with the sorted actors
         */
        public static String sort(final ArrayList<Actor> actors, final ActionInputData action) {
            StringBuilder message = new StringBuilder();
            actors.sort(new CompareActors());

            if (action.getSortType().equals("desc")) {
                Collections.reverse(actors);
            }

            int min = Math.min(actors.size(), action.getNumber());
            for (int i = 0; i < min; ++i) {
                message.append(actors.get(i).getName());
                message.append(", ");
            }

            if (message.length() > 2) {
                message = new StringBuilder(message.substring(0, message.length() - 2));
            }

            return message.toString();
        }
        /**
         * Lists a number of actors sorted by the mean of the movies and serials they played in.
         *
         * @param input the database which contains the information
         * @param action the parameter which contains the number of actors that need to be listed.
         * @return a message which indicates the actors sorted by their mean
         */
        public static String sortActorsAverage(final Input input, final ActionInputData action) {
            String message = "Query result: [";

            List<ActorInputData> actorsList = input.getActors().stream()
                    .filter(actor -> actor.computeGrade(input) > 0)
                    .collect(Collectors.toList());

            ArrayList<Actor> actors = new ArrayList<>();
            for (ActorInputData actor : actorsList) {
                Actor a = new Actor(actor.getName(), actor.computeGrade(input));
                actors.add(a);
            }

            message += sort(actors, action);
            message += "]";

            return message;
        }
        /**
         * Lists all the actors whose description contains all the awards mentioned in query.
         *
         * @param input the database which contains the information
         * @param action the parameter which contains way the actors need to be listed (ascending
         *               or descending)
         * @return a message which indicates the actors with all the awards
         */
        public static String sortActorsAwards(final Input input, final ActionInputData action) {
            String message = "Query result: [";
            List<ActorInputData> actorsFilterApplied  = new ArrayList<>();

            if (action.getFilters().get(action.getFilters().size() - 1) != null) {
                actorsFilterApplied = input.getActors().stream()
                        .filter(new ContainsAwards(action.getFilters()
                                .get(action.getFilters().size() - 1)))
                        .collect(Collectors.toList());
            }

            ArrayList<Actor> actors = new ArrayList<>();
            for (ActorInputData actor : actorsFilterApplied) {
                Actor a = new Actor(actor.getName(), actor.getAwardsNo());
                actors.add(a);
            }

            message += sort(actors, action);
            message += "]";

            return message;
        }
        /**
         * Lists all the actors whose description all the keywords appear.
         *
         * @param input the database which contains the information
         * @param action the parameter which contains way the actors need to be listed (ascending
         *               or descending)
         * @return a message which indicates the actors filtered
         */
        public static String sortActorsFilter(final Input input, final ActionInputData action) {
            String message = "Query result: [";

            for (ActorInputData actor : input.getActors()) {
                actor.setCareerDescription(actor.getCareerDescription().toLowerCase());
                actor.setCareerDescription(actor.getCareerDescription().replace('-', ' '));
                actor.setCareerDescription(actor.getCareerDescription().replace(',', ' '));
                actor.setCareerDescription(actor.getCareerDescription().replace('.',  ' '));
            }

            List<ActorInputData> actorsFilterApplied = input.getActors().stream()
                    .filter(new ContainsWords(action.getFilters().get(2)))
                    .collect(Collectors.toList());

            ArrayList<Actor> actors = new ArrayList<>();
            for (ActorInputData actor : actorsFilterApplied) {
                Actor a = new Actor(actor.getName(), 0);
                actors.add(a);
            }

            message += sort(actors, action);
            message += "]";

            return message;
        }
    }

    public static class Videos {
        /**
         * Sorts in ascending/descending order a list of videos.
         *
         * @param videos the initial videos list that needs to be sorted
         * @param action the parameter which contains way the list needs to be sorted
         * @return a message containing a list with the sorted videos
         */
        public static String sort(final ArrayList<Video> videos, final ActionInputData action) {
            StringBuilder message = new StringBuilder();
            videos.sort(new CompareVideos());

            if (action.getSortType().equals("desc")) {
                Collections.reverse(videos);
            }

            int min = Math.min(videos.size(), action.getNumber());
            for (int i = 0; i < min; ++i) {
                message.append(videos.get(i).getTitle());
                message.append(", ");
            }

            if (message.length() > 2) {
                message = new StringBuilder(message.substring(0, message.length() - 2));
            }

            return message.toString();
        }
        public static class Movies {
            /**
             * Filters a list of movies.
             *
             * @param input the database which contains the information
             * @param action the parameter which contains the filters
             * @return a message containing a list with the filtered movies
             */
            public static List<MovieInputData> moviesFiltered(final ActionInputData action,
                                                               final Input input) {

                List<MovieInputData> moviesFiltered = input.getMovies();

                if (action.getFilters().get(0).get(0) != null) {
                    moviesFiltered = moviesFiltered.stream()
                            .filter(movie -> Integer.toString(movie.getYear())
                                    .equals(action.getFilters().get(0).get(0)))
                            .collect(Collectors.toList());
                }

                if (action.getFilters().get(1).get(0) != null) {
                    moviesFiltered = moviesFiltered.stream()
                            .filter(movie -> movie.getGenres()
                                    .contains(action.getFilters().get(1).get(0)))
                            .collect(Collectors.toList());
                }

                return moviesFiltered;
            }
            /**
             * Lists a number of movies sorted by their rating.
             *
             * @param input the database which contains the information
             * @param action the parameter which contains the number of movies that need to be
             *               listed
             * @return a message which indicates the movies with the highest/lowest rating
             */
            public static String sortRating(final Input input, final ActionInputData action) {
                String message = "Query result: [";

                List<MovieInputData> moviesFiltered = moviesFiltered(action, input);
                ArrayList<Video> videos = new ArrayList<>();
                moviesFiltered = moviesFiltered.stream()
                        .filter(movie -> movie.computeGrade() > 0)
                        .collect(Collectors.toList());

                for (MovieInputData movie : moviesFiltered) {
                    Video video = new Video(movie.getTitle(), movie.computeGrade(), 0,
                            movie.getTitle());
                    videos.add(video);
                }

                message += sort(videos, action);
                message += "]";

                return message;
            }
            /**
             * Lists a number of movies sorted by their numbers of occurrence in users' favorite
             * lists.
             *
             * @param input the database which contains the information
             * @param action the parameter which contains the number of movies that need to be
             *               listed
             * @return a message which indicates the movies that appear the most/least in users'
             * favorite lists
             */
            public static String sortFavorite(final Input input, final ActionInputData action) {
                String message = "Query result: [";
                ArrayList<Video> videos = new ArrayList<>();

                for (MovieInputData movie : input.getMovies()) {
                    movie.setFavoriteNo(0);
                    for (UserInputData user : input.getUsers()) {
                        if (user.getFavoriteMovies().contains(movie.getTitle())) {
                            movie.setFavoriteNo(movie.getFavoriteNo() + 1);
                        }
                    }
                }

                List<MovieInputData> moviesFiltered = moviesFiltered(action, input);

                moviesFiltered = moviesFiltered.stream()
                        .filter(serial -> serial.getFavoriteNo() > 0)
                        .collect(Collectors.toList());

                for (MovieInputData movie : moviesFiltered) {
                    Video video = new Video(movie.getTitle(), 0, movie.getFavoriteNo(),
                            movie.getTitle());
                    videos.add(video);
                }

                message = message + sort(videos, action);
                message += "]";

                return message;
            }
            /**
             * Lists a number of movies sorted by their duration.
             *
             * @param input the database which contains the information
             * @param action the parameter which contains the number movies that need to be
             *               listed
             * @return a message which indicates the longest/shortest serials
             */
            public static String sortLongest(final Input input, final ActionInputData action) {
                String message = "Query result: [";
                ArrayList<Video> videos = new ArrayList<>();

                List<MovieInputData> moviesFiltered = moviesFiltered(action, input);

                for (MovieInputData movie : moviesFiltered) {
                    Video video = new Video(movie.getTitle(), 0, movie.getDuration(),
                            movie.getTitle());
                    videos.add(video);
                }

                message += sort(videos, action);
                message += "]";

                return message;
            }
            /**
             * Lists a number of movies sorted by their views.
             *
             * @param input the database which contains the information
             * @param action the parameter which contains the number of movies that need to be
             *               listed
             * @return a message which indicates the most/least viewed movies
             */
            public static String sortMostViewed(final Input input, final ActionInputData action) {
                String message = "Query result: [";

                ArrayList<Video> videos = new ArrayList<>();
                List<MovieInputData> moviesFiltered = moviesFiltered(action, input);

                for (MovieInputData movie : moviesFiltered) {
                    movie.setViews(0);
                    for (UserInputData user : input.getUsers()) {
                        if (user.getHistory().containsKey(movie.getTitle())) {
                            movie.setViews(movie.getViews() + user.getHistory()
                                    .get(movie.getTitle()));
                        }
                    }
                }

                moviesFiltered = moviesFiltered.stream()
                        .filter(movie -> movie.getViews() > 0)
                        .collect(Collectors.toList());

                for (MovieInputData movie : moviesFiltered) {
                    Video video = new Video(movie.getTitle(), 0, movie.getViews(),
                            movie.getTitle());
                    videos.add(video);
                }

                message += sort(videos, action);
                message += "]";

                return message;
            }
        }
        public static class Serials {
            /**
             * Filters a list of serials.
             *
             * @param input the database which contains the information
             * @param action the parameter which contains the filters
             * @return a message containing a list with the filtered serials
             */
            public static List<SerialInputData> serialsFiltered(final ActionInputData action,
                                                                 final Input input) {
                List<SerialInputData> serialsFiltered = input.getSerials();
                if (action.getFilters().get(0).get(0) != null) {

                    serialsFiltered = input.getSerials().stream()
                            .filter(movie -> Integer.toString(movie.getYear())
                                    .equals(action.getFilters().get(0).get(0)))
                            .collect(Collectors.toList());
                }

                if (action.getFilters().get(1).get(0) != null) {
                    serialsFiltered = serialsFiltered.stream()
                            .filter(serial -> serial.getGenres()
                                    .contains(action.getFilters().get(1).get(0)))
                            .collect(Collectors.toList());
                }

                return serialsFiltered;
            }
            /**
             * Lists a number of serials sorted by their rating.
             *
             * @param input the database which contains the information
             * @param action the parameter which contains the number of serials that need to be
             *               listed
             * @return a message which indicates the serials with the highest/lowest rating
             */
            public static String sortRating(final Input input, final ActionInputData action) {
                String message = "Query result: [";
                ArrayList<Video> videos = new ArrayList<>();

                List<SerialInputData> serialsFiltered = serialsFiltered(action, input);

                serialsFiltered = serialsFiltered.stream()
                        .filter(serial -> serial.computeGrade() > 0)
                        .collect(Collectors.toList());

                for (SerialInputData serial : serialsFiltered) {
                    Video video = new Video(serial.getTitle(), serial.computeGrade(), 0,
                            serial.getTitle());
                    videos.add(video);
                }

                message += sort(videos, action);
                message += "]";

                return message;
            }
            /**
             * Lists a number of serials sorted by their numbers of occurrence in users' favorite
             * lists.
             *
             * @param input the database which contains the information
             * @param action the parameter which contains the number of serials that need to be
             *               listed
             * @return a message which indicates the serials that appear the most/least in users'
             * favorite lists
             */
            public static String sortFavorite(final Input input, final ActionInputData action) {
                String message = "Query result: [";

                ArrayList<Video> videos = new ArrayList<>();
                List<SerialInputData> serialsFiltered = serialsFiltered(action, input);

                for (SerialInputData serial : serialsFiltered) {
                    serial.setFavoriteNo(0);
                    for (UserInputData user : input.getUsers()) {
                        if (user.getFavoriteMovies().contains(serial.getTitle())) {
                            serial.setFavoriteNo(serial.getFavoriteNo() + 1);
                        }
                    }
                }

                serialsFiltered = serialsFiltered.stream()
                        .filter(serial -> serial.getFavoriteNo() > 0)
                        .collect(Collectors.toList());

                for (SerialInputData serial : serialsFiltered) {
                    Video video = new Video(serial.getTitle(), 0, serial.getFavoriteNo(),
                            serial.getTitle());
                    videos.add(video);
                }

                message += sort(videos, action);
                message += "]";

                return message;
            }
            /**
             * Lists a number of serials sorted by their duration.
             *
             * @param input the database which contains the information
             * @param action the parameter which contains the number of serials that need to be
             *               listed
             * @return a message which indicates the longest/shortest serials
             */
            public static String sortLongest(final Input input, final ActionInputData action) {
                String message = "Query result: [";
                ArrayList<Video> videos = new ArrayList<>();

                List<SerialInputData> serialsFiltered = serialsFiltered(action, input);

                for (SerialInputData serial : serialsFiltered) {
                    Video video = new Video(serial.getTitle(), 0, serial.getDuration(),
                            serial.getTitle());
                    videos.add(video);
                }

                message += sort(videos, action);
                message += "]";

                return message;
            }
            /**
             * Lists a number of serials sorted by their views.
             *
             * @param input the database which contains the information
             * @param action the parameter which contains the number of serials that need to be
             *               listed
             * @return a message which indicates the most/least viewed serials
             */
            public static String sortMostViewed(final Input input, final ActionInputData action) {
                String message = "Query result: [";
                ArrayList<Video> videos = new ArrayList<>();

                List<SerialInputData> serialsFiltered = serialsFiltered(action, input);

                for (SerialInputData serial : serialsFiltered) {
                    serial.setViews(0);
                    for (UserInputData user : input.getUsers()) {
                        if (user.getHistory().containsKey(serial.getTitle())) {
                            serial.setViews(serial.getViews() + user.getHistory()
                                    .get(serial.getTitle()));
                        }
                    }
                }

                serialsFiltered = serialsFiltered.stream()
                        .filter(serial -> serial.getViews() > 0)
                        .collect(Collectors.toList());

                for (SerialInputData serial : serialsFiltered) {
                    Video video = new Video(serial.getTitle(), 0, serial.getViews(),
                            serial.getTitle());
                    videos.add(video);
                }

                message += sort(videos, action);
                message += "]";

                return message;
            }
        }
    }

    public static class Users {
        /**
         * Sorts in ascending or descending order a list.
         *
         * @param users the list of users that needs to be sorted
         * @param action the parameter which contains the number of users that need to be sorted
         * @return a message which indicates the most/least active users
         */
        public static String sort(final ArrayList<UserInputData> users,
                                  final ActionInputData action) {
            users.sort(new CompareRatingsNo());
            StringBuilder message = new StringBuilder();

            if (action.getSortType().equals("desc")) {
                Collections.reverse(users);
            }

            int min = Math.min(action.getNumber(), users.size());
            for (int i = 0; i < min; ++i) {
                message.append(users.get(i).getUsername());
                message.append(", ");
            }

            if (message.length() > 2) {
                message = new StringBuilder(message.substring(0, message.length() - 2));
            }

            return message.toString();
        }
        /**
         * Lists a number of users sorted by the number of ratings given.
         *
         * @param input the database which contains the information
         * @param action the parameter which contains the number of users that need to be listed
         * @return a message which indicates the most/least active users
         */
        public static String sortRatingsNo(final Input input, final ActionInputData action) {
            String message = "Query result: [";
            List<UserInputData> usersFiltered;

            usersFiltered = input.getUsers().stream()
                    .filter(user -> user.getRatedMovies().size() > 0)
                    .collect(Collectors.toList());

            message += sort((ArrayList<UserInputData>) usersFiltered, action);
            message += "]";

            return message;
        }
    }
}
