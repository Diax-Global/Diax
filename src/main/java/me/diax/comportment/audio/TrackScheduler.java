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

package me.diax.comportment.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.diax.comportment.util.MessageUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.utils.SimpleLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Comportment at 21:25 on 18/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
public class TrackScheduler extends AudioEventAdapter implements Runnable {

    private static ExecutorService executor;

    static {
        ThreadGroup group = new ThreadGroup("AudioThreads");
        executor = Executors.newCachedThreadPool(r -> new Thread(group, r, "AudioThread-" + group.activeCount()));
    }

    private final SimpleLog logger = SimpleLog.getLog("TrackScheduler");
    public final BlockingQueue<MusicTrack> queue;
    public GuildMusicManager manager;
    private boolean repeating = false;

    private MusicTrack currentTrack;
    private MusicTrack lastTrack;

    public TrackScheduler(GuildMusicManager manager) {
        this.manager = manager;
        this.queue = new LinkedBlockingQueue<>();
        executor.execute(this);
    }

    public boolean play(MusicTrack track) {
        if (track != null) {
            currentTrack = track;
            return manager.player.startTrack(track, false);
        }
        return false;
    }

    public void queue(MusicTrack track) {
        if (queue.offer(track) && this.currentTrack == null) {
            skip();
        }
    }

    public boolean shuffle() {
        if (!queue.isEmpty()) {
            List<MusicTrack> tracks = new ArrayList<>();
            queue.drainTo(tracks);
            Collections.shuffle(tracks);
            queue.addAll(tracks);
            return true;
        }
        return false;
    }

    public boolean skip() {
        lastTrack = currentTrack;
        if (repeating) {
            if (currentTrack != null)
                play(currentTrack.makeClone());
            else if (!queue.isEmpty())
                play(this.queue.poll());
        } else if (queue.isEmpty()) {
            if (currentTrack != null) {
                currentTrack.channel.sendMessage(MessageUtil.basicEmbed("The queue has concluded.")).queue();
            }
            stop();
        } else {
            play(queue.poll());
        }
        return true;
    }

    public void stop() {
        logger.debug("Stopping the track.");
        queue.clear();
        manager.player.stopTrack();
        currentTrack = null;
        manager.guild.getAudioManager().closeAudioConnection();
    }

    public boolean isRepeating() {
        return this.repeating;
    }

    public void setRepeating(boolean repeating) {
        logger.warn("Current track: " + (currentTrack == null ? "null" : currentTrack.hashCode()));
        this.repeating = repeating;
    }

    public VoiceChannel getVoiceChannel(Guild guild, Member member) {
        VoiceChannel vc = null;
        if (member != null && member.getVoiceState().inVoiceChannel()) {
            vc = member.getVoiceState().getChannel();
        } else if (!guild.getVoiceChannels().isEmpty()) {
            vc = guild.getVoiceChannels().get(0);
        }
        return vc;
    }

    public boolean joinVoiceChannel() {
        Guild guild = this.manager.guild;
        Member member = guild.getMember(currentTrack.requester);
        VoiceChannel voiceChannel = getVoiceChannel(guild, member);
        if (!guild.getAudioManager().isConnected() || this.queue.isEmpty() && voiceChannel != null) {
            try {
                guild.getAudioManager().openAudioConnection(voiceChannel);
            } catch (PermissionException exception) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        logger.debug("Starting the player.");
        if (joinVoiceChannel()) {
            if (!repeating) {
                currentTrack.channel.sendMessage(currentTrack.getCard()).queue();
            } else {
                currentTrack.channel.sendMessage(MessageUtil.basicEmbed("Repeating the last song.")).queue();
            }
        } else {
            currentTrack.channel.sendMessage(MessageUtil.errorEmbed("I do not have enough permission to join that voice channel!")).queue();
            stop();
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason reason) {
        if (reason.mayStartNext) {
            logger.warn("Was told we could play the next track.");
            skip();
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        currentTrack.channel.sendMessage(MessageUtil.errorEmbed("Failed to play the track due to: `" + (exception.getMessage() != null ? exception.getMessage() : "Unknown.") + " `")).queue();
        logger.warn(exception.getMessage());
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        currentTrack.channel.sendMessage(MessageUtil.errorEmbed("Got stuck attempting to play track, skipping.")).queue();
        skip();
    }

    @Override
    public void run() {
        boolean paused = false;
        boolean stopped = false;
        long timeout = TimeUnit.MINUTES.toMillis(2);
        long disconnectTime = System.currentTimeMillis() + timeout;
        while (manager.player != null) {
            boolean connected = manager.guild.getAudioManager().isConnected();
            VoiceChannel channel = manager.guild.getAudioManager().getConnectedChannel();
            /* If stopped due to a timeout, reset and un-pause player */
            if (currentTrack != null && stopped) {
                stopped = false;
                manager.player.setPaused(false);
                disconnectTime = System.currentTimeMillis() + timeout;
            }
            /* If we paused and reach timeout period, disconnect and set stop state */
            else if (currentTrack != null && connected && System.currentTimeMillis() >= disconnectTime) {
                logger.info(String.valueOf(disconnectTime));
                stop();
                stopped = true;
                paused = false;
            }
            /* If we aren't paused, but we're the only one in the channel, set pause state */
            else if (currentTrack != null && connected && channel.getMembers().size() == 1 && !paused) {
                manager.player.setPaused(true);
                paused = true;
            }
            /* If we are paused, but we're no longer alone, unset pause state */
            else if (currentTrack != null && connected && channel.getMembers().size() > 1 && paused) {
                manager.player.setPaused(false);
                disconnectTime = System.currentTimeMillis() + timeout;
                paused = false;
            }
            /* If we aren't paused or stopped, increment timeout period */
            else if (!paused && !stopped) {
                disconnectTime = System.currentTimeMillis() + timeout;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}