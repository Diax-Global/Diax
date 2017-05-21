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

package me.diax.comportment.diax.commands.administator

import me.diax.comportment.diax.util.MessageUtil
import me.diax.comportment.diax.util.Util
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.VoiceChannel


/**
 * Created by Comportment at 21:25 on 17/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "voicekick", triggers = arrayOf("voicekick", "vkick"), attributes = arrayOf(
        CommandAttribute(key = "description", value = "Kicks the mentioned users from a voice channel.")
), args = 1)
class VoiceKick : Command {

    override fun execute(message: Message, args: String) {
        if (!Util.checkPermission(message.guild, message.author, Permission.VOICE_MOVE_OTHERS)) {
            message.channel.sendMessage(MessageUtil.permissionError()).queue()
            return
        }
        message.mentionedUsers.map { user ->
            val channel = message.channel
            val controller = message.guild.controller
            val name = "${user.name}#${user.discriminator}"
            try {
                val member = message.guild.getMember(user)
                if (member.voiceState.inVoiceChannel()) {
                    controller.createVoiceChannel("..").queue({ voice -> controller.moveVoiceMember(member, voice as VoiceChannel).queue({ _ -> voice.delete().queue(); channel.sendMessage(MessageUtil.basicEmbed("$name has been voice kicked!")).queue() }) })
                } else {
                    channel.sendMessage(MessageUtil.errorEmbed("$name is not in a voice channel.")).queue()
                }
            } catch (e: Exception) {
                channel.sendMessage(MessageUtil.errorEmbed("Could **not** voice kick $name")).queue()
            }
        }
    }
}