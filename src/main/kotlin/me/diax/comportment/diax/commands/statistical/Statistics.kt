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

import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary
import me.diax.comportment.diax.Main
import me.diax.comportment.diax.util.MessageUtil
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.jdacommand.JDACommandInfo
import net.dv8tion.jda.core.JDAInfo
import net.dv8tion.jda.core.entities.Message

/**
 * Created by Comportment at 21:14 on 15/05/17
 * https://github.com/Comportment | comportment@diax.me

 * @author Comportment
 */
@CommandDescription(name = "stats", triggers = arrayOf("statistics", "stats"), attributes = arrayOf(
        CommandAttribute(key = "allowPrivate")
), description = "Gives you Diax's statistics!")
class Statistics : Command {

    override fun execute(trigger: Message, args: String) {
        val rt = Runtime.getRuntime()
        trigger.channel.sendMessage(MessageUtil.basicEmbed(arrayOf(
                "__**Statistics**__",
                "Server: [OS Name/Arch/Version] [${System.getProperty("os.name")}/${System.getProperty("os.arch")}/${System.getProperty("os.version")}]",
                "Ram: [Used/Total] [${(rt.totalMemory() - rt.freeMemory()) / 1024 / 1024}Mb/${rt.totalMemory() / 1024 / 1024}Mb]",
                "Shards: [Current/Total] ${trigger.jda.shardInfo?.shardString?.replace(" ", "") ?: "[1/1]"}",
                "Theads: ${Thread.getAllStackTraces().keys.size}",
                "",
                "__**Discord Stats**__",
                "",
                "**Channels:**",
                "Total Channels: ${Main.getTotalChannels()}",
                "Text Channels: ${Main.getTextChannels()}",
                "Voice Channels: ${Main.getVoiceChannels()}",
                "Private Channels: ${Main.getPrivateChannels()}",
                "",
                "**Other**",
                "Guilds: ${Main.getGuilds()}",
                "Users: ${Main.getUserCount()}",
                "",
                "__**Libraries**__",
                "JDA: ${JDAInfo.VERSION}",
                "JDA-Command: ${JDACommandInfo.VERSION}",
                "Lavaplayer: ${PlayerLibrary.VERSION}",
                "Java: ${System.getProperty("java.version")}",
                "Kotlin: ${KotlinVersion.CURRENT}",
                "",
                "Use `<>credits` to see all the people who deserve credit for making Diax possible."
        ).joinToString("\n"))).queue()
    }
}