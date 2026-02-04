package com.cursebyte.plugin.command.partai;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.modules.economy.EconomyService;
import com.cursebyte.plugin.modules.member.MemberManager;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiManager;
import com.cursebyte.plugin.modules.reputation.ReputationService;
import com.cursebyte.plugin.utils.MessageUtils;

public class BuatCommand {
    private final PartaiCore plugin;

    public BuatCommand(PartaiCore plugin) {
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length < 2) {
            MessageUtils.sendError(sender, "Usage: /partai buat <nama_partai> [singkatan_partai]");
            return true;
        }

        String namaPartai = args[1];
        String singkatanPartai = args.length >= 3 ? args[2]
                : args[1].substring(0, Math.min(3, args[1].length())).toUpperCase();
        UUID playerUUID = player.getUniqueId();
        UUID randomUUID = UUID.randomUUID();

        // Validasi: player sudah bergabung dengan partai?
        UUID existingPartaiUuid = MemberService.getPartaiUuid(playerUUID);
        if (existingPartaiUuid != null) {
            MessageUtils.sendError(sender, "Kamu sudah bergabung dengan partai!");
            return true;
        }

        if (PartaiManager.existsByName(namaPartai)) {
            MessageUtils.sendError(sender, "Nama partai sudah digunakan!");
            return true;
        }

        if (PartaiManager.existsByShortName(singkatanPartai)) {
            MessageUtils.sendError(sender, "Singkatan partai sudah digunakan!");
            return true;
        }

        double minReputation = plugin.getConfig().getDouble("min-reputation-to-create");
        if (ReputationService.get(playerUUID) < minReputation) {
            MessageUtils.sendError(sender, "Reputasi kamu tidak mencukupi untuk membuat partai!");
            return true;
        }

        double createCost = plugin.getConfig().getDouble("create-cost");
        if (EconomyService.getBalance(playerUUID) < createCost) {
            MessageUtils.sendError(sender, "Saldo kamu tidak mencukupi untuk membuat partai!");
            return true;
        }

        EconomyService.remove(playerUUID, createCost);
        PartaiManager.create(randomUUID, namaPartai, singkatanPartai, playerUUID);
        MemberManager.addMember(playerUUID, randomUUID, "ketua");

        MessageUtils.sendSuccess(sender, "Partai " + namaPartai + " berhasil dibuat!");
        return true;
    }
}
