package adventofcode.events;

import adventofcode.domain.Member;

/**
 * Author: kachte4
 * Date: 14-11-2020.
 */
public abstract class EventChecker {

    private final Member oldMember;
    private final Member newMember;

    public EventChecker(final Member oldMember,
                        final Member newMember) {
        this.oldMember = oldMember;
        this.newMember = newMember;
    }

    public Member getOldMember() {
        return oldMember;
    }

    public Member getNewMember() {
        return newMember;
    }

    public abstract boolean eventOccured();

    public abstract String getEventText();

}
