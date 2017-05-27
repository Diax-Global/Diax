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

import me.diax.comportment.diax.util.MessageUtil
import me.diax.comportment.diax.util.Util
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.jdacommand.CommandHandler
import net.dv8tion.jda.core.entities.Message
import javax.inject.Inject

/**
 * Created by Comportment at 00:03 on 16/05/17
 * https://github.com/Comportment | comportment@diax.me

 * @author Comportment
 */
@CommandDescription(name = "help", triggers = arrayOf("diax", "help", "h", "?"), attributes = arrayOf(
        CommandAttribute(key = "allowPrivate")
), description = "Shows help for all of the commands.")
class Help @Inject
constructor(private val handler: CommandHandler) : Command {

    override fun execute(message: Message, s: String) {
        val first = s.split("\\s+".toRegex())[0]
        val command = handler.findCommand(first)
        if (command != null && command.description != null && !command.hasAttribute("hideFromHelp") ) {
            val cd = command.description
            message.channel.sendMessage(MessageUtil.basicEmbed(arrayOf(
                    "__**${cd.name}**__",
                    "",
                    "__**Description:**__ ",
                    command.description.description,
                    "",
                    "__**Triggers:**__",
                    cd.triggers.joinToString(", "))
                    .joinToString("\n"))).queue()
        } else {
            message.channel.sendMessage(MessageUtil.basicEmbed("__**Links:**__\n\n" + Util.links() + "\n\n__**Commands:**__\n\n" +
                    handler.commands.filterNot { it.hasAttribute("hideFromHelp") }.map { getHelpFormat(it) }.joinToString("\n") + "\n\n"
                    + "Do `help [command]` for more information about a command.")).queue()
        }
    }

    private fun getHelpFormat(command: Command): String? {
        if (command.description == null) return null
        val cd = command.description
        return "`${cd.name}`: [${cd.triggers.joinToString(", ")}]"
    }
}