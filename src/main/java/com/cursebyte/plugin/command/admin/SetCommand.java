package com.cursebyte.plugin.command.admin;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;

import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiManager;
import com.cursebyte.plugin.utils.MessageUtils;

public class SetCommand {
    private static final List<String> VALID_ROLES = Arrays.asList(
            "ketua", "wakil_ketua", "bendahara", "sekretaris", "anggota");

    public void execute(CommandSender sender, String[] args) {
        if (args.length < 4) {
            MessageUtils.sendError(sender, "Gunakan: /adminpartai set <partai> <member> <role>");
            return;
        }

        String partaiName = args[1];
        String memberName = args[2];
        String role = args[3].toLowerCase();

        if (!VALID_ROLES.contains(role)) {
            MessageUtils.sendError(sender, "Role tidak valid! Pilih salah satu:");
            MessageUtils.sendInfo(sender, String.join(", ", VALID_ROLES));
            return;
        }

        // Implementasi logika untuk menetapkan peran kepada anggota partai
        UUID partaiUUID = PartaiManager.getUuidByName(partaiName);
        if (partaiUUID == null) {
            MessageUtils.sendError(sender, "Partai dengan nama '" + partaiName + "' tidak ditemukan.");
            return;
        }

        UUID memberUUID = MemberService.getUuidByName(memberName);
        if (memberUUID == null) {
            MessageUtils.sendError(sender, "Member dengan nama '" + memberName + "' tidak ditemukan.");
            return;
        }

        UUID currentPartai = MemberService.getPartaiUuid(memberUUID);
        if (currentPartai == null || !currentPartai.equals(partaiUUID)) {
            MessageUtils.sendError(sender, memberName + " bukan anggota partai " + partaiName + ".");
            return;
        }

        MemberService.updateRoleInPartai(memberUUID, partaiUUID, role);
        MessageUtils.sendSuccess(sender, "Role " + memberName + " di partai " + partaiName
                + " berhasil diubah menjadi " + role.toUpperCase() + "!");
    }
}
