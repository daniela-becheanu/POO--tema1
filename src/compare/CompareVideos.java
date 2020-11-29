package compare;

import convert.Video;

import java.util.Comparator;

public final class CompareVideos implements Comparator<Video> {

    @Override
    public int compare(final Video video1, final Video video2) {
        if (video1.getFirstCriterion() > video2.getFirstCriterion()) {
            return 1;
        } else if (video1.getFirstCriterion() < video2.getFirstCriterion()) {
            return -1;
        }

        if (video1.getSecondCriterion() != video2.getSecondCriterion()) {
            return video1.getSecondCriterion() - video2.getSecondCriterion();
        }
        return video1.getThirdCriterion().compareTo(video2.getThirdCriterion());
    }
}
