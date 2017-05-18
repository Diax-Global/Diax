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

package me.diax.comportment.commands.musical;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import me.diax.comportment.audio.GuildMusicManager
import me.diax.comportment.audio.MusicTrack
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.util.MessageUtil
import net.dv8tion.jda.core.entities.Message

/**
 * Created by Comportment at 21:37 on 18/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "play", triggers = arrayOf("play"), attributes = arrayOf(CommandAttribute(key = "description", value = "Attempts to play the given track.")))
class Play : Command {

    override fun execute(message: Message, args: String) {
        val manager = GuildMusicManager.getManagerFor(message.guild)
        if (message.guild.getMember(message.author).voiceState.inVoiceChannel()) {
            query(manager, message, args)
        } else {
            message.channel.sendMessage(MessageUtil.errorEmbed("You must be in a voice channel first!")).queue()
        }
    }

    fun query(manager: GuildMusicManager, message: Message, query: String) {
        manager.playerManager.loadItem(query, object : AudioLoadResultHandler {

            override fun playlistLoaded(playlist: AudioPlaylist) {
                playlist.tracks.forEach { trackLoaded(it) }
            }

            override fun trackLoaded(track: AudioTrack) {
                val mtrack = MusicTrack(track, message.author, message.textChannel)
                message.textChannel.sendMessage(mtrack.card).queue()
                if (!manager.scheduler.play(MusicTrack(track, message.author, message.textChannel))) {
                    manager.scheduler.queue(mtrack)
                }
            }

            override fun loadFailed(exception: FriendlyException) {
                if (!query.startsWith("Search Results:")) {
                    query(manager, message, "ytsearch: " + query)
                } else {
                    message.textChannel.sendMessage(MessageUtil.errorEmbed(exception.message)).queue()
                }
            }

            override fun noMatches() {
                loadFailed(FriendlyException("No matches found.", FriendlyException.Severity.COMMON, Throwable("A track could not be found.")))
            }
        })
    }
}

