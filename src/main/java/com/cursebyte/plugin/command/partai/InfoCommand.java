package com.cursebyte.plugin.command.partai;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.member.MemberManager;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiManager;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class InfoCommand {

    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();

        PartaiData partaiData;

        if (args.length < 2) {
            // Get player's own party
            UUID partaiUuid = MemberManager.getPartaiUuid(playerUuid);
            if (partaiUuid == null) {
                MessageUtils.sendError(sender, "Anda tidak memiliki partai!");
                return true;
            }
            partaiData = PartaiService.getPartai(partaiUuid);
        } else {
            // Get party by name
            String partaiName = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
            if (!PartaiManager.existsByName(partaiName)) {
                MessageUtils.sendError(sender, "Partai dengan nama tersebut tidak ditemukan!");
                return true;
            }
            partaiData = PartaiService.getPartaiByName(partaiName);
        }

        displayPartaiInfo(sender, partaiData);
        return true;
    }

    public void displayPartaiInfo(CommandSender sender, PartaiData partai) {
        MessageUtils.sendHeader(sender, partai.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String createdDate = sdf.format(new Date(partai.getCreatedAt() * 1000));
        String leaderName = "Unknown";
        Player leader = Bukkit.getPlayer(partai.getLeaderUuid());
        if (leader != null) {
            leaderName = leader.getName();
        }

        int memberCount = MemberManager.getMembersByPartaiUuid(partai.getUuid()).size();
        MessageUtils.sendRaw(sender, "");
        MessageUtils.sendRaw(sender, "  Nama: " + partai.getName());
        MessageUtils.sendRaw(sender, "  Singkatan: " + partai.getShortName());
        MessageUtils.sendRaw(sender, "  Ketua: " + leaderName);
        MessageUtils.sendRaw(sender, "  Balance: " + MessageUtils.formatCurrency(partai.getBalance()));
        MessageUtils.sendRaw(sender, "  Reputasi: " + String.format("%.2f", Math.min(partai.getReputation(), 1.0)));
        MessageUtils.sendRaw(sender, "  Jumlah Anggota: " + memberCount);
        MessageUtils.sendRaw(sender, "  Dibuat: " + createdDate);
        MessageUtils.sendRaw(sender, "");
        MessageUtils.sendFooter(sender);
    }
}
