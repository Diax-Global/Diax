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
import me.diax.comportment.commands.miscellaneous.Crystal;
import me.diax.comportment.commands.miscellaneous.Echo;
import me.diax.comportment.commands.statistical.Help;
import me.diax.comportment.commands.statistical.Ping;
import me.diax.comportment.commands.statistical.Statistics;
import me.diax.comportment.commands.statistical.WhoAmI;
import me.diax.comportment.util.ShardUtil;
import me.diax.jdacommand.CommandHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import java.io.File;

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
                new Echo(),
                getInstance(Help.class),
                new WhoAmI(),
                new Ping(),
                new Statistics(),
                new Crystal()
        );
    }

    public static void main(String[] args) {
        new Main().main();
    }

    private void main() {
        String token = properties.getToken();
        int amount = ShardUtil.getRecommendedShards(token);
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

    public static JDA[] getShards() {
        return shards;
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
}