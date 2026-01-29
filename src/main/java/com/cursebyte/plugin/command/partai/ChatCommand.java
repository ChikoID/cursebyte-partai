package com.cursebyte.plugin.command.partai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class ChatCommand {
    private static final Map<UUID, Boolean> chatMode = new HashMap<>();

    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendError(sender, "Command ini hanya bisa dipakai oleh player!");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();

        // Validasi: player harus di partai
        UUID partaiUuid = MemberService.getPartaiUuid(playerUuid);
        if (partaiUuid == null) {
            MessageUtils.sendError(sender, "Kamu belum bergabung dengan partai!");
            return true;
        }

        // Get party data
        PartaiData partai = PartaiService.getPartai(partaiUuid);
        if (partai == null) {
            MessageUtils.sendError(sender, "Data partai tidak ditemukan!");
            return true;
        }

        // Jika ada pesan, langsung kirim
        if (args.length >= 2) {
            String message = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
            sendPartaiChat(player, partai, message);
            return true;
        }

        // Toggle chat mode
        boolean currentMode = chatMode.getOrDefault(playerUuid, false);
        chatMode.put(playerUuid, !currentMode);

        if (!currentMode) {
            MessageUtils.sendSuccess(sender, "Mode chat partai aktif! Pesan kamu hanya akan terlihat oleh anggota partai.");
            MessageUtils.sendInfo(sender, "Ketik /partai chat lagi untuk keluar dari mode chat partai.");
        } else {
            MessageUtils.sendSuccess(sender, "Mode chat partai dinonaktifkan. Kembali ke chat global.");
        }

        return true;
    }

    /**
     * Cek apakah player dalam mode chat partai
     */
    public static boolean isInChatMode(UUID playerUuid) {
        return chatMode.getOrDefault(playerUuid, false);
    }

    /**
     * Kirim pesan ke chat partai
     */
    public static void sendPartaiChat(Player sender, PartaiData partai, String message) {
        UUID partaiUuid = partai.getUuid();
        MemberData senderMember = MemberService.getMemberData(sender.getUniqueId());
        
        // Get all party members
        List<MemberData> members = MemberManager.getMembersByPartaiUuid(partaiUuid);

        // Format: [PARTAI] [Role] NamaPlayer: pesan
        Component chatMessage = Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text(partai.getShortName(), NamedTextColor.GOLD))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(Component.text("[", NamedTextColor.DARK_GRAY))
                .append(Component.text(senderMember.getRole(), getRoleColor(senderMember.getRole())))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(Component.text(sender.getName(), NamedTextColor.WHITE))
                .append(Component.text(": ", NamedTextColor.GRAY))
                .append(Component.text(message, NamedTextColor.WHITE));

        // Kirim ke semua member yang online
        for (MemberData member : members) {
            Player target = Bukkit.getPlayer(member.getPlayerUuid());
            if (target != null && target.isOnline()) {
                target.sendMessage(chatMessage);
            }
        }
    }

    /**
     * Clear chat mode saat player logout
     */
    public static void clearChatMode(UUID playerUuid) {
        chatMode.remove(playerUuid);
    }

    private static NamedTextColor getRoleColor(String role) {
        return switch (role.toLowerCase()) {
            case "ketua" -> NamedTextColor.RED;
            case "wakil_ketua" -> NamedTextColor.GOLD;
            case "bendahara" -> NamedTextColor.GREEN;
            case "sekretaris" -> NamedTextColor.AQUA;
            default -> NamedTextColor.GRAY;
        };
    }
}
