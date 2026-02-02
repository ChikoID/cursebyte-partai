package com.cursebyte.plugin.command.partai;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiManager;
import com.cursebyte.plugin.utils.MessageUtils;

public class EditCommand {
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            MessageUtils.sendError(sender, "Usage: /partai edit <nama_baru> [singkatan_baru]");
        }
        
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        if (!PartaiManager.existsByUuid(playerUUID)) {
            MessageUtils.sendError(sender, "Anda tidak memiliki partai!");
            return;
        }

        UUID partaiUuid = MemberService.getPartaiUuid(playerUUID);

        String newName = args[1];
        String singkatanPartai = args.length >= 3 ? args[2]
                : args[1].substring(0, Math.min(3, args[1].length())).toUpperCase();

        if (PartaiManager.existsByName(newName)) {
            MessageUtils.sendError(sender, "Nama partai sudah digunakan!");
            return;
        }

        if (PartaiManager.existsByShortName(singkatanPartai)) {
            MessageUtils.sendError(sender, "Singkatan partai sudah digunakan!");
            return;
        }

        PartaiManager.updateSimple(partaiUuid, newName, singkatanPartai, playerUUID);
        MessageUtils.sendSuccess(sender, "Partai berhasil diubah!");
    }
}
