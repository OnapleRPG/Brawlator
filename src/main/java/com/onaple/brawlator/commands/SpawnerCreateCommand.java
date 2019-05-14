package com.onaple.brawlator.commands;

import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.exceptions.MonsterNotFoundException;
import com.onaple.brawlator.exceptions.SpawnerTypeNotFoundException;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class SpawnerCreateCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("You must be a player to create a spawner !"));
            return CommandResult.empty();
        }
        Player player = (Player)src;

        // Double check for missing arguments
        Optional<String> spawnerType = args.getOne("spawnerType");
        if (!spawnerType.isPresent()) {
            src.sendMessage(Text.of("You need to specify a configured spawner type name."));
            return CommandResult.empty();
        }
        Optional<String> monsterName = args.getOne("monsterName");
        if (!monsterName.isPresent()) {
            src.sendMessage(Text.of("You need to specify a configured monster name."));
            return CommandResult.empty();
        }

        try {
            Brawlator.getSpawnerAction().createSpawner(player.getLocation().getPosition(), player.getLocation().getExtent().getName(), spawnerType.get(), monsterName.get());
        } catch (MonsterNotFoundException e) {
            src.sendMessage(Text.of("The monster with given name was not found."));
            return CommandResult.empty();
        } catch (SpawnerTypeNotFoundException e) {
            src.sendMessage(Text.of("The spawner type with given name was not found."));
            return CommandResult.empty();
        }

        src.sendMessage(Text.of("Created a new spawner at your position."));
        Brawlator.getSpawnerAction().updateSpawners();
        return CommandResult.success();
    }
}
