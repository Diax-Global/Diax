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
import me.diax.comportment.jdacommand.CommandDescription
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.exceptions.PermissionException

/**
 * Created by Comportment at 22:42 on 17/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "unban", triggers = arrayOf("unban", "pardon"), description = "Unbans the mentioned banned users.", args = 1)
class Unban : Command {

    override fun execute(message: Message, args: String) {
        if (!Util.checkPermission(message.guild, message.author, Permission.BAN_MEMBERS)) {
            message.channel.sendMessage(MessageUtil.permissionError()).queue()
            return
        }
        try {
            message.guild.controller.bans.queue { bans ->
                message.mentionedUsers.map { user ->
                    val name = "${user.name}#${user.discriminator}"
                    if (bans.contains(user)) {
                        message.channel.sendMessage(MessageUtil.errorEmbed("$name is not banned.")).queue()
                    }
                    message.guild.controller.unban(user).queue{_ -> message.channel.sendMessage(MessageUtil.basicEmbed("$name has been unbanned.")).queue()}
                }
            }
        } catch (e: PermissionException) {
            message.channel.sendMessage(MessageUtil.errorEmbed("I could not retrieve the banned members on this guild!")).queue()
        }
    }
}