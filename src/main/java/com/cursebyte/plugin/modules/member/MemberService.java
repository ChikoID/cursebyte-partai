package com.cursebyte.plugin.modules.member;

import java.util.List;
import java.util.UUID;

import com.cursebyte.plugin.modules.citizen.CitizenService;

public class MemberService {

    public static UUID getUuidByName(String fullName) {
        // Implementasi jika diperlukan
        return MemberManager.getUuidByName(fullName);
    }

    /**
     * Gabungkan data citizen dengan role partai.
     */
    public static MemberData getMemberData(UUID playerUuid) {
        // Ambil nama dari Citizen DB (read-only)
        var citizenProfile = CitizenService.getProfile(playerUuid);

        // Ambil role dari tabel partai_member sendiri
        String role = MemberManager.getRole(playerUuid);
        UUID partaiUuid = MemberManager.getPartaiUuid(playerUuid);

        return new MemberData(
                playerUuid,
                citizenProfile.getFullName(), // dari DB citizen
                partaiUuid,
                role != null ? role : "ANGGOTA");
    }

    public static void updateRoleInPartai(UUID playerUuid, UUID partaiUuid, String newRole) {
        MemberManager.updateRoleInPartai(partaiUuid, playerUuid, newRole);
    }

    public static UUID getPartaiUuid(UUID playerUuid) {
        return MemberManager.getPartaiUuid(playerUuid);
    }

    public static String getRole(UUID playerUuid) {
        return MemberManager.getRole(playerUuid);
    }

    public static List<MemberData> getMembersByPartaiUuid(UUID partaiUuid) {
        return MemberManager.getMembersByPartaiUuid(partaiUuid);
    }
}