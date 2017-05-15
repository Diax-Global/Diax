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

import me.diax.comportment.util.MessageUtil;
import me.diax.jdacommand.Command;
import me.diax.jdacommand.CommandAttribute;
import me.diax.jdacommand.CommandDescription;
import me.diax.jdacommand.CommandHandler;
import net.dv8tion.jda.core.entities.Message;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Comportment at 00:03 on 16/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "help", triggers = {
        "diax", "help", "h", "?"
}, attributes = {
        @CommandAttribute(key = "description", value = "Shows help for all of the commands"),
        @CommandAttribute(key = "allowPrivate")
})

public class Help implements Command {

    private final CommandHandler handler;

    @Inject
    public Help(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute(Message message, String s) {
        String first = s.split("\\s+")[0];
        Command command = handler.findCommand(first);
        if (command != null && !command.hasAttribute("hideFromHelp")) {
            message.getChannel().sendMessage(MessageUtil.basicEmbed(getHelpFormat(command))).queue();
        } else {
            message.getChannel().sendMessage(MessageUtil.basicEmbed(
                    handler.getCommands().stream()
                            .filter(cd -> !cd.hasAttribute("hideFromHelp"))
                            .map(Help::getHelpFormat)
                            .collect(Collectors.joining("\n")))).queue();
        }
    }

    private static String getHelpFormat(Command command) {
        if (command.getDescription() == null) return null;
        CommandDescription cd = command.getDescription();
        return String.format("`%s` [%s]- `%s`", cd.name(), Arrays.stream(cd.triggers()).collect(Collectors.joining(", ", "`", "`")), command.getAttributeValueFromKey("description"));
    }
}