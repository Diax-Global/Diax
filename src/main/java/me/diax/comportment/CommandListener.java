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

package me.diax.comportment;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.comportment.util.MessageUtil;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.inject.Inject;
import java.util.regex.Pattern;

/**
 * Created by Comportment at 21:28 on 15/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
public class CommandListener extends ListenerAdapter {

    private final CommandHandler handler;
    private final String prefix;

    @Inject
    public CommandListener(DiaxProperties properties, CommandHandler handler) {
        this.handler = handler;
        this.prefix = properties.getPrefix();
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        String message = event.getMessage().getRawContent();
        if (message.startsWith(prefix)) message = message.replaceFirst(Pattern.quote(prefix), "");
        message = message.trim();
        String first = message.split("\\s+")[0];
        Command command = handler.findCommand(first);
        if (command == null) return;
        if (!command.hasAttribute("allowPrivate")) return;
        if (command.getDescription().args() > message.split("\\s+").length) {
            event.getChannel().sendMessage(MessageUtil.errorEmbed("You did not specify enough arguments!")).queue();
            return;
        }
        handler.execute(command, event.getMessage(), message.replaceFirst(Pattern.quote(first), ""));
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String message = event.getMessage().getRawContent();
        if (!message.startsWith(prefix)) return;
        message = message.replaceFirst(Pattern.quote(prefix), "");
        String first = message.split("\\s+")[0];
        Command command = handler.findCommand(first);
        if (command == null) return;
        if (command.getDescription().args() > message.split("\\s+").length) {
            event.getChannel().sendMessage(MessageUtil.errorEmbed("You did not specify enough arguments!")).queue();
            return;
        }
        handler.execute(
                command,
                event.getMessage(),
                message.replaceFirst(Pattern.quote(first), ""));
    }
}