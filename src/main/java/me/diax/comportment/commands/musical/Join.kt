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

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.util.MessageUtil
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.exceptions.PermissionException

/**
 * Created by Comportment at 21:57 on 18/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "join", triggers = arrayOf("join"), attributes = arrayOf(CommandAttribute(key = "description", value = "Makes Diax attempt to join your voice channel.")))
class Join : Command {

    override fun execute(message: Message, string: String) {
        val voiceState = message.guild.getMember(message.author).voiceState
        val channel = message.textChannel
        if (!voiceState.inVoiceChannel()) {
            channel.sendMessage(MessageUtil.errorEmbed("You must be in a voice channel to do this!")).queue()
            return
        }
        try {
            message.guild.audioManager.openAudioConnection(voiceState.channel)
            channel.sendMessage(MessageUtil.basicEmbed("I have joined your voice channel.")).queue()
        } catch (exception: PermissionException) {
            channel.sendMessage(MessageUtil.errorEmbed("I could not join your voice channel.")).queue()
        }
    }
}