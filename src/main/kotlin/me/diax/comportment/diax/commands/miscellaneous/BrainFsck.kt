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

import me.diax.comportment.brainjack.BrainJack
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import net.dv8tion.jda.core.entities.Message

/**
 * Created by Comportment at 21:35 on 26/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "brainjack", triggers = arrayOf("bf", "brain", "brainfsk", "brain****", "brainfuck"), attributes = arrayOf(
        CommandAttribute(key = "allowPrivate")
), description = "A brainfuck interpretor, shhhh.")
class BrainFsck : Command {

    override fun execute(message: Message, string: String) {
        message.channel.sendMessage(BrainJack(32).interpret(string)).queue()
    }
}