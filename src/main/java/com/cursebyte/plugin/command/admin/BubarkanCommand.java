package com.cursebyte.plugin.command.admin;

import java.util.UUID;

import org.bukkit.command.CommandSender;

import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class BubarkanCommand {
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            MessageUtils.sendError(sender, "Gunakan: /adminpartai bubarkan <nama_partai> [alasan]");
            return;
        }

        String partaiName = args[1];
        
        // Cari partai
        PartaiData partai = PartaiService.getPartaiByName(partaiName);
        if (partai == null) {
            MessageUtils.sendError(sender, "Partai dengan nama '" + partaiName + "' tidak ditemukan.");
            return;
        }

        UUID partaiUUID = partai.getUuid();
        
        // Ambil alasan jika ada
        String alasan = "Admin decision";
        if (args.length > 2) {
            StringBuilder alasanBuilder = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                alasanBuilder.append(args[i]);
                if (i < args.length - 1) {
                    alasanBuilder.append(" ");
                }
            }
            alasan = alasanBuilder.toString();
        }
        
        PartaiService.deletePartai(partaiUUID);
        MessageUtils.sendSuccess(sender, "Partai " + partaiName + " telah dibubarkan.");
        MessageUtils.sendInfo(sender, "Alasan: " + alasan);
    }
}
