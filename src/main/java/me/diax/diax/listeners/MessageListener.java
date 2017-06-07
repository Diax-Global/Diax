package me.diax.diax.listeners;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.Util;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.inject.Inject;
import java.util.regex.Pattern;

/**
 *
 * @author Comportment
 * @version 2.0.0
 */
public class MessageListener extends ListenerAdapter {

    private final CommandHandler handler;

    @Inject
    public MessageListener(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String content = event.getMessage().getRawContent();
        String prefix = null;
        if (content.startsWith(Util.PREFIX)) {
            prefix = Util.PREFIX;
        } else if (content.startsWith(event.getJDA().getSelfUser().getAsMention())) {
            prefix = event.getJDA().getSelfUser().getAsMention();
        } //TODO: Get prefix from database.
        if (prefix == null && event.getChannelType().isGuild()) return; //The message must have a prefix in a non private channel.
        prefix = "";
        content = content.replaceFirst(Pattern.quote(prefix), "").trim();
        Command command = handler.findCommand(content.split(" ")[0]);
        if (command == null) {
            return; //The command does not exist/is not registered
            //TODO: Custom command support.
        }
        if (!command.hasAttribute("allowPrivate") && !event.getChannelType().isGuild()) {
            return; //The command can not be used in private messages.
        }
        if (command.hasAttribute("developerOnly") && !event.getAuthor().getId().equals("293884638101897216")) {
            return; //The user is not Comportment
        }
        handler.execute(command, event.getMessage(), content);
    }
}