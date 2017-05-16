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

package me.diax.comportment.util;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;

/**
 * Created by Comportment at 20:12 on 15/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
public class MessageUtil {

    public static EmbedBuilder defaultEmbed() {
        return new EmbedBuilder()
                .setColor(RandomUtil.randomColor())
                .setFooter("Powered by JDA-Command", null)
                .setAuthor("Diax", null, "https://cdn.discordapp.com/avatars/295500621862404097/5c2754a6dafa36d5c5372106da7c4c36.png");
    }


    public static MessageEmbed errorEmbed(String error) {
        return defaultEmbed().setDescription(error).setColor(Color.RED).build();
    }

    public static MessageEmbed permissionError() {
        return errorEmbed("You do not have enough permission to do that.");
    }

    public static MessageEmbed basicEmbed(String message) {
        return defaultEmbed().setDescription(message).build();
    }
}