package contain;

import fileio.ActorInputData;
import utils.Utils;

import java.util.List;
import java.util.function.Predicate;

public final class ContainsAwards implements Predicate<ActorInputData> {
    private final List<String> awards;

    public ContainsAwards(final List<String> awards) {
        this.awards = awards;
    }

    @Override
    public boolean test(final ActorInputData actor) {
        for (String award : awards) {
            if (!actor.getAwards().containsKey(Utils.stringToAwards(award))) {
                return false;
            }
        }
        return true;
    }
}
