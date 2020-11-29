package fileio;

import java.util.ArrayList;

import java.util.Map;

/**
 * Information about an user, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class UserInputData {
    /**
     * User's username
     */
    private final String username;
    /**
     * Subscription Type
     */
    private final String subscriptionType;
    /**
     * The history of the movies seen
     */
    private final Map<String, Integer> history;
    /**
     * Movies added to favorites
     */
    private final ArrayList<String> favoriteMovies;

    private final ArrayList<String> ratedMovies;

    public UserInputData(final String username, final String subscriptionType,
                         final Map<String, Integer> history,
                         final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.favoriteMovies = favoriteMovies;
        this.history = history;
        this.ratedMovies = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public ArrayList<String> getRatedMovies() { //mn
        return ratedMovies;
    }

    @Override
    public String toString() {
        return "UserInputData{" + "username='"
                + username + '\'' + ", subscriptionType='"
                + subscriptionType + '\'' + ", history="
                + history + ", favoriteMovies="
                + favoriteMovies + '}';
    }
    /**
     * Adds a new video (movie or serial) in the favorite Arraylist if it didn't already contain it
     * and the history contains it.
     * If the history doesn't contain the title (it was not seen) or the favoriteMovies already
     * contained it, errors occur.
     *
     * @param title the title of the video which needs to be added in the favoriteMovies list
     * @return a string with the confirmation / disproof of adding
     */
    public String addFavorite(final String title) {
        for (Map.Entry<String, Integer> entry : history.entrySet()) {
            if (entry.getKey().equals(title)) {
                if (favoriteMovies.contains(entry.getKey())) {
                    return "error -> " + title + " is already in favourite list";
                }
                this.favoriteMovies.add(title);
                return "success -> " + title + " was added as favourite";
            }
        }
        return "error -> " + title + " is not seen";
    }
    /**
     * Adds a new video (movie or serial) in the history Map if it didn't already contain it (and
     * sets its number of views to 1), or increases the number of views by 1 if the Map already
     * contained it
     *
     * @return a string which tells the number of views of that video
     */
    public String addView(final String title) {
        if (history.containsKey(title)) {
            history.replace(title, history.get(title) + 1);
            return "success -> " + title + " was viewed with total views of " + history.get(title);
        }
        this.history.put(title, 1);
        return "success -> " + title + " was viewed with total views of 1";
    }
}
