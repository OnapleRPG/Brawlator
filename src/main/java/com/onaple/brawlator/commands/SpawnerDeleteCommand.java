package com.onaple.brawlator.commands;

import com.flowpowered.math.vector.Vector3d;
import com.onaple.brawlator.Brawlator;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class SpawnerDeleteCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("You must be a player to create a spawner !"));
            return CommandResult.empty();
        }
        Player player = (Player)src;

        Vector3d playerPosition = player.getLocation().getPosition();
        int deletedSpawnerCount = Brawlator.getSpawnerAction().removeSpawnersAround(playerPosition);

        src.sendMessage(Text.of("Deleted " + deletedSpawnerCount + " spawners."));
        Brawlator.getSpawnerAction().updateSpawners();
        return CommandResult.success();
    }
}
