package me.diax.diax;

/**
 *
 * @author Comportment
 * @since 2.0.0
 */
public interface ComponentProvider {

    /**
     *
     * @param type The type of class.
     * @param <T> The type of class.
     * @return An instance of T.
     * @author Comportment
     * @since 2.0.0
     */
    <T> T getInstance(Class<T> type);
}