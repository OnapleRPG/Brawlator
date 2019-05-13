package com.onaple.brawlator.commands;

import com.flowpowered.math.vector.Vector3d;
import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.exceptions.EntityTypeNotFoundException;
import com.onaple.brawlator.exceptions.MonsterNotFoundException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class InvokeCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> monsterName = args.getOne("name");
        if (!monsterName.isPresent()) {
            src.sendMessage(Text.of("You need to specify a monster name."));
            return CommandResult.empty();
        }

        // Getting right position
        Optional<Vector3d> position = args.getOne("position");
        Location location;
        if (position.isPresent()) {
            if (src instanceof Player) {
                location = new Location<>(((Player)src).getWorld(), position.get());
            } else {
                Optional<World> defaultWorld = Sponge.getServer().getWorld(Sponge.getServer().getDefaultWorldName());
                if (!defaultWorld.isPresent()) {
                    src.sendMessage(Text.of("Default world could not be found for invocation."));
                    return CommandResult.empty();
                }
                location = new Location<>(defaultWorld.get(), position.get());
            }
        } else {
            if (!(src instanceof Player)) {
                src.sendMessage(Text.of("You need to specify a position."));
                return CommandResult.empty();
            }
            location = ((Player)src).getLocation();
        }

        // Invoke monster or handles exception
        try {
            Brawlator.getMonsterAction().invokeMonster(location, monsterName.get());
        } catch (MonsterNotFoundException e) {
            src.sendMessage(Text.of("The monster with given name was not found."));
            return CommandResult.empty();
        } catch (EntityTypeNotFoundException e) {
            src.sendMessage(Text.of("The monster type was not found."));
            return CommandResult.empty();
        }

        return CommandResult.success();
    }
}
