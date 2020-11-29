package convert;

public final class Actor {
    private final String name;
    private final double firstCriterion;

    public Actor(final String name, final double firstCriterion) {
        this.name = name;
        this.firstCriterion = firstCriterion;
    }

    public String getName() {
        return name;
    }

    public double getFirstCriterion() {
        return firstCriterion;
    }
}
