package com.onaple.brawlator;

import com.onaple.brawlator.events.BrawlatorEntityDiedEvent;
import org.spongepowered.api.event.EventListener;

public class MonsterDiedListener implements EventListener<BrawlatorEntityDiedEvent> {
    @Override
    public void handle(BrawlatorEntityDiedEvent event) throws Exception {

        // Brawlator.getLogger().info("event handled {}, {}",event.getEntity().get(BrawlatorKeys.EXPERIENCE), event.getEntity().get(BrawlatorKeys.LOOT));
    }
}
