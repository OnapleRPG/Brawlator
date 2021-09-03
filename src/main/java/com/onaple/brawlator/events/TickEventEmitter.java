package com.onaple.brawlator.events;

import com.onaple.brawlator.Brawlator;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Singleton
public class TickEventEmitter {

    @Inject
    private PluginContainer plugin;

    public void createTask(){
        Task task = Task.builder().execute(() -> Brawlator.getLogger().info("Yay! Schedulers!"))
                .async().delay(100, TimeUnit.MILLISECONDS).interval(5, TimeUnit.MINUTES)
                .name("ExamplePlugin - Fetch Stats from Database").submit(plugin);
    }
}
