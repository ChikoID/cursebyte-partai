package com.cursebyte.plugin.command.admin;

import java.util.UUID;

import org.bukkit.command.CommandSender;

import com.cursebyte.plugin.modules.partai.PartaiManager;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class BalanceCommand {
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            MessageUtils.sendError(sender, "Gunakan: /adminpartai balance <nama_partai> <jumlah>");
            return;
        }

        String partaiName = args[1];
        UUID partaiUUID = PartaiManager.getUuidByName(partaiName);
        if (partaiUUID == null) {
            MessageUtils.sendError(sender, "Partai dengan nama '" + partaiName + "' tidak ditemukan.");
        }

        double amount = 0; // Initialize amount with a default value
        try {
            amount = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            MessageUtils.sendError(sender, "Jumlah tidak valid: " + args[2]);
            return; // Exit the method if the amount is invalid
        }

        Double currentBalance = PartaiManager.getBalance(partaiUUID);
        if (currentBalance == null) {
            MessageUtils.sendError(sender, "Gagal mengambil saldo partai.");
            return;
        }

        double newBalance = currentBalance + amount;
        PartaiService.updateBalance(partaiUUID, newBalance);

        MessageUtils.sendSuccess(sender, "Balance partai " + partaiName + " berhasil diubah!");
        MessageUtils.sendInfo(sender, "Balance sebelumnya: " + MessageUtils.formatCurrency(currentBalance));
        MessageUtils.sendInfo(sender, "Balance sekarang: " + MessageUtils.formatCurrency(newBalance));
    }
}
