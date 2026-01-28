package com.cursebyte.plugin.command.admin;

import java.util.UUID;

import org.bukkit.command.CommandSender;

import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;

public class BubarkanCommand {
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            MessageUtils.sendError(sender, "Gunakan: /adminpartai bubarkan <nama_partai> <alasan>");
            return;
        }

        String partaiName = args[1];
        UUID partaiUUID = com.cursebyte.plugin.modules.partai.PartaiManager.getUuidByName(partaiName);
        if (partaiUUID == null) {
            MessageUtils.sendError(sender, "Partai dengan nama '" + partaiName + "' tidak ditemukan.");
            return;
        }

        StringBuilder alasanBuilder = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            alasanBuilder.append(args[i]);
            if (i < args.length - 1) {
                alasanBuilder.append(" ");
            }
        }

        String alasan = alasanBuilder.toString();
        PartaiService.deletePartai(partaiUUID);
        MessageUtils.sendSuccess(sender, "Partai " + partaiName + " telah dibubarkan.");
        MessageUtils.sendInfo(sender, "Alasan: " + alasan);
    }
}
