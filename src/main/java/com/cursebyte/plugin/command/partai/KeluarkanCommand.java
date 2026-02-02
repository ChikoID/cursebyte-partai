package com.cursebyte.plugin.command.partai;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.member.MemberData;
import com.cursebyte.plugin.modules.member.MemberManager;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;
import com.cursebyte.plugin.utils.PlayerUtils;

public class KeluarkanCommand {
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendError(sender, "Command ini hanya bisa dipakai oleh player!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            MessageUtils.sendError(sender, "Usage: /partai keluarkan <playername>");
            return true;
        }

        String targetName = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).trim();
        if (targetName.isEmpty()) {
            MessageUtils.sendError(sender, "Usage: /partai keluarkan <playername>");
            return true;
        }
        UUID playerUuid = player.getUniqueId();
        Player targetPlayer = PlayerUtils.findOnlinePlayerByName(targetName);

        if (targetPlayer == null) {
            MessageUtils.sendError(sender, "Player '" + targetName + "' tidak ditemukan atau sedang offline!");
            return true;
        }

        UUID targetUuid = targetPlayer.getUniqueId();

        // Validasi: player harus di partai
        UUID playerPartaiUuid = MemberService.getPartaiUuid(playerUuid);
        if (playerPartaiUuid == null) {
            MessageUtils.sendError(sender, "Kamu belum bergabung dengan partai!");
            return true;
        }

        // Validasi: player harus ketua
        MemberData playerMember = MemberService.getMemberData(playerUuid);
        if (!playerMember.getRole().equalsIgnoreCase("ketua")) {
            MessageUtils.sendError(sender, "Hanya ketua partai yang bisa mengeluarkan anggota!");
            return true;
        }

        // Validasi: tidak bisa keluarkan diri sendiri
        if (targetUuid.equals(playerUuid)) {
            MessageUtils.sendError(sender, "Kamu tidak bisa mengeluarkan diri sendiri!");
            return true;
        }

        // Validasi: target harus di partai yang sama
        UUID targetPartaiUuid = MemberService.getPartaiUuid(targetUuid);
        if (targetPartaiUuid == null) {
            MessageUtils.sendError(sender, targetName + " tidak tergabung dalam partai manapun!");
            return true;
        }

        if (!targetPartaiUuid.equals(playerPartaiUuid)) {
            MessageUtils.sendError(sender, targetName + " tidak berada di partaimu!");
            return true;
        }

        // Validasi: tidak bisa keluarkan ketua (double check)
        MemberData targetMember = MemberService.getMemberData(targetUuid);
        if (targetMember.getRole().equalsIgnoreCase("ketua")) {
            MessageUtils.sendError(sender, "Tidak bisa mengeluarkan ketua partai!");
            return true;
        }

        // Hapus member dari database
        MemberManager.removeMember(targetUuid);

        // Get party info untuk notifikasi
        PartaiData partai = PartaiService.getPartai(playerPartaiUuid);

        // Notifikasi ke yang mengeluarkan
        MessageUtils.sendSuccess(sender, targetName + " telah dikeluarkan dari partai " + partai.getName());

        // Notifikasi ke yang dikeluarkan
        MessageUtils.sendWarning(targetPlayer,
                "Kamu telah dikeluarkan dari partai " + partai.getName() + " oleh " + player.getName());

        return true;
    }
}
