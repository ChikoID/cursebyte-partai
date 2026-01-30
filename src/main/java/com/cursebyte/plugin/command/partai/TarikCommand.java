package com.cursebyte.plugin.command.partai;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.economy.EconomyService;
import com.cursebyte.plugin.modules.member.MemberData;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class TarikCommand {
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendError(sender, "Command ini hanya bisa dipakai oleh player!");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();

        if (args.length < 2) {
            MessageUtils.sendError(sender, "Usage: /partai tarik <jumlah>");
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

        // Validasi: hanya ketua atau bendahara yang bisa tarik
        MemberData memberData = MemberService.getMemberData(playerUuid);
        String role = memberData.getRole().toLowerCase();
        if (!role.equals("ketua") && !role.equals("bendahara")) {
            MessageUtils.sendError(sender, "Hanya ketua atau bendahara yang bisa menarik saldo partai!");
            return true;
        }

        // Get party data
        PartaiData partai = PartaiService.getPartai(partaiUuid);
        if (partai == null) {
            MessageUtils.sendError(sender, "Data partai tidak ditemukan!");
            return true;
        }

        // Validasi: saldo partai cukup
        if (partai.getBalance() < amount) {
            MessageUtils.sendError(sender, "Saldo partai tidak cukup!");
            MessageUtils.sendInfo(sender, "Saldo partai: " + MessageUtils.formatCurrency(partai.getBalance()));
            return true;
        }

        // Proses transaksi
        EconomyService.add(playerUuid, amount);

        // Update saldo partai
        double newBalance = partai.getBalance() - amount;
        PartaiService.updateBalance(partaiUuid, newBalance);

        // Notifikasi
        MessageUtils.sendSuccess(sender, "Berhasil menarik " + MessageUtils.formatCurrency(amount) + " dari partai " + partai.getName());
        MessageUtils.sendInfo(sender, "Saldo partai sekarang: " + MessageUtils.formatCurrency(newBalance));

        return true;
    }
}
