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

package me.diax.comportment.commands.miscellaneous;

import me.diax.comportment.Main
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.util.MessageUtil
import net.dv8tion.jda.core.entities.Message

/**
 * Created by Comportment at 19:07 on 18/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "discrim", triggers = arrayOf("discriminator", "farm"), attributes = arrayOf(
        CommandAttribute(key = "description", value = "Gives you a list of all the users with the given discriminator."),
        CommandAttribute(key = "allowPrivate")
), args = 1)
class Discrim : Command {

    override fun execute(message: Message, args: String) {
        val first = args.split(Regex("\\s+"))[0]
        val users = Main.getShards().flatMap { jda -> jda.users }.filter { user -> user.discriminator == first }.subList(0, 10).map { user -> "${user.name}#${user.discriminator}" }.joinToString("\n")
        message.channel.sendMessage(MessageUtil.basicEmbed("***Users found with the discriminator #$first***\n$users")).queue()
    }
}