package adventofcode.events;

import adventofcode.domain.Member;

import java.util.Map;

/**
 * Author: kachte4
 * Date: 14-11-2020.
 */
public class GetStarEvent extends EventChecker {

    public GetStarEvent(final Member oldMember,
                        final Member newMember) {
        super(oldMember, newMember);
    }

    @Override
    public boolean eventOccured() {
        final Map<Integer, Integer> oldMemberStarMap = getOldMember().getStarMap();
        final Map<Integer, Integer> newMemberStarMap = getNewMember().getStarMap();

        return !oldMemberStarMap.equals(newMemberStarMap);
    }

    @Override
    public String getEventText() {
        final Map<Integer, Integer> oldMemberStarMap = getOldMember().getStarMap();
        final Map<Integer, Integer> newMemberStarMap = getNewMember().getStarMap();
        final StringBuilder sb = new StringBuilder();

        for (Map.Entry<Integer, Integer> entry : newMemberStarMap.entrySet()) {
            final Integer newNumOfStars = entry.getValue();
            final Integer oldNumOfStars = oldMemberStarMap.get(entry.getKey());

            if (oldNumOfStars == null || !oldNumOfStars.equals(newNumOfStars)) {
                final int numOfStarsGet = determineDifferenceInStars(oldNumOfStars, newNumOfStars);
                sb
                        .append("\n:star: *")
                        .append(getNewMember().getName())
                        .append("* got ")
                        .append(numOfStarsGet)
                        .append(" new ")
                        .append(numOfStarsGet == 1 ? "star" : "stars")
                        .append(" for challenge ")
                        .append(entry.getKey())
                        .append("!");
            }
        }

        return sb.toString();
    }

    private int determineDifferenceInStars(final Integer oldNumOfStars,
                                           final Integer newNumOfStars) {
        if (oldNumOfStars == null) {
            return newNumOfStars;
        }
        return newNumOfStars - oldNumOfStars;
    }
}
