package com.cursebyte.plugin.command.admin;

import org.bukkit.command.CommandSender;

import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class ReputationCommand {
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            MessageUtils.sendError(sender, "Gunakan: /adminpartai reputation <partai_name> <+/-jumlah>");
            MessageUtils.sendInfo(sender, "Contoh: /adminpartai reputation Serikat +500");
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

        double currentReputation = partai.getReputation();
        double newReputation = currentReputation + amount;
        
        // Clamp reputation ke range 0.0-1.0
        newReputation = com.cursebyte.plugin.modules.partai.PartaiManager.clampReputation(newReputation);
        
        // Validasi agar perubahan meaningful
        if (newReputation == currentReputation) {
            MessageUtils.sendError(sender, "Perubahan reputasi tidak berpengaruh! (Sudah di max 1.0 atau min 0.0)");
            return;
        }

        PartaiService.updateReputation(partai.getUuid(), newReputation);

        String operation = amount > 0 ? "ditambahkan" : "dikurangkan";
        MessageUtils.sendSuccess(sender, "Reputasi partai " + partaiName + " berhasil diubah!");
        MessageUtils.sendInfo(sender, "Reputasi sebelumnya: " + currentReputation);
        MessageUtils.sendInfo(sender, "Perubahan: " + Math.abs(amount) + " (" + operation + ")");
        MessageUtils.sendInfo(sender, "Reputasi sekarang: " + newReputation);
    }
}
