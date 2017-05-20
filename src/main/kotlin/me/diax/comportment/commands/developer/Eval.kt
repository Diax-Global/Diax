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

package me.diax.comportment.commands.developer;

import groovy.lang.GroovyShell
import me.diax.comportment.Main
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.jdacommand.JDACommandInfo
import net.dv8tion.jda.core.JDAInfo
import net.dv8tion.jda.core.entities.Message

/**
 * Created by Comportment at 08:34 on 20/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "eval", triggers = arrayOf("eval"), attributes = arrayOf(
        CommandAttribute(key = "developerOnly"),
        CommandAttribute(key = "allowPrivate"),
        CommandAttribute(key = "hideFromHelp")
))
class Eval : Command {

    override fun execute(trigger: Message, args: String) {
    }

    fun makeEngine(trigger: Message): GroovyShell {
        val engine = GroovyShell()
        engine.setVariable("jda", trigger.jda)
        engine.setVariable("jdainfo", JDAInfo())
        engine.setVariable("jdacinfo", JDACommandInfo())
        engine.setVariable("this", trigger)
        engine.setVariable("guild", trigger.guild)
        engine.setVariable("channel", trigger.channel)
        //engine.setVariable("system", System())
        engine.setVariable("shards", Main.getShards())
        return engine
    }
}