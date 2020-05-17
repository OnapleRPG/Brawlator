package com.onaple.brawlator.commands;

import com.flowpowered.math.vector.Vector3d;
import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.data.beans.MonsterBean;
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
        Optional<MonsterBean> monster = args.getOne("monster");
        if (!monster.isPresent()) {
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
        Brawlator.getMonsterAction().invokeMonster(location, monster.get(), -1);


        return CommandResult.success();
    }
}
