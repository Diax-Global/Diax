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

package me.diax.comportment.commands.administator;

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.util.MessageUtil
import me.diax.comportment.util.Util
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message

/**
 * Created by Comportment at 18:38 on 16/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "ban", triggers = arrayOf("ban", "banne", "hammer"), attributes = arrayOf(CommandAttribute(key = "description", value = "Kicks a user if you have permission.")), args = 1)
class Kick : Command {

    override fun execute(message: Message, string: String) {
        val controller = message.guild.controller
        if (!Util.checkPermission(message.guild, message.author, Permission.KICK_MEMBERS)) {
            message.channel.sendMessage(MessageUtil.permissionError()).queue()
            return
        }
        message.mentionedUsers.map { user ->
            if (user == message.author) {
                message.channel.sendMessage(MessageUtil.errorEmbed("You can't kick yourself!")).queue()
            }
            try {
                controller.kick(message.guild.getMember(user)).queue { _ -> message.channel.sendMessage(MessageUtil.basicEmbed("${user.name + "#" + user.discriminator} has been banned.")).queue() }
            } catch (e: Exception) {
                message.channel.sendMessage(MessageUtil.basicEmbed("Could not kick: ${user.name + "#" + user.discriminator}, do I have enough permission?"))
            }
        }
    }
}