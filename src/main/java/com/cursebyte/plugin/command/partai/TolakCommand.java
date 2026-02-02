package com.cursebyte.plugin.command.partai;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class TolakCommand {
    private final PartaiCore plugin;
    private final UndangCommand undangCommand;

    public TolakCommand(PartaiCore plugin, UndangCommand undangCommand) {
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

        // Ambil data partai untuk info
        PartaiData partai = PartaiService.getPartai(partaiUuid);
        String partaiName = (partai != null) ? partai.getName() : "Unknown";

        // Hapus undangan
        undangCommand.removeInvitation(player, partaiUuid);
        
        MessageUtils.sendInfo(sender, "Kamu menolak undangan dari partai " + partaiName);
    }
}
