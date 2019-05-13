package com.onaple.brawlator.commands;

import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.actions.SpawnerAction;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

/**
 * Player command to fetch an item from a pool defined in a configuration file
 */
public class ViewCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("You must be a player to enable monstro-vision !"));
            return CommandResult.empty();
        }
        Player player = (Player)src;
        Optional<Boolean> value = args.getOne("value");

        SpawnerAction spawnerAction = Brawlator.getSpawnerAction();
        if (value.isPresent()) {
            if (value.get()) {
                spawnerAction.registerPlayerToVision(player);
                player.sendMessage(Text.of("Monstrovision enabled."));
            } else {
                spawnerAction.unregisterPlayerToVision(player);
                player.sendMessage(Text.of("Monstrovision disabled."));
            }
        } else {
            boolean playerHasVisionEnabled = spawnerAction.isPlayerRegisteredToVision(player);
            if (playerHasVisionEnabled) {
                spawnerAction.unregisterPlayerToVision(player);
                player.sendMessage(Text.of("Monstrovision disabled."));
            } else {
                spawnerAction.registerPlayerToVision(player);
                player.sendMessage(Text.of("Monstrovision enabled."));
            }
        }

        return CommandResult.success();
    }
}
