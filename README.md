# Advent of Code: slackbot
This is a bot written in Java that pulls data from a private Advent of Code leaderboard, processes it, and formats it nicely whereafter it will post the data to a given slack webhook.

This is a maven project, so all dependencies should be handled for you. Just import it into your favorite IDE (as a maven project) and you should be good to go!

![alt text](https://i.ibb.co/55sdyzz/aoc-example.png "Example slack post")

## How to use
Initial setup needs to be performed by filling in the `settings.properties` file under `src/main/resources/`. No need to use any quotes! Other than this, just provide the location of your settings-file as an input parameter to the Java process and you're good to go (for the example file, this would be `src/main/resources/settings.properties`)! 

Schedule it somewhere to run as frequently as you like, but be aware that sending requests to the AoC-website more often than once every 15 minutes is frowned upon (as a matter of fact: you risk of getting your IP blocked)!

```properties
# Leaderboard ID: This is at the end of the URL used to view your leaderboard: https://adventofcode.com/<year>/leaderboard/private/view/<id>
leaderboard_id=123456

# Session ID: retrieve this from the cookie-header sent in a request to AoC https://adventofcode.com/ while logged in
session_id=678901

# This is the incoming webhook you created in slack
slack_hook=234567

# Year definition: this allows you to generate scoreboards for other years as well
year=2020
```

The data that should be provided is pretty much self explanatory, but let me help you get along.

### Leaderboard ID
This is simply the number at the end of your leaderboard URL.

![alt text](https://i.ibb.co/4jBSTpY/leaderboard.png "Leaderboard URL")

### Session ID
This is the id of a session on https://adventofcode.com that has access to your leaderboard. This id is stored in a cookie. See e.g. [this issue on another project](https://github.com/wimglenn/advent-of-code-wim/issues/1) on how to obtain it.

If you want, you can use the same session ID to automatically pull your puzzle-input from the website. I have done this myself in my solutions for 2020. Check the Kotlin-implementation [here!](https://github.com/KristofAchten/AoC2020/blob/master/kotlin/src/challenges/Puzzle.kt)

### Slack incoming webhook
This is the webhook to which the formatted Slack message will be posted (literally through a POST-request). Check out [this article](https://slack.com/intl/en-be/help/articles/115005265063-Incoming-webhooks-for-Slack) on how to set it up.

### Year
If you want, you can generate final scoreboards for other AoC events.


