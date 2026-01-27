package com.cursebyte.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.command.partai.BuatCommand;
import com.cursebyte.plugin.command.partai.HelpCommand;

public class PartaiCommand implements CommandExecutor {
    // private final PartaiCore plugin;

    private final HelpCommand helpCommand;

    // Command modules
    private final BuatCommand buatCommand;

    public PartaiCommand(PartaiCore plugin) {
        // this.plugin = plugin;
        this.helpCommand = new HelpCommand();
        this.buatCommand = new BuatCommand(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0)
            return true;

        switch (args[0].toLowerCase()) {
            case "help" -> helpCommand.execute(sender, args);
            case "buat" -> buatCommand.execute(sender, args);
        }
        return true;
    }
}
