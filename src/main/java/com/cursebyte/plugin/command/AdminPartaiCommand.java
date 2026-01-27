package com.cursebyte.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.command.admin.ListCommand;
import com.cursebyte.plugin.command.admin.ResetCommand;
import com.cursebyte.plugin.utils.MessageUtils;

public class AdminPartaiCommand implements CommandExecutor {
    private final PartaiCore plugin;

    private final ResetCommand resetCommand;
    private final ListCommand listCommand;
    private final BalanceCommand balanceCommand;
    private final BubarkanCommand bubarkanCommand;
    private final SetCommand setCommand;


    public AdminPartaiCommand(PartaiCore plugin) {
        this.plugin = plugin;
        this.resetCommand = new ResetCommand(plugin);
        this.listCommand = new ListCommand(plugin);
        this.balanceCommand = new BalanceCommand(plugin);
        this.bubarkanCommand = new BubarkanCommand(plugin);
        this.setCommand = new SetCommand(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendError(sender, "Perintah ini hanya bisa digunakan oleh player!");
            return true;
        }

        Player player = (Player) sender;
        if (player.hasPermission("partai.admin") == false) {
            MessageUtils.sendError(sender, "Kamu tidak memiliki izin untuk menggunakan perintah ini.");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "reload" -> {
                plugin.reloadConfig();
                MessageUtils.sendSuccess(sender, "Konfigurasi partai telah dimuat ulang.");
                plugin.getLogger().info("Konfigurasi partai telah dimuat ulang oleh " + sender.getName());
            }

            case "reset" -> resetCommand.execute(sender, args);
            case "list" -> listCommand.execute(sender, args);
            case "balance" -> balanceCommand.execute(sender, args);
            case "bubarkan" -> bubarkanCommand.execute(sender, args);
            case "set" -> setCommand.execute(sender, args);
            default -> MessageUtils.sendError(sender, "Perintah admin partai tidak dikenal.");
        }

        return true;
    }
}
