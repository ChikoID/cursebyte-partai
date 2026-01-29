package com.cursebyte.plugin.command.partai;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.economy.EconomyService;
import com.cursebyte.plugin.modules.reputation.ReputationService;
import com.cursebyte.plugin.utils.MessageUtils;

public class CheckCommand {
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();

        double reputasi = ReputationService.get(playerUuid);
        double money = EconomyService.getBalance(playerUuid);

        MessageUtils.sendInfo(sender, "Reputasi :" + reputasi);
        MessageUtils.sendInfo(sender, "Saldo :" + money);

    }
}
