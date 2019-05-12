package com.onaple.brawlator.commands;

import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.SpawnerAction;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.utils.ItemBuilder;
import com.onaple.itemizer.utils.PoolFetcher;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Optional;

/**
 * Player command to fetch an item from a pool defined in a configuration file
 */
public class MonstroVisionCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("You must be a player to enable monstro-vision !"));
            return CommandResult.empty();
        }
        Player player = (Player)src;

        SpawnerAction spawnerAction = Brawlator.getSpawnerAction();
        boolean playerHasVisionEnabled = spawnerAction.isPlayerRegisteredToVision(player);
        if (playerHasVisionEnabled) {
            spawnerAction.unregisterPlayerToVision(player);
            player.sendMessage(Text.of("Monstrovision disabled."));
        } else {
            spawnerAction.registerPlayerToVision(player);
            player.sendMessage(Text.of("Monstrovision enabled."));
        }

        return CommandResult.success();
    }
}
