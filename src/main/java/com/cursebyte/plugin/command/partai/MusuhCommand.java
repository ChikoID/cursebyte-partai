package com.cursebyte.plugin.command.partai;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.member.MemberData;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.modules.relasi.RelasiPartaiManager;
import com.cursebyte.plugin.utils.MessageUtils;

public class MusuhCommand {

    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendError(sender, "Command ini hanya bisa dipakai oleh player!");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();

        if (args.length < 2) {
            MessageUtils.sendError(sender, "Usage: /partai musuh <nama_partai>");
            MessageUtils.sendInfo(sender, "Untuk membatalkan: /partai musuh <nama_partai> batal");
            return true;
        }

        // Validasi: player harus di partai
        UUID partaiUuid = MemberService.getPartaiUuid(playerUuid);
        if (partaiUuid == null) {
            MessageUtils.sendError(sender, "Kamu belum bergabung dengan partai!");
            return true;
        }

        // Validasi: hanya ketua yang bisa set relasi
        MemberData memberData = MemberService.getMemberData(playerUuid);
        if (!memberData.getRole().equalsIgnoreCase("ketua")) {
            MessageUtils.sendError(sender, "Hanya ketua partai yang bisa mengatur hubungan musuh!");
            return true;
        }

        // Get current party
        PartaiData myPartai = PartaiService.getPartai(partaiUuid);
        if (myPartai == null) {
            MessageUtils.sendError(sender, "Data partai tidak ditemukan!");
            return true;
        }

        // Parse target party name (bisa multi-word)
        String targetName;
        boolean isCancel = false;
        if (args.length >= 3 && args[args.length - 1].equalsIgnoreCase("batal")) {
            targetName = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length - 1));
            isCancel = true;
        } else {
            targetName = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        }

        // Get target party
        PartaiData targetPartai = PartaiService.getPartaiByName(targetName);
        if (targetPartai == null) {
            MessageUtils.sendError(sender, "Partai '" + targetName + "' tidak ditemukan!");
            return true;
        }

        // Validasi: tidak bisa musuhan dengan diri sendiri
        if (targetPartai.getUuid().equals(partaiUuid)) {
            MessageUtils.sendError(sender, "Tidak bisa bermusuhan dengan partai sendiri!");
            return true;
        }

        if (isCancel) {
            // Batalkan hubungan musuh (set ke netral)
            RelasiPartaiManager.update(partaiUuid, targetPartai.getUuid(), "NETRAL");
            RelasiPartaiManager.update(targetPartai.getUuid(), partaiUuid, "NETRAL");
            MessageUtils.sendSuccess(sender, "Hubungan musuh dengan " + targetPartai.getName() + " telah dibatalkan!");
        } else {
            // Cek relasi saat ini
            String currentRelation = RelasiPartaiManager.getRelationType(partaiUuid, targetPartai.getUuid());
            if ("SEKUTU".equalsIgnoreCase(currentRelation)) {
                MessageUtils.sendError(sender, "Tidak bisa bermusuhan dengan partai yang sedang bersekutu!");
                MessageUtils.sendInfo(sender, "Batalkan status sekutu terlebih dahulu dengan: /partai sekutu " + targetPartai.getName() + " batal");
                return true;
            }

            // Set hubungan musuh (bilateral)
            RelasiPartaiManager.update(partaiUuid, targetPartai.getUuid(), "MUSUH");
            RelasiPartaiManager.update(targetPartai.getUuid(), partaiUuid, "MUSUH");
            
            MessageUtils.sendSuccess(sender, "Berhasil mengatur hubungan musuh dengan " + targetPartai.getName() + "!");
            MessageUtils.sendWarning(sender, "Hubungan ini bersifat bilateral (dua arah).");
        }

        return true;
    }
}
