package com.cursebyte.plugin.command.admin;

import org.bukkit.command.CommandSender;

import com.cursebyte.plugin.modules.citizen.CitizenService;
import com.cursebyte.plugin.modules.partai.PartaiManager;
import com.cursebyte.plugin.utils.MessageUtils;

public class ListCommand {
    public void execute(CommandSender sender, String[] args) {
        var all = PartaiManager.getAll();
        if (all.isEmpty()) {
            MessageUtils.sendError(sender, "Tidak ada partai yang terdaftar.");
            return;
        }

        StringBuilder partaiList = new StringBuilder("Daftar Partai:\n");
        all.forEach(p -> partaiList.append("- ").append(p.getName())
                .append(" [")
                .append(p.getShortName())
                .append("] Ketua: ")
                .append(CitizenService.getProfile(p.getLeaderUuid()).getFullName())
                .append("\n"));

        MessageUtils.sendRaw(sender, partaiList.toString());
    }
}
