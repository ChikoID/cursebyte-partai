package com.cursebyte.plugin.command.partai;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.member.MemberData;
import com.cursebyte.plugin.modules.member.MemberManager;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class PengumumanCommand {
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendError(sender, "Command ini hanya bisa dipakai oleh player!");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();

        if (args.length < 2) {
            MessageUtils.sendError(sender, "Usage: /partai pengumuman <pesan>");
            return true;
        }

        // Validasi: player harus di partai
        UUID partaiUuid = MemberService.getPartaiUuid(playerUuid);
        if (partaiUuid == null) {
            MessageUtils.sendError(sender, "Kamu belum bergabung dengan partai!");
            return true;
        }

        // Validasi: hanya ketua atau wakil ketua yang bisa broadcast
        MemberData memberData = MemberService.getMemberData(playerUuid);
        String role = memberData.getRole().toLowerCase();
        if (!role.equals("ketua") && !role.equals("wakil_ketua")) {
            MessageUtils.sendError(sender, "Hanya ketua atau wakil ketua yang bisa membuat pengumuman!");
            return true;
        }

        // Get party data
        PartaiData partai = PartaiService.getPartai(partaiUuid);
        if (partai == null) {
            MessageUtils.sendError(sender, "Data partai tidak ditemukan!");
            return true;
        }

        // Gabungkan pesan
        String message = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));

        // Get all party members
        List<MemberData> members = MemberManager.getMembersByPartaiUuid(partaiUuid);

        // Broadcast ke semua member yang online
        int sentCount = 0;
        for (MemberData member : members) {
            Player target = Bukkit.getPlayer(member.getPlayerUuid());
            if (target != null && target.isOnline()) {
                // Format pengumuman
                target.sendMessage(Component.empty());
                target.sendMessage(Component.text("═══════════════════════════════════", NamedTextColor.GOLD));
                target.sendMessage(Component.text("       PENGUMUMAN PARTAI", NamedTextColor.YELLOW, TextDecoration.BOLD));
                target.sendMessage(Component.text("═══════════════════════════════════", NamedTextColor.GOLD));
                target.sendMessage(Component.empty());
                target.sendMessage(Component.text("Dari: ", NamedTextColor.GRAY)
                        .append(Component.text(player.getName(), NamedTextColor.WHITE))
                        .append(Component.text(" (" + memberData.getRole() + ")", NamedTextColor.GRAY)));
                target.sendMessage(Component.text("Partai: ", NamedTextColor.GRAY)
                        .append(Component.text(partai.getName(), NamedTextColor.GOLD)));
                target.sendMessage(Component.empty());
                target.sendMessage(Component.text(message, NamedTextColor.WHITE));
                target.sendMessage(Component.empty());
                target.sendMessage(Component.text("═══════════════════════════════════", NamedTextColor.GOLD));
                target.sendMessage(Component.empty());
                sentCount++;
            }
        }

        // Notifikasi ke sender
        MessageUtils.sendSuccess(sender, "Pengumuman terkirim ke " + sentCount + " anggota online");

        return true;
    }
}
