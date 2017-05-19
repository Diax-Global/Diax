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

import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackState;
import com.sedmelluq.discord.lavaplayer.track.TrackMarker;
import me.diax.comportment.util.MessageUtil;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * Created by Comportment at 21:02 on 18/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
public class MusicTrack implements AudioTrack {

    private static final long MILLISECOND = 1;
    private static final long SECOND = 1000 * MILLISECOND;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    private static final long WEEK = 7 * DAY;

    public final AudioTrack track;
    public final User requester;
    public final TextChannel channel;

    public MusicTrack(AudioTrack toWrap, User requester, TextChannel channel) {
        track = toWrap;
        this.requester = requester;
        this.channel = channel;
    }

    @Override
    public AudioTrackInfo getInfo() {
        return track.getInfo();
    }

    @Override
    public String getIdentifier() {
        return track.getIdentifier();
    }

    @Override
    public AudioTrackState getState() {
        return track.getState();
    }

    @Override
    public void stop() {
        track.stop();
    }

    @Override
    public boolean isSeekable() {
        return track.isSeekable();
    }

    @Override
    public long getPosition() {
        return track.getPosition();
    }

    @Override
    public void setPosition(long l) {
        track.setPosition(l);
    }

    @Override
    public void setMarker(TrackMarker trackMarker) {
        track.setMarker(trackMarker);
    }

    @Override
    public long getDuration() {
        return track.getDuration();
    }

    @Override
    public MusicTrack makeClone() {
        return new MusicTrack(track, requester, channel);
    }

    @Override
    public AudioSourceManager getSourceManager() {
        return track.getSourceManager();
    }

    @Override
    public void setUserData(Object o) {
        track.setUserData(o);
    }

    @Override
    public Object getUserData() {
        return track.getUserData();
    }

    @Override
    public <T> T getUserData(Class<T> aClass) {
        return track.getUserData(aClass);
    }

    public MessageEmbed getCard() {
        return MessageUtil.defaultEmbed().setThumbnail(this.getInfo().uri)
                .addField("__**Title:**__", this.getInfo().title.replace("`", ""), true)
                .addField("__**Song Duration:**__", track.getInfo().isStream ? "LIVE" : getLengthString(), true)
                .addField("__**Requested By:**__", requester.getName() + "#" + requester.getDiscriminator(), true)
                .build();
    }

    public String getLengthString() {
        // TODO: Fix this, it always returns 18:04:xxx
        long t = this.getInfo().length;
        boolean neg = t < 0;
        if (neg) t = t * -1;
        int size = t > WEEK ? 5 : (t > DAY ? 4 : (t > HOUR ? 3 : (t > MINUTE ? 2 : 1)));
        long weeks = 0;
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;

        switch (size) {
            case (5):
                weeks = t / WEEK;
            case (4):
                days = (t %= WEEK) / DAY;
            case (3):
                hours = (t %= DAY) / HOUR;
            case (2):
                minutes = (t %= HOUR) / MINUTE;
            case (1):
                seconds = (t % MINUTE) / SECOND;
        }

        switch (size) {
            case (5):
                return String.format("%s%d:%02d:%02d:%02d:%02d", neg ? "-" : "", weeks, days, hours, minutes, seconds);
            case (4):
                return String.format("%s%d:%02d:%02d:%02d", neg ? "-" : "", days, hours, minutes, seconds);
            case (3):
                return String.format("%s%d:%02d:%02d", neg ? "-" : "", hours, minutes, seconds);
            case (2):
                return String.format("%s%d:%02d", neg ? "-" : "", minutes, seconds);
            case (1):
                return String.format("%s%d:%02d", neg ? "-" : "", 0, seconds);
            default:
                return "ERROR";
        }
    }

}