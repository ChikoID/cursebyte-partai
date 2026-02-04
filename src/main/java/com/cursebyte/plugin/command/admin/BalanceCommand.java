package com.cursebyte.plugin.command.admin;

import java.util.UUID;

import org.bukkit.command.CommandSender;

import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class BalanceCommand {
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            MessageUtils.sendError(sender, "Gunakan: /adminpartai balance <nama_partai> <+/-jumlah>");
            MessageUtils.sendInfo(sender, "Contoh: /adminpartai balance Serikat +1000 (tambah)");
            MessageUtils.sendInfo(sender, "Contoh: /adminpartai balance Serikat -500 (kurangi)");
            return;
        }

        String partaiName = args[1];
        String amountStr = args[2];
        
        // Parse jumlah dengan tanda +/-
        double amount = 0;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            MessageUtils.sendError(sender, "Jumlah tidak valid: " + amountStr);
            return;
        }

        // Cari partai
        PartaiData partai = PartaiService.getPartaiByName(partaiName);
        if (partai == null) {
            MessageUtils.sendError(sender, "Partai dengan nama '" + partaiName + "' tidak ditemukan.");
            return;
        }

        UUID partaiUUID = partai.getUuid();
        double currentBalance = partai.getBalance();
        double newBalance = currentBalance + amount;
        
        // Validasi agar balance tidak negatif
        if (newBalance < 0) {
            MessageUtils.sendError(sender, "Saldo partai tidak boleh negatif!");
            MessageUtils.sendInfo(sender, "Saldo saat ini: " + MessageUtils.formatCurrency(currentBalance));
            return;
        }

        PartaiService.updateBalance(partaiUUID, newBalance);

        String operation = amount > 0 ? "ditambahkan" : "dikurangkan";
        MessageUtils.sendSuccess(sender, "Balance partai " + partaiName + " berhasil diubah!");
        MessageUtils.sendInfo(sender, "Saldo sebelumnya: " + MessageUtils.formatCurrency(currentBalance));
        MessageUtils.sendInfo(sender, "Perubahan: " + MessageUtils.formatCurrency(Math.abs(amount)) + " (" + operation + ")");
        MessageUtils.sendInfo(sender, "Saldo sekarang: " + MessageUtils.formatCurrency(newBalance));
    }
}
