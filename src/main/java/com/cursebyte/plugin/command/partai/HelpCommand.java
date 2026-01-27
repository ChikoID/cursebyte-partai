package com.cursebyte.plugin.command.partai;

import org.bukkit.command.CommandSender;

public class HelpCommand {
    // private final PartaiCore plugin;

    // public HelpCommand(PartaiCore plugin) {
    //     this.plugin = plugin;
    // }

    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("===== Partai Help =====");
        sender.sendMessage("/partai buat <nama> - Membuat partai baru");
        sender.sendMessage("/partai undang <player> - Mengundang pemain");
        return true;
    }
}
