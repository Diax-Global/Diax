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

import me.diax.comportment.Main
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.jdacommand.JDACommandInfo
import me.diax.comportment.util.MessageUtil
import net.dv8tion.jda.core.JDAInfo
import net.dv8tion.jda.core.entities.Message

/**
 * Created by Comportment at 21:14 on 15/05/17
 * https://github.com/Comportment | comportment@diax.me

 * @author Comportment
 */
@CommandDescription(name = "stats", triggers = arrayOf("statistics", "stats"), attributes = arrayOf(CommandAttribute(key = "description", value = "The description of the command to show in help"), CommandAttribute(key = "allowPrivate")))
class Statistics : Command {

    override fun execute(trigger: Message, args: String) {
        val rt = Runtime.getRuntime()
        trigger.channel.sendMessage(MessageUtil.basicEmbed(arrayOf(
                "__**Statistics**__",
                "Ram: [Used/Total] [${(rt.totalMemory() - rt.freeMemory()) / 1024 / 1024}Mb/${rt.totalMemory() / 1024 / 1024}Mb]",
                "Shards: [Current/Total] ${trigger.jda.shardInfo?.shardString?.replace(" ", "") ?: "[1/1]"}",
                "Guilds: ${Main.getGuilds()}",
                "Users: ${Main.getUserCount()}",
                "",
                "__**Channels**__",
                "Total Channels: ${Main.getTotalChannels()}",
                "Text Channels: ${Main.getTextChannels()}",
                "Voice Channels: ${Main.getVoiceChannels()}",
                "Private Channels: ${Main.getTextChannels()}",
                "",
                "__**Libraries**__",
                "JDA: ${JDAInfo.VERSION}",
                "JDA-Command: ${JDACommandInfo.VERSION}",
                "Java: ${System.getProperty("java.version")}",
                "Kotlin: ${KotlinVersion.CURRENT}",
                "",
                "__**Developers**__",
                "Comportment#9489: JDA-Command library dev, lead bot developer."
        ).joinToString("\n"))).queue()
    }
}