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

import me.diax.comportment.diax.util.MessageUtil
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import net.dv8tion.jda.core.entities.Message
import java.security.SecureRandom

/**
 * Created by Comportment at 18:25 on 18/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "dice", triggers = arrayOf("die", "dice", "roll"), attributes = arrayOf(
        CommandAttribute(key = "description", value = "Rolls a dice and shows the result"),
        CommandAttribute(key = "allowPrivate")
))
class Dice : Command {

    override fun execute(message: Message, args: String) {
        message.channel.sendMessage(MessageUtil.basicEmbed("\uD83C\uDFB2 You rolled a [${SecureRandom().nextInt(6) + 1}]")).queue()
    }
}