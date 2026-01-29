package com.cursebyte.plugin.command.partai;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.utils.MessageUtils;

public class UndangCommand {
    private final PartaiCore plugin;
    private final Map<String, Map<String, Long>> invitations = new HashMap<>();

    public UndangCommand(PartaiCore plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        int inviteExpiration = plugin.getConfig().getInt("invite-expiration", 10);

        if (args.length < 2) {
            MessageUtils.sendError(sender, "Usage: /partai undang <playername>");
        }

        


    }
}
