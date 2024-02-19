package com.imjustdoom.bettermessages;

import com.imjustdoom.cmdinstruction.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Command extends com.imjustdoom.cmdinstruction.Command {

    public Command() {
        setSubcommands(new SubCmd().setName("no"));

        List<String> tabCompletions = new ArrayList<>();
        for (SubCommand subCommand : getSubcommands()) {
            tabCompletions.add(subCommand.getName());
        }
        setTabCompletions(tabCompletions);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        BetterMessages.getInstance().getConfig().set(strings[0], strings[1]);
        BetterMessages.getInstance().getConfig().options().header("test header i guess " + strings[0]);
        BetterMessages.getInstance().saveConfig();
    }
}
