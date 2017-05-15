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

package me.diax.comportment.commands.administator;

import me.diax.comportment.util.MessageUtil;
import me.diax.comportment.util.Util;
import me.diax.jdacommand.Command;
import me.diax.jdacommand.CommandAttribute;
import me.diax.jdacommand.CommandDescription;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 * Created by Comportment at 00:51 on 16/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "ban", triggers = {
        "ban", "banne", "hammer"
}, attributes = {
        @CommandAttribute(key = "description", value = "Bans a user if you have permission.")
}, args = 1)
public class Ban implements Command {

    @Override
    public void execute(Message message, String s) {
        if (!Util.checkPermission(message.getGuild(), message.getAuthor(), Permission.BAN_MEMBERS)) {
            message.getChannel().sendMessage(MessageUtil.errorEmbed("You don't have permission to do this!")).queue();
            return;
        }
        message.getMentionedUsers().forEach(user -> {
            try {
                message.getGuild().getController().ban(user, 7).queue();
            } catch (PermissionException e) {
                message.getChannel().sendMessage(MessageUtil.errorEmbed("I don't have enough permission to ban: " + user.getName() + "#" + user.getDiscriminator())).queue();
            }
        });
    }
}