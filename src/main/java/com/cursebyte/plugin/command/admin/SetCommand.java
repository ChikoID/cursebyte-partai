package com.cursebyte.plugin.command.admin;

import java.util.Arrays;
import java.util.List;
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

public class SetCommand {
    private static final List<String> VALID_ROLES = Arrays.asList(
            "ketua", "wakil_ketua", "bendahara", "sekretaris", "anggota");

    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            MessageUtils.sendError(sender, "Gunakan: /adminpartai set <partai_name> <player_name> <role>");
            MessageUtils.sendInfo(sender, "Role tersedia: " + String.join(", ", VALID_ROLES));
            return;
        }

        // Parse arguments dengan cara cari player dari yang terpanjang ke terpendek
        String partaiName = args[1];
        Player targetPlayer = null;
        String role = null;

        // Coba cari player dari kombinasi nama terpanjang ke terpendek
        for (int i = args.length - 1; i >= 2; i--) {
            String candidateName = String.join(" ", Arrays.copyOfRange(args, 2, i)).trim();
            String candidateRole = args[i].toLowerCase();
            
            // Validasi role dulu
            if (VALID_ROLES.contains(candidateRole)) {
                // Coba cari player dengan nama ini
                targetPlayer = Bukkit.getServer().getOnlinePlayers().stream()
                        .filter(p -> {
                            var profile = CitizenService.getProfile(p.getUniqueId());
                            return profile != null && profile.getFullName().equalsIgnoreCase(candidateName);
                        })
                        .findFirst()
                        .orElse(null);
                
                if (targetPlayer != null) {
                    role = candidateRole;
                    break;
                }
            }
        }

        if (targetPlayer == null || role == null) {
            MessageUtils.sendError(sender, "Player atau role tidak ditemukan!");
            MessageUtils.sendInfo(sender, "Gunakan: /adminpartai set <partai_name> <player_name> <role>");
            return;
        }

        UUID memberUUID = targetPlayer.getUniqueId();
        
        // Cari partai berdasarkan nama
        PartaiData partai = PartaiService.getPartaiByName(partaiName);
        if (partai == null) {
            MessageUtils.sendError(sender, "Partai dengan nama '" + partaiName + "' tidak ditemukan.");
            return;
        }

        UUID partaiUUID = partai.getUuid();

        // Validasi member adalah bagian dari partai
        UUID currentPartai = MemberService.getPartaiUuid(memberUUID);
        if (currentPartai == null || !currentPartai.equals(partaiUUID)) {
            MessageUtils.sendError(sender, targetPlayer.getName() + " bukan anggota partai " + partaiName + ".");
            return;
        }

        // Cek apakah role sudah punya slot tersedia (kecuali role sama dengan sebelumnya)
        var memberData = MemberService.getMemberData(memberUUID);
        String currentRole = memberData != null ? memberData.getRole().toLowerCase() : "anggota";
        
        if (!currentRole.equals(role)) {
            int maxSlot = getMaxSlotForRole(role);
            int currentCount = MemberManager.countMembersByRole(partaiUUID, role);
            
            if (currentCount >= maxSlot) {
                MessageUtils.sendError(sender, "Slot jabatan " + role + " sudah penuh! (" + currentCount + "/" + maxSlot + ")");
                return;
            }
        }

        // Update role
        MemberService.updateRoleInPartai(memberUUID, partaiUUID, role);
        
        var profile = CitizenService.getProfile(memberUUID);
        String displayName = (profile != null) ? profile.getFullName() : targetPlayer.getName();
        
        MessageUtils.sendSuccess(sender, "Role " + displayName + " di partai " + partaiName
                + " berhasil diubah menjadi " + role.toUpperCase() + "!");
    }

    private static int getMaxSlotForRole(String role) {
        return switch (role.toLowerCase()) {
            case "ketua" -> 1;
            case "wakil_ketua" -> 1;
            case "bendahara" -> 1;
            case "sekretaris" -> 2;
            case "anggota" -> 44;
            default -> 0;
        };
    }
}
