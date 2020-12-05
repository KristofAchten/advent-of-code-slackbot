package adventofcode;

import adventofcode.domain.Member;
import adventofcode.tools.AoCTools;
import adventofcode.tools.BatchTools;
import adventofcode.tools.HttpTools;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Author: kachte4
 * Date: 12-11-2020.
 */
public class AdventOfCodeSlackApp {

    public static void main(final String[] args) {
        try {
            doJob(args);
        } catch (RuntimeException ignored) {
        }
    }

    public static void doJob(final String[] args) {
        if (args.length < 1) {
            throw new RuntimeException("No config-file was provided to the job.");
        }

        final Properties config = BatchTools.getConfig(args[0]);
        final String webhook = config.getProperty("slack_hook");
        final String sessionId = config.getProperty("session_id");
        final String leaderboardId = config.getProperty("leaderboard_id");
        final String year = config.getProperty("year");

        execute(webhook, sessionId, leaderboardId, year);
    }

    private static void execute(final String webhook,
                                final String sessionId,
                                final String leaderboardId,
                                final String year) {
        // Determine the URL that points to the leaderboard page and fetch all member-data from its raw json content.
        final URL leaderboardUrl = AoCTools.determineUrl(leaderboardId, year);
        final List<Member> newMembers = AoCTools.fetchMembersFromSite(leaderboardUrl, sessionId);

        // Sort the member-data, resulting in the member with the highest score being at the top.
        Collections.sort(newMembers);

        // Construct the message to be posted to the slack webhook.
        final String message = AoCTools.constructSlackMessage(
                newMembers,
                AoCTools.findAndParseMemberHistory(year),
                leaderboardUrl.toString().substring(0, leaderboardUrl.toString().length() - 5));

        // Persist the current member-data to the history for later comparison to facilitate status update generation.
        AoCTools.persistMembers(newMembers, year);

        // Post message to the slack webhook.
        HttpTools.postMessageToHook(message, webhook);
    }
}
