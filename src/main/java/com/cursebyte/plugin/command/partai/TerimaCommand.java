package com.cursebyte.plugin.command.partai;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.modules.member.MemberManager;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class TerimaCommand {
    private final PartaiCore plugin;
    private final UndangCommand undangCommand;

    public TerimaCommand(PartaiCore plugin, UndangCommand undangCommand) {
        this.plugin = plugin;
        this.undangCommand = undangCommand;
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();

        // Cek apakah sudah bergabung dengan partai
        if (MemberService.getMemberData(playerUuid) != null) {
            MessageUtils.sendError(sender, "Kamu sudah bergabung dengan partai!");
            return;
        }

        // Ambil undangan partai
        UUID partaiUuid = undangCommand.getInvitedPartaiUuid(player);
        if (partaiUuid == null) {
            MessageUtils.sendError(sender, "Kamu tidak memiliki undangan partai yang valid!");
            return;
        }

        // Ambil data partai
        PartaiData partai = PartaiService.getPartai(partaiUuid);
        if (partai == null) {
            MessageUtils.sendError(sender, "Partai tidak ditemukan!");
            undangCommand.removeInvitation(player, partaiUuid);
            return;
        }

        // Tambahkan sebagai anggota dengan jabatan "anggota"
        MemberManager.addMember(playerUuid, partaiUuid, "anggota");
        
        // Hapus undangan
        undangCommand.removeInvitation(player, partaiUuid);
        
        MessageUtils.sendSuccess(sender, "Kamu berhasil bergabung dengan partai " + partai.getName() + "!");
        MessageUtils.sendInfo(sender, "Jabatan: Anggota");
    }
}
