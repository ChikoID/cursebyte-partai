package com.cursebyte.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.command.partai.BuatCommand;
import com.cursebyte.plugin.command.partai.EditCommand;
import com.cursebyte.plugin.command.partai.HapusCommand;
import com.cursebyte.plugin.command.partai.HelpCommand;
import com.cursebyte.plugin.command.partai.InfoCommand;
import com.cursebyte.plugin.utils.MessageUtils;

public class PartaiCommand implements CommandExecutor {
    // private final PartaiCore plugin;

    private final HelpCommand helpCommand;

    // Command modules
    private final BuatCommand buatCommand;
    private final HapusCommand hapusCommand;
    private final EditCommand editCommand;
    private final InfoCommand infoCommand;

    public PartaiCommand(PartaiCore plugin) {
        // this.plugin = plugin;
        this.helpCommand = new HelpCommand();
        this.buatCommand = new BuatCommand(plugin);
        this.hapusCommand = new HapusCommand(plugin);
        this.editCommand = new EditCommand();
        this.infoCommand = new InfoCommand();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendError(sender, "Perintah ini hanya bisa digunakan oleh player!");
            return true;
        }
        
        if (args.length == 0)
            return true;

        switch (args[0].toLowerCase()) {
            case "help" -> helpCommand.execute(sender, args);
            case "buat" -> buatCommand.execute(sender, args);
            case "hapus" -> hapusCommand.execute(sender, args);
            case "edit" -> editCommand.execute(sender, args);
            case "info" -> infoCommand.execute(sender, args);
        }
        return true;
    }
}
