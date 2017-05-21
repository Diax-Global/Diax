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
import net.dv8tion.jda.core.exceptions.PermissionException

/**
 * Created by Comportment at 22:30 on 17/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "softban", triggers = arrayOf("softban", "softbanne"), attributes = arrayOf(
        CommandAttribute(key = "description", value = "Softbans the mentioned members from a guild.")
), args = 1)
class SoftBan : Command {

    override fun execute(message: Message, args: String) {
        if (!Util.checkPermission(message.guild, message.author, Permission.KICK_MEMBERS)) {
            message.channel.sendMessage(MessageUtil.permissionError()).queue()
            return
        }
        message.mentionedUsers.map { user ->
            val channel = message.channel
            val controller = message.guild.controller
            val name = "${user.name}#${user.discriminator}"
            try {
                controller.ban(user, 7).queue { _ -> controller.unban(user).queue(); channel.sendMessage(MessageUtil.basicEmbed("$name has been softbanned!")).queue() }
            } catch (exception: PermissionException) {
                channel.sendMessage(MessageUtil.errorEmbed("I do not have enough permission to softban $name")).queue()
            }
        }
    }
}