package com.cursebyte.plugin.command.admin;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.citizen.CitizenService;
import com.cursebyte.plugin.modules.member.MemberManager;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class LeaderCommand {
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 4) {
            MessageUtils.sendError(sender, "Gunakan: /adminpartai leader <partai> <player_A> <player_B>");
            return;
        }

        String partaiName = args[1];
        
        // Cari partai
        PartaiData partai = PartaiService.getPartaiByName(partaiName);
        if (partai == null) {
            MessageUtils.sendError(sender, "Partai dengan nama '" + partaiName + "' tidak ditemukan.");
            return;
        }

        UUID partaiUUID = partai.getUuid();
        
        // Parse dua player name dari belakang ke depan
        // Try every possible split: args[2:i] = playerA, args[i:end] = playerB
        Player playerA = null;
        String playerAName = null;
        Player playerB = null;
        String playerBName = null;

        for (int i = args.length - 1; i >= 3; i--) {
            String candidateBName = String.join(" ", Arrays.copyOfRange(args, i, args.length)).trim();
            String candidateAName = String.join(" ", Arrays.copyOfRange(args, 2, i)).trim();
            
            if (candidateBName.isEmpty() || candidateAName.isEmpty()) {
                continue;
            }
            
            // Cari player B
            Player foundPlayerB = Bukkit.getServer().getOnlinePlayers().stream()
                    .filter(p -> {
                        var profile = CitizenService.getProfile(p.getUniqueId());
                        return profile != null && profile.getFullName().equalsIgnoreCase(candidateBName);
                    })
                    .findFirst()
                    .orElse(null);
            
            if (foundPlayerB == null) {
                continue;
            }
            
            // Cari player A
            Player foundPlayerA = Bukkit.getServer().getOnlinePlayers().stream()
                    .filter(p -> {
                        var profile = CitizenService.getProfile(p.getUniqueId());
                        return profile != null && profile.getFullName().equalsIgnoreCase(candidateAName);
                    })
                    .findFirst()
                    .orElse(null);
            
            if (foundPlayerA == null) {
                continue;
            }
            
            playerA = foundPlayerA;
            playerAName = candidateAName;
            playerB = foundPlayerB;
            playerBName = candidateBName;
            break;
        }

        if (playerA == null || playerB == null) {
            MessageUtils.sendError(sender, "Salah satu atau kedua player tidak ditemukan!");
            return;
        }

        UUID currentLeaderUUID = playerA.getUniqueId();
        UUID newLeaderUUID = playerB.getUniqueId();

        // Validasi player A adalah leader saat ini
        if (!partai.getLeaderUuid().equals(currentLeaderUUID)) {
            MessageUtils.sendError(sender, playerAName + " bukan leader partai " + partaiName + ".");
            return;
        }

        // Validasi player B adalah member partai
        UUID playerBPartai = MemberService.getPartaiUuid(newLeaderUUID);
        if (playerBPartai == null || !playerBPartai.equals(partaiUUID)) {
            MessageUtils.sendError(sender, playerBName + " bukan anggota partai " + partaiName + ".");
            return;
        }

        // Validasi player A dan B berbeda
        if (currentLeaderUUID.equals(newLeaderUUID)) {
            MessageUtils.sendError(sender, "Tidak bisa transfer leader ke diri sendiri!");
            return;
        }

        // Update leader di database
        PartaiService.updateLeader(partaiUUID, newLeaderUUID);
        
        // Update role leader lama (player A) menjadi anggota
        MemberService.updateRoleInPartai(currentLeaderUUID, partaiUUID, "anggota");
        
        // Update role leader baru (player B) menjadi ketua
        MemberService.updateRoleInPartai(newLeaderUUID, partaiUUID, "ketua");
        
        MessageUtils.sendSuccess(sender, "Leadership partai " + partaiName + " ditransfer dari " + playerAName + " ke " + playerBName);
        
        Player oldLeaderPlayer = Bukkit.getPlayer(currentLeaderUUID);
        if (oldLeaderPlayer != null && oldLeaderPlayer.isOnline()) {
            MessageUtils.sendInfo(oldLeaderPlayer, "Kamu bukan lagi leader partai " + partaiName);
        }
        
        Player newLeaderPlayer = Bukkit.getPlayer(newLeaderUUID);
        if (newLeaderPlayer != null && newLeaderPlayer.isOnline()) {
            MessageUtils.sendSuccess(newLeaderPlayer, "Selamat! Kamu sekarang menjadi ketua partai " + partaiName);
        }
    }
}
