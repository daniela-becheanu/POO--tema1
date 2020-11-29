package contain;

import fileio.ActorInputData;

import java.util.List;
import java.util.function.Predicate;

public final class ContainsWords implements Predicate<ActorInputData> {
    private final List<String> words;

    public ContainsWords(final List<String> words) {
        this.words = words;
    }

    @Override
    public boolean test(final ActorInputData actor) {
        for (String s : words) {
            if (!actor.getCareerDescription().contains(" " + s + " ")) {
                return false;
            }
        }
        return true;
    }
}
