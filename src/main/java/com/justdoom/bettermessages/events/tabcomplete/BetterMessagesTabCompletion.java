package com.justdoom.bettermessages.events.tabcomplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BetterMessagesTabCompletion implements TabCompleter {

    List<String> arguments = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(arguments.isEmpty()){
            arguments.add("reload");
            arguments.add("help");
        }

        List<String> result = new ArrayList<String>();

        if(args.length == 1){
            for(String a:arguments){
                if(a.toLowerCase().startsWith(args[0].toLowerCase())){
                    result.add(a);
                }
            }
            return result;
        }

        return null;
    }
}
