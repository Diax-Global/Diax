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

package me.diax.comportment.diax.commands.miscellaneous

import me.diax.comportment.diax.Main
import me.diax.comportment.diax.util.MessageUtil
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import net.dv8tion.jda.core.entities.Message

/**
 * Created by Comportment at 19:07 on 18/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "discrim", triggers = arrayOf("discrim", "discriminator", "farm"), attributes = arrayOf(
        CommandAttribute(key = "allowPrivate")
), description = "Gives you a list of the first 10 users found with the discriminator given.", args = 1)
class Discrim : Command {

    override fun execute(message: Message, args: String) {
        val first = args.split(Regex("\\s+"))[0]
        val list = Main.getShards().flatMap { jda -> jda.users }.filter { user -> user.discriminator == first }
        if (list.size > 10) list.subList(0, 10)
        val users = list.map { user -> "${user.name}#${user.discriminator}" }.joinToString("\n")
        message.channel.sendMessage(MessageUtil.basicEmbed("__**Users with the discriminator #$first**__\n\n$users")).queue()
    }
}