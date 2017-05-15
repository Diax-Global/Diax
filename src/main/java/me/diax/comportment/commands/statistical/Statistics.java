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

import me.diax.comportment.util.ShardUtil;
import me.diax.jdacommand.Command;
import me.diax.jdacommand.CommandAttribute;
import me.diax.jdacommand.CommandDescription;
import net.dv8tion.jda.core.entities.Message;

/**
 * Created by Comportment at 21:14 on 15/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
@CommandDescription(name = "stats", triggers = {
        "statistics" , "stats"
}, attributes = {
        @CommandAttribute(key = "description", value = "The description of the command to show in help"),
        @CommandAttribute(key = "allowPrivate")
})
public class Statistics implements Command {

    @Override
    public void execute(Message trigger, String args) {
        String guilds = ShardUtil.getGuilds() + "";
        String channels = ShardUtil.getTotalChannels() + "";
        //^ send that and some other stuff to channel
        trigger.getChannel().sendMessage("Guilds: " + guilds).queue(); //like that
    }
}