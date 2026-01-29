package com.cursebyte.plugin.command.partai;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.modules.relasi.RelasiPartaiManager;
import com.cursebyte.plugin.utils.MessageUtils;

import net.kyori.adventure.text.format.NamedTextColor;

public class StatusCommand {
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();

        // Validasi: player harus di partai
        UUID partaiUuid = MemberService.getPartaiUuid(playerUuid);
        if (partaiUuid == null) {
            MessageUtils.sendError(sender, "Kamu belum bergabung dengan partai!");
            return true;
        }

        // Get current party
        PartaiData myPartai = PartaiService.getPartai(partaiUuid);
        if (myPartai == null) {
            MessageUtils.sendError(sender, "Data partai tidak ditemukan!");
            return true;
        }

        // Jika ada argument, cek status dengan partai tertentu
        if (args.length >= 2) {
            String targetName = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
            PartaiData targetPartai = PartaiService.getPartaiByName(targetName);
            
            if (targetPartai == null) {
                MessageUtils.sendError(sender, "Partai '" + targetName + "' tidak ditemukan!");
                return true;
            }

            displayRelationStatus(sender, myPartai, targetPartai);
        } else {
            // Display general info
            MessageUtils.sendHeader(sender, "Status Partai " + myPartai.getName());
            MessageUtils.sendRaw(sender, "");
            MessageUtils.sendRaw(sender, "  Nama: " + myPartai.getName());
            MessageUtils.sendRaw(sender, "  Tag: " + myPartai.getShortName());
            MessageUtils.sendRaw(sender, "  Reputasi: " + myPartai.getReputation());
            MessageUtils.sendRaw(sender, "");
            MessageUtils.sendInfo(sender, "Gunakan /partai status <nama_partai> untuk cek hubungan dengan partai lain");
            MessageUtils.sendRaw(sender, "");
            MessageUtils.sendFooter(sender);
        }

        return true;
    }

    private void displayRelationStatus(CommandSender sender, PartaiData myPartai, PartaiData targetPartai) {
        String relationType = RelasiPartaiManager.getRelationType(myPartai.getUuid(), targetPartai.getUuid());
        
        // Default ke NETRAL jika null
        if (relationType == null || relationType.equalsIgnoreCase("NETRAL")) {
            relationType = "NETRAL";
        }

        MessageUtils.sendHeader(sender, "Status Hubungan");
        MessageUtils.sendRaw(sender, "");
        MessageUtils.sendRaw(sender, "  Partai Kamu: " + myPartai.getName());
        MessageUtils.sendRaw(sender, "  Partai Lain: " + targetPartai.getName());
        MessageUtils.sendRaw(sender, "");
        
        // Display status dengan warna
        NamedTextColor statusColor = switch (relationType.toUpperCase()) {
            case "SEKUTU" -> NamedTextColor.GREEN;
            case "MUSUH" -> NamedTextColor.RED;
            default -> NamedTextColor.GRAY;
        };
        
        MessageUtils.sendRaw(sender, "  Status: " + getStatusEmoji(relationType) + " " + relationType, statusColor);
        MessageUtils.sendRaw(sender, "");
        
        // Info tambahan
        switch (relationType.toUpperCase()) {
            case "SEKUTU" -> MessageUtils.sendRaw(sender, "  ✓ Hubungan persahabatan", NamedTextColor.GREEN);
            case "MUSUH" -> MessageUtils.sendRaw(sender, "  ✗ Hubungan permusuhan", NamedTextColor.RED);
            default -> MessageUtils.sendRaw(sender, "  ● Tidak ada hubungan khusus", NamedTextColor.GRAY);
        }
        
        MessageUtils.sendRaw(sender, "");
        MessageUtils.sendFooter(sender);
    }

    private String getStatusEmoji(String relationType) {
        return switch (relationType.toUpperCase()) {
            case "SEKUTU" -> "⚔";
            case "MUSUH" -> "☠";
            default -> "●";
        };
    }
}
