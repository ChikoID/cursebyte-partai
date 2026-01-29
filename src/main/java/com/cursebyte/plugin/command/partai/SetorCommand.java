package com.cursebyte.plugin.command.partai;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.economy.EconomyService;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class SetorCommand {
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();

        if (args.length < 2) {
            MessageUtils.sendError(sender, "Usage: /partai setor <jumlah>");
            return true;
        }

        // Parse amount
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            MessageUtils.sendError(sender, "Jumlah harus berupa angka!");
            return true;
        }

        // Validasi amount positif
        if (amount <= 0) {
            MessageUtils.sendError(sender, "Jumlah harus lebih dari 0!");
            return true;
        }

        // Validasi: player harus di partai
        UUID partaiUuid = MemberService.getPartaiUuid(playerUuid);
        if (partaiUuid == null) {
            MessageUtils.sendError(sender, "Kamu belum bergabung dengan partai!");
            return true;
        }

        // Validasi: player punya saldo cukup
        if (!EconomyService.has(playerUuid, amount)) {
            MessageUtils.sendError(sender, "Saldo kamu tidak cukup!");
            MessageUtils.sendInfo(sender, "Saldo: " + MessageUtils.formatCurrency(EconomyService.getBalance(playerUuid)));
            return true;
        }

        // Get party data
        PartaiData partai = PartaiService.getPartai(partaiUuid);
        if (partai == null) {
            MessageUtils.sendError(sender, "Data partai tidak ditemukan!");
            return true;
        }

        // Proses transaksi
        if (!EconomyService.withdraw(playerUuid, amount)) {
            MessageUtils.sendError(sender, "Gagal memotong saldo kamu!");
            return true;
        }

        // Update saldo partai
        double newBalance = partai.getBalance() + amount;
        PartaiService.updateBalance(partaiUuid, newBalance);

        // Notifikasi
        MessageUtils.sendSuccess(sender, "Berhasil menyetor " + MessageUtils.formatCurrency(amount) + " ke partai " + partai.getName());
        MessageUtils.sendInfo(sender, "Saldo partai sekarang: " + MessageUtils.formatCurrency(newBalance));

        return true;
    }
}
