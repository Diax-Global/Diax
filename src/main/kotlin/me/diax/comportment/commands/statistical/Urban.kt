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

package me.diax.comportment.commands.statistical;

import com.mashape.unirest.http.Unirest
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.util.MessageUtil
import net.dv8tion.jda.core.entities.Message

/**
 * Created by Comportment at 01:10 on 21/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "urban", triggers = arrayOf("urban"), attributes = arrayOf(
        CommandAttribute(key = "description", value = "Searches a command on urban dictionary."),
        CommandAttribute(key = "allowPrivate")
), args = 1)
class Urban : Command {

    override fun execute(message: Message, args: String) {
        try {
            val urban = Unirest.get("http://urbanscraper.herokuapp.com/define/" + args).asJson().body.`object`
            val result = MessageUtil.defaultEmbed().setTitle(urban.getString("term"))
                    .setDescription(urban.getString("definition"))
                    //.setTimestamp(ZonedDateTime(Timestamp.valueOf("posted"))
                    .addField("Example:", urban.getString("definition"), false).build()
            message.channel.sendMessage(result).queue()
        } catch (e: Exception) {
            val result = MessageUtil.errorEmbed("Definition was not found.")
            message.channel.sendMessage(result).queue()
        }
    }
}