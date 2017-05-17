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

package me.diax.comportment.scheduler;

import net.dv8tion.jda.core.utils.SimpleLog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Comportment at 20:49 on 17/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
public class DiaxScheduler {

    private static final SimpleLog logger = SimpleLog.getLog("DiaxScheduler");
    private static final ScheduledExecutorService timers = Executors.newScheduledThreadPool(10, r -> new Thread(r, "Diax Scheduled Task"));
    public static final Map<String, ScheduledFuture<?>> tasks = new HashMap<>();

    public static boolean scheduleRepeating(Runnable task, String name, long delay, long interval) {
        if (tasks.containsKey(name)) return false;
        tasks.put(name, timers.scheduleAtFixedRate(() -> {
            try {
                task.run();
            } catch (Exception e) {
                logger.log(SimpleLog.Level.FATAL, name + " could not be ran.");
            }
        }, delay, interval, TimeUnit.MILLISECONDS));
        return true;
    }

    public static void delayTask(Runnable task, long delay) {
        timers.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    public static boolean cancelTask(String taskName) {
        Iterator<Map.Entry<String, ScheduledFuture<?>>> i = tasks.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<String, ScheduledFuture<?>> next = i.next();
            if (next.getKey().equals(taskName)) {
                next.getValue().cancel(false);
                i.remove();
                return true;
            }
        }
        return false;
    }
}