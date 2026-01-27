package com.cursebyte.plugin.command.partai;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.modules.reputation.ReputationService;

public class CheckCommand {
    private final PartaiCore plugin;

    public CheckCommand(PartaiCore plugin) {
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        Double reputation = ReputationService.get(playerUUID);
        sender.sendMessage(reputation.toString());
        return true;
    }
}
