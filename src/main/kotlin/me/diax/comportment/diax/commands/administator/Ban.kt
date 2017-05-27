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
import me.diax.comportment.jdacommand.CommandDescription
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message

/**
 * Created by Comportment at 00:51 on 16/05/17
 * https://github.com/Comportment | comportment@diax.me

 * @author Comportment
 */
@CommandDescription(name = "ban", triggers = arrayOf("ban", "banne", "hammer"), description = "Bans the mentioned users.", args = 1)
class Ban : me.diax.comportment.jdacommand.Command {

    override fun execute(message: Message, s: String) {
        if (!Util.checkPermission(message.guild, message.author, Permission.BAN_MEMBERS)) {
            message.channel.sendMessage(MessageUtil.permissionError()).queue()
            return
        }
        message.mentionedUsers.forEach { user ->
            if (user == message.author) {
                message.channel.sendMessage(MessageUtil.errorEmbed("You can't ban yourself!")).queue()
            } else {
                try {
                    message.guild.controller.ban(user, 7).queue { _ -> message.channel.sendMessage(MessageUtil.basicEmbed(user.name + "#" + user.discriminator + " has been banned.")).queue() }
                } catch (e: net.dv8tion.jda.core.exceptions.PermissionException) {
                    message.channel.sendMessage(MessageUtil.errorEmbed("I don't have enough permission to ban: " + user.name + "#" + user.discriminator)).queue()
                }
            }
        }
    }
}