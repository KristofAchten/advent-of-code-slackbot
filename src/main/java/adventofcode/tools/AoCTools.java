package adventofcode.tools;

import adventofcode.domain.Member;
import adventofcode.events.EventChecker;
import adventofcode.events.FinishChallengeEvent;
import adventofcode.events.GetStarEvent;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.*;

/**
 * Author: kachte4
 * Date: 14-11-2020.
 */
public class AoCTools {

    private static final String AOC_URL_BASE = "https://www.adventofcode.com/%s/leaderboard/private/view/%s.json";
    private static final String HISTORY_PATH = "history/";

    // Json element identifiers
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String LOCAL_SCORE = "local_score";
    private static final String STARS = "stars";
    private static final String STAR_MAP = "completion_day_level";

    public static URL determineUrl(final String leaderboardId,
                                   final String year) {
        try {
            return new URL(String.format(AOC_URL_BASE, year, leaderboardId));
        } catch (MalformedURLException malformedURLException) {
            throw new RuntimeException(malformedURLException);
        }
    }

    public static List<Member> fetchMembersFromSite(final URL leaderboardUrl,
                                                    final String sessionId) {
        final List<Member> memberList = new ArrayList<>();
        final JsonElement json = JsonParser.parseString(HttpTools.getRawContentFromPageWithSession(leaderboardUrl, sessionId));

        json.getAsJsonObject().get("members").getAsJsonObject().entrySet().stream()
                .map(Map.Entry::getValue)
                .forEach(me -> parseMemberAndAddToList(me, memberList));

        return memberList;
    }

    private static void parseMemberAndAddToList(final JsonElement element,
                                                final List<Member> memberList) {
        final String id = JsonTools.getValueFromJsonElement(element, ID);
        final String score = JsonTools.getValueFromJsonElement(element, LOCAL_SCORE);
        final String nbOfStars = JsonTools.getValueFromJsonElement(element, STARS);
        final String name = JsonTools.getValueFromJsonElement(element, NAME);

        final Map<Integer, Integer> starMap = determineStarMap(element);

        if (id != null && score != null && nbOfStars != null) {
            memberList.add(
                    new Member(
                            Integer.parseInt(id),
                            Integer.parseInt(score),
                            Integer.parseInt(nbOfStars),
                            name,
                            starMap));
        }
    }

    private static Map<Integer, Integer> determineStarMap(final JsonElement element) {
        final Map<Integer, Integer> starMap = new HashMap<>();

        element.getAsJsonObject().get(STAR_MAP).getAsJsonObject().entrySet()
                .forEach(entry -> {
                    final Integer challenge = Integer.parseInt(entry.getKey());
                    final Integer numOfStars = entry.getValue().getAsJsonObject().entrySet().size();

                    starMap.put(challenge, numOfStars);
                });

        return starMap;
    }

    @SuppressWarnings("unchecked")
    public static List<Member> findAndParseMemberHistory(final String year) {
        final File historyDir = new File(HISTORY_PATH);
        if (!historyDir.exists()) {
            final boolean dirCreatedSuccesfully = historyDir.mkdir();
        }

        // Find the most recent history file.
        final Optional<String> mostRecent = Arrays
                .stream(Objects.requireNonNull(new File(HISTORY_PATH).list()))
                .filter(s -> s.startsWith(year + "_"))
                .max(Comparator.naturalOrder());

        if (!mostRecent.isPresent()) {
            return null;
        }

        // Deserialize
        try {
            final FileInputStream inputStream = new FileInputStream(HISTORY_PATH + mostRecent.get());
            final ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            return (List<Member>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error while deserializing member history", e);
        }
    }

    public static String constructSlackMessage(final List<Member> newMembers,
                                               final List<Member> oldMembers,
                                               final String leaderboardUrl) {
        final StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(":christmas_tree: *AoC update!* :christmas_tree:\n");

        generateEvents(newMembers, oldMembers, messageBuilder);
        generateScoreboard(newMembers, oldMembers, messageBuilder, leaderboardUrl);

        return messageBuilder.toString();
    }

    private static void generateEvents(final List<Member> newMembers,
                                       final List<Member> oldMembers,
                                       final StringBuilder messageBuilder) {
        messageBuilder.append("\n\n*=== STATUS UPDATES ===*\n");

        final String nothingToReport = "\n_No updates to report since the last run._";
        if (oldMembers == null) {
            messageBuilder.append(nothingToReport);
            return;
        }

        final StringBuilder eventsBuilder = new StringBuilder();
        for (Member newMember : newMembers) {
            final Member oldMember = determineMember(oldMembers, newMember.getId());
            if (oldMember == null) {
                eventsBuilder
                        .append("\n:new: *")
                        .append(newMember.getName())
                        .append("* joined the leaderboard!");
            } else {
                final List<EventChecker> checkers = Arrays.asList(
                        new GetStarEvent(oldMember, newMember),
                        new FinishChallengeEvent(oldMember, newMember)
                );
                checkers.forEach(c -> {
                    if (c.eventOccured()) {
                        eventsBuilder.append(c.getEventText());
                    }
                });
            }
        }

        if (eventsBuilder.toString().isEmpty()) {
            eventsBuilder.append(nothingToReport);
        }

        messageBuilder.append(eventsBuilder.toString());
    }

    private static Member determineMember(final List<Member> members,
                                          int id) {
        return members.stream().filter(m -> m.getId() == id).findAny().orElse(null);
    }

    private static void generateScoreboard(final List<Member> newMembers,
                                           final List<Member> oldMembers,
                                           final StringBuilder messageBuilder,
                                           final String leaderboardUrl) {
        messageBuilder.append("\n\n*==== LEADERBOARD ====*\n");

        if (oldMembers != null) {
            Collections.sort(oldMembers);
        }

        int index = 1;
        for (Member member : newMembers) {
            messageBuilder.append(String.format("\n%d. %s *%s*: %d Points, %d Stars :star2:",
                    index++, determinePosSwitch(index - 2, member, oldMembers).getSlackIcon(),
                    member.getName(), member.getScore(), member.getNumOfStars()));
        }
        messageBuilder.append(String.format("\n\n<%s|View leaderboard on adventofcode.com>", leaderboardUrl));
    }


    private enum PosSwitch {
        STEADY(":small_blue_diamond:"),
        UP(":small_green_triangle_up:"),
        DOWN(":small_red_triangle_down:");

        private final String slackIcon;

        PosSwitch(final String slackIcon) {
            this.slackIcon = slackIcon;
        }

        public String getSlackIcon() {
            return slackIcon;
        }
    }

    private static PosSwitch determinePosSwitch(final int newPos,
                                                final Member member,
                                                final List<Member> oldMembers) {

        if (oldMembers == null) {
            return PosSwitch.STEADY;
        }

        final int oldPos = oldMembers.indexOf(determineMember(oldMembers, member.getId()));
        if (oldPos == newPos) {
            return PosSwitch.STEADY;
        }

        return oldPos < newPos ? PosSwitch.DOWN : PosSwitch.UP;
    }

    public static void persistMembers(final List<Member> newMembers,
                                      final String year) {
        try {
            final String fileName = year + "_" + Long.toString(Instant.now().getEpochSecond());
            final FileOutputStream outputStream = new FileOutputStream(HISTORY_PATH + fileName);
            final ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);

            objectStream.writeObject(newMembers);

            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Error while saving members to history.", e);
        }
    }
}
