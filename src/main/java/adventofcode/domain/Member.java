package adventofcode.domain;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

/**
 * Author: kachte4
 * Date: 12-11-2020.
 */
public class Member implements Comparable<Member>, Serializable {

    private static final Comparator<Member> COMPARATOR = Comparator
            .comparingInt(Member::getScore)
            .thenComparingInt(Member::getNumOfStars)
            .thenComparing(Member::getName);

    private final int id;
    private final int score;
    private final int numOfStars;
    private final String name;
    private final Map<Integer, Integer> starMap;

    public Member(final int id,
                  final int score,
                  final int numOfStars,
                  final String name,
                  final Map<Integer, Integer> starMap) {
        this.id = id;
        this.score = score;
        this.numOfStars = numOfStars;
        this.name = name;
        this.starMap = starMap;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public int getNumOfStars() {
        return numOfStars;
    }

    public String getName() {
        return name == null ? "Anonymous user #" + id : name;
    }

    public Map<Integer, Integer> getStarMap() {
        return starMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }
        Member member = (Member) o;
        return getScore() == member.getScore() &&
                getId() == member.getId() &&
                getNumOfStars() == member.getNumOfStars() &&
                getName().equals(member.getName()) &&
                getStarMap().equals(member.getStarMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getScore(), getId(), getNumOfStars(), getName(), getStarMap());
    }

    @Override
    public int compareTo(final Member o) {
        return COMPARATOR.compare(o, this);
    }
}
