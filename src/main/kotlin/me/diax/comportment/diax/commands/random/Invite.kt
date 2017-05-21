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

package me.diax.comportment.diax.commands.random

import me.diax.comportment.diax.Main
import me.diax.comportment.diax.util.MessageUtil
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import net.dv8tion.jda.core.entities.Message
import java.security.SecureRandom

/**
 * Created by Comportment at 19:18 on 18/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "invite", triggers = arrayOf("invite", "guild", "server"), attributes = arrayOf(
        CommandAttribute(key = "description", value = "Invites you to a random server that Diax is in"),
        CommandAttribute(key = "allowPrivate")
))
@Deprecated("This shouldn't be used for now.")
class Invite : Command {

    override fun execute(message: Message, args: String) {
        message.channel.sendMessage(MessageUtil.basicEmbed("Searching for server...")).queue{ msg ->
            var invite = ""
            var guilds = Main.getShards().flatMap { jda -> jda.guilds }
            if (message.guild != null) guilds = guilds.minus(message.guild)
            while (invite == "") {
                if (guilds.isEmpty()) {
                    msg.editMessage(MessageUtil.errorEmbed("No guilds could be found :/")).queue()
                    invite = "mission failed"
                    continue
                }
                val guild = guilds[SecureRandom().nextInt(guilds.size)]
                guilds = guilds.minus(guild)
                try {
                    invite = "https://discord.gg/" + guilds[SecureRandom().nextInt(guilds.size)].publicChannel.createInvite().complete().code
                    msg.editMessage(MessageUtil.basicEmbed("Server found: $invite")).queue()
                } catch (e: Exception) {
                    continue
                }
            }
        }
    }
}