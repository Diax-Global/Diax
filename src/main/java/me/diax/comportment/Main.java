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

package me.diax.comportment;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.knockturnmc.api.util.ConfigurationUtils;
import com.mashape.unirest.http.Unirest;
import me.diax.comportment.commands.administator.*;
import me.diax.comportment.commands.miscellaneous.Crystal;
import me.diax.comportment.commands.miscellaneous.Echo;
import me.diax.comportment.commands.miscellaneous.Embed;
import me.diax.comportment.commands.random.*;
import me.diax.comportment.commands.statistical.Help;
import me.diax.comportment.commands.statistical.Ping;
import me.diax.comportment.commands.statistical.Statistics;
import me.diax.comportment.commands.statistical.WhoAmI;
import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.comportment.scheduler.DiaxScheduler;
import me.diax.comportment.scheduler.DiaxTask;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.TextChannel;

import java.io.File;
import java.util.Arrays;

/**
 * Created by Comportment at 16:55 on 15/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
public class Main implements ComponentProvider, Module {

    private static JDA[] shards;
    private DiaxProperties properties;
    private Injector injector;
    private CommandHandler handler;

    private Main() {
        handler = new CommandHandler();
        properties = ConfigurationUtils.loadConfiguration(this.getClass().getClassLoader(), "diax.properties", new File(System.getProperty("user.dir")), DiaxProperties.class);
        injector = Guice.createInjector(this);
        handler.registerCommands(
                //Admin
                new Ban(),
                new Kick(),
                new Purge(),
                new SoftBan(),
                new Unban(),
                new VoiceKick(),

                //Misc
                new Crystal(),
                new Echo(),
                new Embed(),

                //Rand
                new Bird(),
                new Cat(),
                new Dice(),
                new Dog(),
                new Flip(),

                //Stats
                getInstance(Help.class),
                new Ping(),
                new Statistics(),
                new WhoAmI()
        );
    }

    public static void main(String[] args) {
        new Main().main();
    }

    private void main() {
        String token = properties.getToken();
        int amount = Main.getRecommendedShards(token);
        shards = new JDA[amount >= 3 ? amount : 1];
        for (int i = 0; i < shards.length; i++) {
            JDA jda = null;
            JDABuilder builder = new JDABuilder(AccountType.BOT)
                    .setAudioEnabled(true)
                    .setGame(Game.of(properties.getGame()))
                    .setToken(properties.getToken())
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListener(injector.getInstance(CommandListener.class));
            if (shards.length >= 3) {
                builder.useSharding(i, shards.length);
            }
            try {
                jda = builder.buildBlocking();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (jda != null) {
                shards[i] = jda;
            }
        }
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(DiaxProperties.class).toProvider(() -> properties);
        binder.bind(CommandHandler.class).toProvider(() -> handler);
    }

    @Override
    public <T> T getInstance(Class<T> type) {
        return injector.getInstance(type);
    }

    public static JDA[] getShards() {
        return shards;
    }

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