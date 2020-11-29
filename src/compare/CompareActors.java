package compare;

import convert.Actor;

import java.util.Comparator;

public final class CompareActors implements Comparator<Actor> {
    @Override
    public int compare(final Actor actor1, final Actor actor2) {
        if (actor1.getFirstCriterion() > actor2.getFirstCriterion()) {
            return 1;
        }
        if (actor1.getFirstCriterion() < actor2.getFirstCriterion()) {
            return -1;
        }
        return actor1.getName().compareTo(actor2.getName());
    }
}
