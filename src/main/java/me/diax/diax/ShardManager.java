package me.diax.diax;

import net.dv8tion.jda.core.JDA;

/**
 *
 * @author Comportment
 * @since 2.0.0
 */
public class ShardManager {

    static JDA[] SHARDS;

    /**
     *
     * @return An Array containing {@link JDA} shards.
     * @author Comportment
     * @since 2.0.0
     */
    public JDA[] getShards() {
        return SHARDS;
    }
}