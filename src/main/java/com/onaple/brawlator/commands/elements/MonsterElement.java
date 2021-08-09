package com.onaple.brawlator.commands.elements;

import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.ItemBean;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class MonsterElement extends CommandElement {
    public MonsterElement(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String id = args.next();
        return Brawlator.getConfigurationHandler().getMonsterList()
                .stream()
                .filter(monsterBean -> monsterBean.getName().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new ArgumentParseException(Text.of("Id not found"), id, 1));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        try {
            String id = args.next();
            return Brawlator.getConfigurationHandler().getMonsterList().stream().map(MonsterBean::getName).
                    filter(s -> s.contains(id))
                    .collect(Collectors.toList());
        } catch (ArgumentParseException e) {
            return Brawlator.getConfigurationHandler().getMonsterList()
                    .stream()
                    .map(MonsterBean::getName).collect(Collectors.toList());
        }
    }

    @Override
    public Text getUsage(CommandSource src) {
        return Text.of("<monster>");
    }
}
