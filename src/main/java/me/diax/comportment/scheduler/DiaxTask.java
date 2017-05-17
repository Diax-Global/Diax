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

/**
 * Created by Comportment at 21:00 on 17/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
public abstract class DiaxTask implements Runnable {

    private String name;

    public DiaxTask(String name) {
        this.name = name;
    }

    public DiaxTask() {
        this.name = "DiaxTask-" + System.currentTimeMillis();
    }

    public void delay(long delay) {
        DiaxScheduler.delayTask(this, delay);
    }

    public boolean repeat(long delay, long interval) {
        return DiaxScheduler.scheduleRepeating(this, name, delay, interval);
    }

    public boolean cancel() {
        return DiaxScheduler.cancelTask(name);
    }
}