package com.cursebyte.plugin.command.partai;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class SaldoCommand {
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendError(sender, "Command ini hanya bisa dipakai oleh player!");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();

        // Jika ada argument, cek saldo partai lain
        PartaiData partai;
        if (args.length >= 2) {
            String partaiName = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
            partai = PartaiService.getPartaiByName(partaiName);
            if (partai == null) {
                MessageUtils.sendError(sender, "Partai '" + partaiName + "' tidak ditemukan!");
                return true;
            }
        } else {
            // Default: cek saldo partai sendiri
            UUID partaiUuid = MemberService.getPartaiUuid(playerUuid);
            if (partaiUuid == null) {
                MessageUtils.sendError(sender, "Kamu belum bergabung dengan partai!");
                return true;
            }
            partai = PartaiService.getPartai(partaiUuid);
            if (partai == null) {
                MessageUtils.sendError(sender, "Data partai tidak ditemukan!");
                return true;
            }
        }

        // Display balance
        MessageUtils.sendHeader(sender, "Saldo Partai");
        MessageUtils.sendRaw(sender, "");
        MessageUtils.sendRaw(sender, "  Partai: " + partai.getName() + " (" + partai.getShortName() + ")");
        MessageUtils.sendRaw(sender, "  Saldo: " + MessageUtils.formatCurrency(partai.getBalance()));
        MessageUtils.sendRaw(sender, "");
        MessageUtils.sendFooter(sender);

        return true;
    }
}
