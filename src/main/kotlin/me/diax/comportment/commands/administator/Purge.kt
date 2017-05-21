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
import java.util.concurrent.TimeUnit

/**
 * Created by Comportment at 00:03 on 17/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "purge", triggers = arrayOf("purge", "clear", "clean"), attributes = arrayOf(
        CommandAttribute(key = "description", value = "Purges up to 100 messages at a time from the chat.")
), args = 1)
class Purge : Command {

    override fun execute(message: Message, string: String) {
        if (!Util.checkPermission(message.guild, message.author, Permission.MESSAGE_MANAGE)) {
            message.channel.sendMessage(MessageUtil.permissionError()).queue()
            return
        }
        var amount: Int = string.split("\\s+".toRegex())[0].toInt()
        if (amount > 100) amount = 100
        if (amount < 0) amount = 2
        val channel = message.textChannel
        message.channel.history.retrievePast(amount).queue { history ->
            val msg = history.filterNot { it.isPinned }
            try {
                channel.deleteMessages(msg).queue { _ ->
                    message.channel.sendMessage(MessageUtil.basicEmbed("${msg.size} messages have been deleted.\nThis message will be deleted after 10 seconds.")).queue { it.delete().queueAfter(10, TimeUnit.SECONDS) }
                }
            } catch (exception: PermissionException) {
                message.channel.sendMessage(MessageUtil.basicEmbed("Could not purge: #${channel.name} do I have enough permission?"))
            }
        }
    }
}