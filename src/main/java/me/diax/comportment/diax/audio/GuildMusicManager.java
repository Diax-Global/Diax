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

package me.diax.comportment.diax.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;

/**
 * Created by Comportment at 21:23 on 18/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
public class GuildMusicManager {

    private static HashMap<String, GuildMusicManager> MANAGERS;
    private static DefaultAudioPlayerManager PLAYER_MANAGER;

    static {
        MANAGERS = new HashMap<>();
        PLAYER_MANAGER = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(PLAYER_MANAGER);
    }

    public final AudioPlayer player;
    public final TrackScheduler scheduler;
    public final Guild guild;

    public GuildMusicManager(Guild guild) {
        player = PLAYER_MANAGER.createPlayer();
        scheduler = new TrackScheduler(this);
        this.guild = guild;
        player.addListener(scheduler);
        guild.getAudioManager().setSendingHandler(getSendHandler());
    }

    public static GuildMusicManager getManagerFor(Guild guild) {
        return MANAGERS.computeIfAbsent(guild.getId(), (i) -> new GuildMusicManager(guild));
    }

    public DefaultAudioPlayerManager getPlayerManager() {
        return PLAYER_MANAGER;
    }

    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }
}