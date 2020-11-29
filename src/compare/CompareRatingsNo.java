package compare;

import fileio.UserInputData;

import java.util.Comparator;

public class CompareRatingsNo implements Comparator<UserInputData> {
    @Override
    public final int compare(final UserInputData user1, final UserInputData user2) {
        if (user2.getRatedMovies().size() != user1.getRatedMovies().size()) {
            return user1.getRatedMovies().size() - user2.getRatedMovies().size();
        }
        return user1.getUsername().compareTo(user2.getUsername());
    }
}
