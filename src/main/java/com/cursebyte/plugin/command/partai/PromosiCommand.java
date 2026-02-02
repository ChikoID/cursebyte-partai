package com.cursebyte.plugin.command.partai;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.modules.member.MemberData;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.utils.MessageUtils;
import com.cursebyte.plugin.utils.PlayerUtils;

public class PromosiCommand {
    private final Map<String, Integer> roleLevel = new HashMap<>();

    public PromosiCommand(PartaiCore plugin) {
        // Load role levels from config
        roleLevel.put("anggota", plugin.getConfig().getInt("role.anggota.level", 1));
        roleLevel.put("sekretaris", plugin.getConfig().getInt("role.sekretaris.level", 2));
        roleLevel.put("bendahara", plugin.getConfig().getInt("role.bendahara.level", 2));
        roleLevel.put("wakil_ketua", plugin.getConfig().getInt("role.wakil_ketua.level", 3));
        roleLevel.put("ketua", plugin.getConfig().getInt("role.ketua.level", 4));
    }

    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length < 3) {
            MessageUtils.sendError(sender, "Usage: /partai promosi <playername> <jabatan_baru>");
            MessageUtils.sendInfo(sender, "Jabatan: anggota, sekretaris, bendahara, wakil_ketua");
            return true;
        }

        String targetName = String.join(" ", Arrays.copyOfRange(args, 1, args.length - 1)).trim();
        String newRole = args[args.length - 1].toLowerCase();
        if (targetName.isEmpty()) {
            MessageUtils.sendError(sender, "Usage: /partai promosi <playername> <jabatan_baru>");
            MessageUtils.sendInfo(sender, "Jabatan: anggota, sekretaris, bendahara, wakil_ketua");
            return true;
        }
        UUID playerUuid = player.getUniqueId();

        // Validasi target online
        Player targetPlayer = PlayerUtils.findOnlinePlayerByName(targetName);
        if (targetPlayer == null) {
            MessageUtils.sendError(sender, "Player '" + targetName + "' tidak sedang online!");
            return true;
        }

        UUID targetUuid = targetPlayer.getUniqueId();

        // Validasi player harus di partai
        UUID playerPartaiUuid = MemberService.getPartaiUuid(playerUuid);
        if (playerPartaiUuid == null) {
            MessageUtils.sendError(sender, "Kamu belum bergabung dengan partai!");
            return true;
        }

        // Validasi player harus ketua
        MemberData playerMember = MemberService.getMemberData(playerUuid);
        if (!playerMember.getRole().equalsIgnoreCase("ketua")) {
            MessageUtils.sendError(sender, "Hanya ketua partai yang bisa mempromosikan anggota!");
            return true;
        }

        // Validasi target harus di partai yang sama
        UUID targetPartaiUuid = MemberService.getPartaiUuid(targetUuid);
        if (targetPartaiUuid == null || !targetPartaiUuid.equals(playerPartaiUuid)) {
            MessageUtils.sendError(sender, targetName + " tidak berada di partaimu!");
            return true;
        }

        // Validasi role valid
        if (!roleLevel.containsKey(newRole)) {
            MessageUtils.sendError(sender, "Jabatan tidak valid! Pilih: anggota, sekretaris, bendahara, wakil_ketua");
            return true;
        }

        // Tidak bisa promosi ke ketua
        if (newRole.equalsIgnoreCase("ketua")) {
            MessageUtils.sendError(sender, "Tidak bisa mempromosikan ke jabatan ketua!");
            return true;
        }

        // Validasi: target tidak promosi diri sendiri
        if (targetUuid.equals(playerUuid)) {
            MessageUtils.sendError(sender, "Kamu tidak bisa mempromosikan diri sendiri!");
            return true;
        }

        // Get current role
        MemberData targetMember = MemberService.getMemberData(targetUuid);
        String currentRole = targetMember.getRole().toLowerCase();
        int currentLevel = roleLevel.getOrDefault(currentRole, 1);
        int newLevel = roleLevel.get(newRole);

        // Validasi: harus promosi (level naik)
        if (newLevel <= currentLevel) {
            MessageUtils.sendError(sender, "Promosi harus menaikkan jabatan! Gunakan /partai demosi untuk menurunkan.");
            return true;
        }

        // Update role di database
        MemberService.updateRoleInPartai(targetUuid, playerPartaiUuid, newRole);

        // Notifikasi
        MessageUtils.sendSuccess(sender, targetName + " berhasil dipromosikan dari " + currentRole + " menjadi " + newRole);
        MessageUtils.sendSuccess(targetPlayer, "Selamat! Kamu dipromosikan menjadi " + newRole + " oleh " + player.getName());

        return true;
    }
}
