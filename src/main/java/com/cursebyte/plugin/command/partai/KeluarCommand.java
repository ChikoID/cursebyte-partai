package com.cursebyte.plugin.command.partai;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.member.MemberData;
import com.cursebyte.plugin.modules.member.MemberManager;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class KeluarCommand {
    public boolean execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();

        // Validasi: player harus di partai
        UUID partaiUuid = MemberService.getPartaiUuid(playerUuid);
        if (partaiUuid == null) {
            MessageUtils.sendError(sender, "Kamu belum bergabung dengan partai!");
            return true;
        }

        // Validasi: ketua tidak bisa keluar, harus bubarkan atau transfer ketua dulu
        MemberData memberData = MemberService.getMemberData(playerUuid);
        if (memberData.getRole().equalsIgnoreCase("ketua")) {
            MessageUtils.sendError(sender, "Ketua tidak bisa keluar dari partai!");
            MessageUtils.sendInfo(sender, "Gunakan /partai hapus untuk membubarkan partai atau transfer jabatan ketua terlebih dahulu.");
            return true;
        }

        // Get party info untuk notifikasi
        PartaiData partai = PartaiService.getPartai(partaiUuid);

        // Hapus member dari database
        MemberManager.removeMember(playerUuid);

        // Notifikasi
        MessageUtils.sendSuccess(sender, "Kamu telah keluar dari partai " + partai.getName());

        // Optional: broadcast ke party members yang online
        // (bisa ditambahkan nanti jika perlu)

        return true;
    }
}
