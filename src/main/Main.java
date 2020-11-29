package main;

import action.Command;
import action.Query;
import action.Recommendation;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;

import fileio.Writer;
import fileio.Input;
import fileio.InputLoader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import fileio.ActionInputData;
import fileio.UserInputData;
import fileio.SerialInputData;
import fileio.MovieInputData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation

        for (ActionInputData action : input.getCommands()) {
            String message = "";
            if (action.getActionType().equals("command")) {
                for (UserInputData user : input.getUsers()) {
                    if (user.getUsername().equals(action.getUsername())) {
                        if (action.getType().equals("favorite")) {
                            message = user.addFavorite(action.getTitle());
                        }
                        if (action.getType().equals("rating")) {
                            for (MovieInputData movie : input.getMovies()) {
                                if (movie.getTitle().equals(action.getTitle())) {
                                    message = Command.Movie.addRating(movie.getTitle(), user,
                                            movie, action);
                                }
                            }
                            for (SerialInputData serial : input.getSerials()) {
                                if (serial.getTitle().equals(action.getTitle())) {
                                    message = Command.Serial.addRating(serial.getTitle(),
                                            action.getSeasonNumber(), user, serial, action);
                                }
                            }
                        }
                        if (action.getType().equals("view")) {
                            message = user.addView(action.getTitle());
                        }
                    }
                }
            }
            if (action.getActionType().equals("query")) {
                if (action.getObjectType().equals("actors")) {
                    message = switch (action.getCriteria()) {
                        case "average" -> Query.Actors.sortActorsAverage(input, action);
                        case "awards" -> Query.Actors.sortActorsAwards(input, action);
                        default -> Query.Actors.sortActorsFilter(input, action);
                    };
                }
                if (action.getObjectType().equals("movies")) {
                    message = switch (action.getCriteria()) {
                        case "ratings" -> Query.Videos.Movies.sortRating(input, action);
                        case "favorite" -> Query.Videos.Movies.sortFavorite(input, action);
                        case "longest" -> Query.Videos.Movies.sortLongest(input, action);
                        default -> Query.Videos.Movies.sortMostViewed(input, action);
                    };
                }
                if (action.getObjectType().equals("shows")) {
                    message = switch (action.getCriteria()) {
                        case "ratings" -> Query.Videos.Serials.sortRating(input, action);
                        case "favorite" -> Query.Videos.Serials.sortFavorite(input, action);
                        case "longest" -> Query.Videos.Serials.sortLongest(input, action);
                        default -> Query.Videos.Serials.sortMostViewed(input, action);
                    };
                }
                if (action.getObjectType().equals("users")) {
                    message = Query.Users.sortRatingsNo(input, action);
                }
            }
            if (action.getActionType().equals("recommendation")) {
                //POT SA PUN SI CONSTANTA RECOMMENDATION
                for (UserInputData user : input.getUsers()) {
                    if (user.getUsername().equals(action.getUsername())) {
                        message = switch (action.getType()) {
                            case "standard" -> Recommendation.BasicUser
                                    .standard(input, user);
                            case "best_unseen" -> Recommendation.BasicUser
                                    .bestUnseen(input, user);
                            case "favorite" -> Recommendation.PremiumUser
                                    .favorite(input, user);
                            case "popular" -> Recommendation.PremiumUser
                                    .popular(input, user);
                            default -> Recommendation.PremiumUser
                                    .search(input, action, user);
                        };
                    }
                }
            }
            JSONObject jsonObj = fileWriter.writeFile(action.getActionId(), " ", message);
            arrayResult.add(jsonObj);
        }

        fileWriter.closeJSON(arrayResult);
    }
}
