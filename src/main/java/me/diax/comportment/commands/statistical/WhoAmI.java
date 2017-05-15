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
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

/**
 * Created by Comportment at 23:18 on 15/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "whoami", triggers = {
        "whoami"
}, attributes = {
        @CommandAttribute(key = "allowPrivate"),
        @CommandAttribute(key = "description", value = "Tells you who you are.")
})
public class WhoAmI implements Command {

    @Override
    public void execute(Message trigger, String args) {
        User user = trigger.getAuthor();
        trigger.getChannel().sendMessage(MessageUtil.basicEmbed(user.getName() + "#" + user.getDiscriminator())).queue();
    }
}