package com.cursebyte.plugin.command.partai;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.modules.member.MemberData;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class UndangCommand {
    private final Map<UUID, Map<UUID, Long>> invitations = new HashMap<>();

    private final int inviteExpiration;

    public UndangCommand(PartaiCore plugin) {
        this.inviteExpiration = plugin.getConfig().getInt("invite-expiration", 10);
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length < 2) {
            MessageUtils.sendError(sender, "Usage: /partai undang <playername>");
            return;
        }

        String targetName = args[1];
        UUID inviterUuid = player.getUniqueId();
        Player targetPlayer = Bukkit.getPlayer(targetName);

        if (targetPlayer == null) {
            MessageUtils.sendError(sender, "Player '" + targetName + "' tidak ditemukan atau sedang offline!");
            return;
        }

        UUID targetUuid = targetPlayer.getUniqueId();
        UUID inviterPartaiUuid = MemberService.getPartaiUuid(inviterUuid);
        if (inviterPartaiUuid == null) {
            MessageUtils.sendError(sender, "Kamu belum bergabung dengan partai!");
            return;
        }

        MemberData inviterMember = MemberService.getMemberData(inviterUuid);
        if (!inviterMember.getRole().equalsIgnoreCase("ketua")) {
            MessageUtils.sendError(sender, "Hanya ketua partai yang bisa mengundang!");
            return;
        }

        if (MemberService.getMemberData(targetUuid) != null) {
            MessageUtils.sendError(sender, targetName + " sudah bergabung dengan partai!");
        }

        Map<UUID, Long> targetInvites = invitations.getOrDefault(targetUuid, new HashMap<>());

        if (targetInvites.containsKey(inviterPartaiUuid)) {
            long elapsed = System.currentTimeMillis() - targetInvites.get(inviterPartaiUuid);
            if (elapsed < inviteExpiration * 60 * 1000) {
                MessageUtils.sendError(sender, targetName + " sudah punya undangan dari partaimu!");
            } else {
                targetInvites.remove(inviterPartaiUuid);
            }
        }

        targetInvites.put(inviterPartaiUuid, System.currentTimeMillis());
        invitations.put(targetUuid, targetInvites);

        PartaiData partai = PartaiService.getPartai(inviterPartaiUuid);
        MessageUtils.sendSuccess(sender, "Undangan terkirim ke " + targetName);

        MessageUtils.sendRaw(targetPlayer, "");
        MessageUtils.sendHeader(targetPlayer, "Undangan Partai");
        MessageUtils.sendRaw(targetPlayer, "  Partai: " + partai.getName() + " (" + partai.getShortName() + ")");
        MessageUtils.sendRaw(targetPlayer, "  Dari: " + sender.getName());
        MessageUtils.sendRaw(targetPlayer, "  Ketik /partai terima untuk menerima undangan");
        MessageUtils.sendRaw(targetPlayer, "  Ketik /partai tolak untuk menolak");
        MessageUtils.sendRaw(targetPlayer, "");
        MessageUtils.sendFooter(targetPlayer);
    }

    public UUID getInvitedPartaiUuid(Player player) {
        Map<UUID, Long> playerInvites = invitations.getOrDefault(player.getUniqueId(), new HashMap<>());
        for (Map.Entry<UUID, Long> entry : playerInvites.entrySet()) {
            long elapsed = System.currentTimeMillis() - entry.getValue();
            if (elapsed < inviteExpiration * 60 * 1000) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void removeInvitation(Player player, UUID partaiUuid) {
        Map<UUID, Long> playerInvites = invitations.get(player.getUniqueId());
        if (playerInvites != null) {
            playerInvites.remove(partaiUuid);
        }
    }
}
