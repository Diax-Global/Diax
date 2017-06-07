package me.diax.diax;

import com.mashape.unirest.http.Unirest;

/**
 *
 * @author Comportment
 * @since 2.0.0
 */
public class Util {

    /**
     * @param token The token of the bot.
     * @return The amount of shards.
     * @author Comportment
     * @since 2.0.0
     */
    public static int getRecommendedShards(String token) {
        try {
            return Unirest.get("https://discordapp.com/api/gateway/bot")
                    .header("Authorization", "Bot " + token)
                    .header("Content-Type", "application/json")
                    .asJson().getBody().getObject().getInt("shards");
        } catch (Exception ignored) {
            return 1;
        }
    }

    public static String TOKEN = "token here";
}