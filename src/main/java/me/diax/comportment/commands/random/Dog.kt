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

package me.diax.comportment.commands.random;

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.util.MessageUtil
import me.diax.comportment.util.Util
import net.dv8tion.jda.core.entities.Message

/**
 * Created by Comportment at 18:20 on 18/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "dog", triggers = arrayOf("doge", "god", "dog", "shibe"), attributes = arrayOf(CommandAttribute(key = "allowPrivate"), CommandAttribute(key = "description", value = "Shows a random dog picture")))
class Dog : Command {

    override fun execute(message: Message, args: String) {
        message.channel.sendMessage(MessageUtil.defaultEmbed().setTitle("\uD83D\uDC36").setImage(Util.getAnimal("shibes")).build()).queue()
    }
}