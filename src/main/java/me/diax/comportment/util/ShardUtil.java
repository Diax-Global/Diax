/*
 * Copyright 2017 Comportment | comportment@diax.me
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.diax.comportment.util;

import com.mashape.unirest.http.Unirest;
import me.diax.comportment.Main;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Arrays;

/**
 * Created by Comportment at 20:05 on 15/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
public class ShardUtil {

    private static JDA[] shards = Main.getShards();

    public static TextChannel retreiveTextChannel(long id) {
        return Arrays.stream(shards).flatMap(jda -> jda.getTextChannels().stream()).findFirst().orElse(null);
    }

    public static long getUserCount() {
        return Arrays.stream(shards).mapToLong(jda -> jda.getUsers().size()).sum();
    }

    public static long getTotalChannels() {
        return getVoiceChannels() + getTextChannels() + getPrivateChannels();
    }

    public static long getVoiceChannels() {
        return Arrays.stream(shards).mapToLong(jda -> jda.getVoiceChannels().size()).sum();
    }

    public static long getTextChannels() {
        return Arrays.stream(shards).mapToLong(jda -> jda.getTextChannels().size()).sum();
    }

    public static long getPrivateChannels() {
        return Arrays.stream(shards).mapToLong(jda -> jda.getPrivateChannels().size()).sum();
    }

    public static long getGuilds() {
        return Arrays.stream(shards).mapToLong(jda -> jda.getGuilds().size()).sum();
    }

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
}