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

import com.mashape.unirest.http.Unirest;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.PermissionUtil;

/**
 * Created by Comportment at 00:48 on 16/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
public class Util {

    public static String getAnimal(String animal) {
        try {
            return Unirest.get("https://shibe.online/api/" + animal + "/?count=1")
                    .header("User-Agent", "Diax-Bot")
                    .header("Content-Type", "application/json")
                    .asJson().getBody().getArray().getString(0);
        } catch (Exception e) {
            return "https://http.cat/404.jpg";
        }
    }

    public static boolean checkPermission(Guild guild, User user, Permission permission) {
        return PermissionUtil.checkPermission(guild.getMember(user), permission) || user.getIdLong() == getOwnerLong();
    }

    public static long getOwnerLong() {
        return 293884638101897216L;
    }
}