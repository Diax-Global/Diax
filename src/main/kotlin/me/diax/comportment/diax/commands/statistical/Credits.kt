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
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import net.dv8tion.jda.core.entities.Message

/**
 * Created by Comportment at 23:01 on 27/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "credits", triggers = arrayOf("credits"), attributes = arrayOf(
        CommandAttribute(key = "allowPrivate")
), description = "Tells you the people who deserve credit.")
class Credits : Command {

    override fun execute(message: Message, args: String) {
        message.channel.sendMessage(MessageUtil.basicEmbed(arrayOf(
                "-]-+-[-",
                "ImBursting#8306, FOR HOSTING DIAX <3333 love meeeeeee",
                "NachtRaben#8307, for teaching me how to use lavaplayer and fixing my stupid mistakes.",
                "Andrew#0621, for being my first PayPal donator.",
                "Kodehawa#3457, for helping me when I was down and always brightening my day.",
                "Adrian#07522, for making me not make stupid decisions with my life.",
                "Crystal#3166, for being the best backend developer I know. <>wat",
                "Nomsy#7453, for coping with me. Good luck in your future.",
                "DV8FromTheWorld#6297, for making JDA possible. [Imagine if I had used D4J]",
                "sedmelluq#3562, for making an awesome music library.",
                "Skiletro#3888, for making a lot of art for Diax.",
                "zer0ney#0025, for being the cutest doggo.",
                "App#6132, for speaking to me even though you knew my past. Horses owo",
                "luke#2865, for making me interested in first botting Discord.",
                "Minn#6688, for reading the docs and making me interested in Kotlin.",
                "Lars#8117, for being a cutie <3, ~~can we ship yall with kode, kthx~~",
                "-]-+-[-"
        ).joinToString("\n\n", "```brainfuck\n", "```"))).queue()
    }
}