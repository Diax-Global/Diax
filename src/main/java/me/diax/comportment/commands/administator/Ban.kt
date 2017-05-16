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

package me.diax.comportment.commands.administator

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.util.MessageUtil
import me.diax.comportment.util.Util
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.exceptions.PermissionException

/**
 * Created by Comportment at 00:51 on 16/05/17
 * https://github.com/Comportment | comportment@diax.me

 * @author Comportment
 */
@CommandDescription(name = "ban", triggers = arrayOf("ban", "banne", "hammer"), attributes = arrayOf(CommandAttribute(key = "description", value = "Bans a user if you have permission.")), args = 1)
class Ban : Command {

    override fun execute(message: Message, s: String) {
        if (!Util.checkPermission(message.guild, message.author, Permission.BAN_MEMBERS)) {
            message.channel.sendMessage(MessageUtil.permissionError()).queue()
            return
        }
        message.mentionedUsers.forEach { user ->
            try {
                message.guild.controller.ban(user, 7).queue { success -> message.channel.sendMessage(MessageUtil.basicEmbed(user.name + "#" + user.discriminator + " has been banned.")).queue() }
            } catch (e: PermissionException) {
                message.channel.sendMessage(MessageUtil.errorEmbed("I don't have enough permission to ban: " + user.name + "#" + user.discriminator)).queue()
            }
        }
    }
}