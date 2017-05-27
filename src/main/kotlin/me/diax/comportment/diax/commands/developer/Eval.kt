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

package me.diax.comportment.diax.commands.developer

import groovy.lang.GroovyShell
import groovy.lang.Binding
import me.diax.comportment.diax.Main
import me.diax.comportment.diax.util.Util
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.jdacommand.JDACommandInfo
import net.dv8tion.jda.core.JDAInfo
import net.dv8tion.jda.core.entities.Message
import java.io.StringWriter
import java.util.concurrent.Executors


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
), description = "Evals the given args.")
class Eval : Command {

    private val EVALS = ThreadGroup("Eval Thread Pool")
    private val POOL = Executors.newCachedThreadPool { r ->
        Thread(EVALS, r,
                EVALS.name + EVALS.activeCount())
    }

    override fun execute(trigger: Message, args: String) {
        POOL.submit{
            try {
                var engine = makeEngine(trigger)
                val result = engine.evaluate(args)
                if (result != null) trigger.channel.sendMessage("```groovy\n$result\n```").queue()
                var stdout = engine.getProperty("out").toString()
                if (!stdout.isEmpty()) trigger.channel.sendMessage("```groovy\nScript Output:\n$stdout\n```").queue()
            } catch (e: Exception) {
                trigger.channel.sendMessage(Util.paste(e.message)).queue()
            }
        }
    }

    fun makeEngine(trigger: Message): GroovyShell {
        var binding = Binding()
        binding.setProperty("out", StringWriter())
        val engine = GroovyShell(binding)
        engine.setVariable("jda", trigger.jda)
        engine.setVariable("JDAInfo", JDAInfo())
        engine.setVariable("JDACommandInfo", JDACommandInfo())
        engine.setVariable("this", trigger)
        engine.setVariable("guild", trigger.guild)
        engine.setVariable("channel", trigger.channel)
        engine.setVariable("shards", Main.getShards())
        return engine
    }
}