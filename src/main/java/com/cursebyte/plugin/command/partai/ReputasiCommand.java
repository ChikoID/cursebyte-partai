package com.cursebyte.plugin.command.partai;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

import net.kyori.adventure.text.format.NamedTextColor;

public class ReputasiCommand {
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendError(sender, "Command ini hanya bisa dipakai oleh player!");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();

        // Jika ada argument, cek reputasi partai lain
        PartaiData partai;
        if (args.length >= 2) {
            String partaiName = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
            partai = PartaiService.getPartaiByName(partaiName);
            if (partai == null) {
                MessageUtils.sendError(sender, "Partai '" + partaiName + "' tidak ditemukan!");
                return true;
            }
        } else {
            // Default: cek reputasi partai sendiri
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

        // Display reputation
        displayReputation(sender, partai);

        return true;
    }

    private void displayReputation(CommandSender sender, PartaiData partai) {
        double reputation = Math.min(partai.getReputation(), 1.0);
        String tier = getReputationTier(reputation);
        NamedTextColor tierColor = getReputationColor(reputation);

        MessageUtils.sendHeader(sender, "Reputasi Partai");
        MessageUtils.sendRaw(sender, "");
        MessageUtils.sendRaw(sender, "  Partai: " + partai.getName() + " (" + partai.getShortName() + ")");
        MessageUtils.sendRaw(sender, "");
        MessageUtils.sendRaw(sender, "  Reputasi: " + String.format("%.2f", reputation), tierColor);
        MessageUtils.sendRaw(sender, "  Tingkat: " + tier, tierColor);
        MessageUtils.sendRaw(sender, "");
        
        // Progress bar
        displayReputationBar(sender, reputation);
        
        MessageUtils.sendRaw(sender, "");
        MessageUtils.sendRaw(sender, "  Info:", NamedTextColor.GRAY);
        MessageUtils.sendRaw(sender, "  - Reputasi partai berbeda dengan reputasi citizen", NamedTextColor.GRAY);
        MessageUtils.sendRaw(sender, "  - Reputasi partai didapat dari aktivitas bersama", NamedTextColor.GRAY);
        MessageUtils.sendRaw(sender, "");
        MessageUtils.sendFooter(sender);
    }

    private void displayReputationBar(CommandSender sender, double reputation) {
        int maxBars = 20;
        int filledBars = (int) Math.round(Math.min(maxBars, Math.max(0, reputation * maxBars)));
        int emptyBars = maxBars - filledBars;

        StringBuilder bar = new StringBuilder("  [");
        
        // Filled portion (green)
        for (int i = 0; i < filledBars; i++) {
            bar.append("█");
        }
        
        // Empty portion
        for (int i = 0; i < emptyBars; i++) {
            bar.append("░");
        }
        
        bar.append("]");
        
        MessageUtils.sendRaw(sender, bar.toString(), getReputationColor(reputation));
    }

    private String getReputationTier(double reputation) {
        if (reputation >= 0.90) return "Legendaris";
        if (reputation >= 0.75) return "Terkenal";
        if (reputation >= 0.50) return "Terpercaya";
        if (reputation >= 0.25) return "Dikenal";
        if (reputation >= 0.10) return "Pemula";
        if (reputation >= 0.0) return "Baru";
        return "Tercela";
    }

    private NamedTextColor getReputationColor(double reputation) {
        if (reputation >= 0.75) return NamedTextColor.GOLD;
        if (reputation >= 0.50) return NamedTextColor.GREEN;
        if (reputation >= 0.25) return NamedTextColor.YELLOW;
        if (reputation >= 0.0) return NamedTextColor.GRAY;
        return NamedTextColor.RED;
    }
}
