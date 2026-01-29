package com.cursebyte.plugin.command.partai;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.member.MemberManager;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class InfoCommand {

    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendError(sender, "Command ini hanya bisa dipakai oleh player!");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();

        // Jika ada argument, ambil info partai berdasarkan nama
        PartaiData partaiData;
        if (args.length >= 2) {
            String partaiName = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
            partaiData = PartaiService.getPartaiByName(partaiName);
            if (partaiData == null) {
                MessageUtils.sendError(sender, "Partai '" + partaiName + "' tidak ditemukan!");
                return true;
            }
        } else {
            // Ambil info partai pemain itu sendiri
            UUID partaiUuid = MemberManager.getPartaiUuid(playerUuid);
            if (partaiUuid == null) {
                MessageUtils.sendError(sender, "Kamu belum bergabung dengan partai!");
                return true;
            }
            partaiData = PartaiService.getPartai(partaiUuid);
            if (partaiData == null) {
                MessageUtils.sendError(sender, "Data partai tidak ditemukan!");
                return true;
            }
        }

        // Display party info
        displayPartaiInfo(sender, partaiData);
        return true;
    }

    private void displayPartaiInfo(CommandSender sender, PartaiData partai) {
        MessageUtils.sendHeader(sender, partai.getName());

        // Format creation date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String createdDate = sdf.format(new Date(partai.getCreatedAt()));

        // Get leader name
        String leaderName = "Unknown";
        Player leader = Bukkit.getPlayer(partai.getLeaderUuid());
        if (leader != null) {
            leaderName = leader.getName();
        }

        // Get member count
        int memberCount = MemberManager.getMembersByPartaiUuid(partai.getUuid()).size();

        // Display info
        MessageUtils.sendRaw(sender, "");
        MessageUtils.sendRaw(sender, "  Nama: " + partai.getName());
        MessageUtils.sendRaw(sender, "  Singkatan: " + partai.getShortName());
        MessageUtils.sendRaw(sender, "  Ketua: " + leaderName);
        MessageUtils.sendRaw(sender, "  Balance: " + MessageUtils.formatCurrency(partai.getBalance()));
        MessageUtils.sendRaw(sender, "  Reputasi: " + partai.getReputation());
        MessageUtils.sendRaw(sender, "  Jumlah Anggota: " + memberCount);
        MessageUtils.sendRaw(sender, "  Dibuat: " + createdDate);
        MessageUtils.sendRaw(sender, "");
        MessageUtils.sendFooter(sender);
    }
}
