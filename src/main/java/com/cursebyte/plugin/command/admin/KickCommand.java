package com.cursebyte.plugin.command.admin;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.citizen.CitizenService;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class KickCommand {
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            MessageUtils.sendError(sender, "Gunakan: /adminpartai kick <partai_name> <player_name>");
            return;
        }

        String partaiName = args[1];
        
        // Parse player name dari kombinasi terpanjang ke terpendek
        Player targetPlayer = null;
        String targetName = null;

        for (int i = args.length; i >= 2; i--) {
            String candidateName = String.join(" ", Arrays.copyOfRange(args, 2, i)).trim();
            
            if (candidateName.isEmpty()) {
                continue;
            }
            
            targetPlayer = Bukkit.getServer().getOnlinePlayers().stream()
                    .filter(p -> {
                        var profile = CitizenService.getProfile(p.getUniqueId());
                        return profile != null && profile.getFullName().equalsIgnoreCase(candidateName);
                    })
                    .findFirst()
                    .orElse(null);
            
            if (targetPlayer != null) {
                targetName = candidateName;
                break;
            }
        }

        if (targetPlayer == null) {
            MessageUtils.sendError(sender, "Player tidak ditemukan!");
            return;
        }

        UUID memberUUID = targetPlayer.getUniqueId();
        
        // Cari partai
        PartaiData partai = PartaiService.getPartaiByName(partaiName);
        if (partai == null) {
            MessageUtils.sendError(sender, "Partai dengan nama '" + partaiName + "' tidak ditemukan.");
            return;
        }

        UUID partaiUUID = partai.getUuid();

        // Validasi member adalah bagian dari partai
        UUID currentPartai = MemberService.getPartaiUuid(memberUUID);
        if (currentPartai == null || !currentPartai.equals(partaiUUID)) {
            MessageUtils.sendError(sender, targetName + " bukan anggota partai " + partaiName + ".");
            return;
        }

        // Validasi tidak bisa kick leader
        var memberData = MemberService.getMemberData(memberUUID);
        if (memberData != null && memberData.getRole().equalsIgnoreCase("ketua")) {
            MessageUtils.sendError(sender, "Tidak bisa kick leader partai! Gunakan /adminpartai leader untuk transfer kepemimpinan.");
            return;
        }

        // Kick member
        MemberService.removeMember(memberUUID, partaiUUID);
        
        var profile = CitizenService.getProfile(memberUUID);
        String displayName = (profile != null) ? profile.getFullName() : targetPlayer.getName();
        
        MessageUtils.sendSuccess(sender, displayName + " telah dikeluarkan dari partai " + partaiName);
        MessageUtils.sendWarning(targetPlayer, "Kamu telah dikeluarkan dari partai " + partaiName + " oleh admin");
    }
}
