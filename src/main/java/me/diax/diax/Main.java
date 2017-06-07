package me.diax.diax;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Created by Comportment on 07/06/2017 at 19:15
 * If you don't understand this, we are screwed.
 */
public class Main implements ComponentProvider, Module {

    private Injector injector;

    private Main() {
        injector = Guice.createInjector(this);
    }

    @Override
    public <T> T getInstance(Class<T> type) {
        return injector.getInstance(type);
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(ComponentProvider.class).toInstance(this);
    }
}