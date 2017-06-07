package me.diax.diax;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.listeners.MessageListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;

/**
 *
 * @author Comportment
 * @since 2.0.0
 */
public class Main implements ComponentProvider, Module {

    private Injector injector;
    private String token = Util.TOKEN;
    private CommandHandler handler;

    private Main() {
        handler = new CommandHandler();
        handler.registerCommands();
        injector = Guice.createInjector(this);
    }

    /**
     *
     * @param args Args provided by the compiler.
     * @author Comportment
     * @since 2.0.0
     */
    public static void main(String[] args) {
        new Main().main();
    }

    /**
     *
     * @author Comportment
     * @since 2.0.0
     */
    public void main() {
        int amount = Util.getRecommendedShards(token);
        JDA[] shards = new JDA[amount >= 3 ? amount : 1];
        for (int i = 1; i < shards.length; i++) {
            JDA jda = null;
            JDABuilder builder = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .setAudioEnabled(true)
                    .setGame(Game.of("owo what's this", "https://twitch.tv/comportment"))
                    .addEventListener(this.getInstance(MessageListener.class));
            if (shards.length >= 3) {
                builder = builder.useSharding(i, shards.length);
            }
            try {
                jda = builder.buildBlocking();
            } catch (LoginException|InterruptedException|RateLimitedException ignored){}
            shards[i] = jda;
        }
        ShardManager.SHARDS = shards;
    }

    /**
     *
     * @param type The type of the class.
     * @author Comportment
     * @since 2.0.0
     */
    @Override
    public <T> T getInstance(Class<T> type) {
        return injector.getInstance(type);
    }

    /**
     *
     * @param binder The {@link Binder} to bind.
     * @author Comportment
     * @since 2.0.0
     */
    @Override
    public void configure(Binder binder) {
        binder.bind(ComponentProvider.class).toInstance(this);
        binder.bind(CommandHandler.class).toInstance(handler);
    }
}