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

package me.diax.comportment.diax.commands.statistical

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message

/**
 * Created by Comportment at 15:29 on 20/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "invite", triggers = arrayOf("invite", "guild", "server"), attributes = arrayOf(
        CommandAttribute(key = "description", value = "Invites you to Diax's server."),
        CommandAttribute(key = "allowPrivate")
))
class Invite : Command {

    override fun execute(message: Message, args: String) {
        message.channel.sendMessage("Invite Diax: <${message.jda.asBot().getInviteUrl(Permission.ADMINISTRATOR)}>\nGet Support: https://discord.gg/5sJZa2y").queue()
    }
}