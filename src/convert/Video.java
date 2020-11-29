package convert;

public class Video {
    private final String title;
    private final double firstCriterion;
    private final int secondCriterion;
    private final String thirdCriterion;

    public Video(final String title, final double firstCriterion, final int secondCriterion,
                 final String thirdCriterion) {
        this.title = title;
        this.firstCriterion = firstCriterion;
        this.secondCriterion = secondCriterion;
        this.thirdCriterion = thirdCriterion;
    }

    public final String getTitle() {
        return title;
    }

    public final double getFirstCriterion() {
        return firstCriterion;
    }

    public final int getSecondCriterion() {
        return secondCriterion;
    }

    public final String getThirdCriterion() {
        return thirdCriterion;
    }
}
