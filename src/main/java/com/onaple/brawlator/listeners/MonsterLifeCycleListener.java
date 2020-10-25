package com.onaple.brawlator.listeners;

import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.actions.MonsterAction;
import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.brawlator.exceptions.MonsterNotFoundException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@Singleton
public class MonsterLifeCycleListener {

    @Inject
    private MonsterAction monsterAction;

    @Listener
    public void onDeath(DestructEntityEvent.Death event, @First Entity entity,@First Player player) throws InstantiationException, IllegalAccessException, MonsterNotFoundException, NoSuchMethodException, InvocationTargetException {
        Optional<String> name = entity.get(Keys.DISPLAY_NAME).map(Text::toPlain);
        if(name.isPresent()){
            MonsterBean monster = monsterAction.getMonster(name.get());
            Event onDeath = (Event) monster.getEvents().get("onDeath").getConstructor(Entity.class, Player.class, Cause.class).newInstance(entity, player, event.getCause());
            Brawlator.getLogger().info("emit event {}", onDeath );
            Sponge.getEventManager().post(onDeath);
        }
    }
}
