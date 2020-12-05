package adventofcode.events;

import adventofcode.domain.Member;

import java.util.Map;
import java.util.Objects;

/**
 * Author: kachte4
 * Date: 14-11-2020.
 */
public class FinishChallengeEvent extends EventChecker {

    public FinishChallengeEvent(final Member oldMember,
                                final Member newMember) {
        super(oldMember, newMember);
    }

    @Override
    public boolean eventOccured() {
        for (Map.Entry<Integer, Integer> entry : getNewMember().getStarMap().entrySet()) {
            final Integer challengeNumber = entry.getKey();
            final Integer numOfStars = entry.getValue();

            if (challengeFinishedInThisIteration(numOfStars, getOldMember().getStarMap().get(challengeNumber))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getEventText() {
        final StringBuilder sb = new StringBuilder();

        for (Map.Entry<Integer, Integer> entry : getNewMember().getStarMap().entrySet()) {
            final Integer challengeNumber = entry.getKey();
            final Integer numOfStars = entry.getValue();

            if (challengeFinishedInThisIteration(numOfStars, getOldMember().getStarMap().get(challengeNumber))) {
                sb
                        .append("\n:sports_medal: *")
                        .append(getNewMember().getName())
                        .append("* finished challenge number ")
                        .append(entry.getKey())
                        .append("!");
            }
        }

        return sb.toString();
    }

    private boolean challengeFinishedInThisIteration(final Integer newScore,
                                                     final Integer oldScore) {
        return newScore == 2 && !Objects.equals(oldScore, 2);
    }
}
