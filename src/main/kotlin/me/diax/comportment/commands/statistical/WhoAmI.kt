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

package me.diax.comportment.commands.statistical

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.util.MessageUtil
import net.dv8tion.jda.core.entities.Message
import java.time.format.DateTimeFormatter

/**
 * Created by Comportment at 23:18 on 15/05/17
 * https://github.com/Comportment | comportment@diax.me

 * @author Comportment
 */
@CommandDescription(name = "whoami", triggers = arrayOf("whoami"), attributes = arrayOf(
        CommandAttribute(key = "allowPrivate"),
        CommandAttribute(key = "description", value = "Tells you who you are.")
))
class WhoAmI : Command {

    override fun execute(trigger: Message, args: String) {
        val user = trigger.author
        trigger.channel.sendMessage(MessageUtil.defaultEmbed().setThumbnail(user.effectiveAvatarUrl).addField("***${user.name+"#"+user.discriminator}***", arrayOf("", "__**Unique ID:**__", user.id, "", "__**Discord Join Date:**__", DateTimeFormatter.RFC_1123_DATE_TIME.format(user.creationTime), "", "__**Avatar URL:**__", user.effectiveAvatarUrl, "", "").joinToString("\n"), false).build()).queue()
    }
}