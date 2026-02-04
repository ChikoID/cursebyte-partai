package com.cursebyte.plugin.command.partai;

import java.util.Comparator;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiManager;
import com.cursebyte.plugin.modules.partai.PartaiService;
import com.cursebyte.plugin.utils.MessageUtils;
import net.kyori.adventure.text.format.NamedTextColor;

public class StatsCommand {
    public boolean execute(CommandSender sender, String[] args) {
        List<PartaiData> partaiList = PartaiManager.getAllPartai();

        MessageUtils.sendHeader(sender, "Statistik Partai Server");
        MessageUtils.sendRaw(sender, "");

        if (partaiList.isEmpty()) {
            MessageUtils.sendRaw(sender, "  Belum ada partai yang terdaftar", NamedTextColor.GRAY);
        } else {
            // Top Uang (Balance)
            MessageUtils.sendRaw(sender, "  " + "Top 5 Partai Terkaya", NamedTextColor.GOLD);
            int[] balanceIndex = { 1 };
            partaiList.stream()
                    .sorted(Comparator.comparingDouble(PartaiData::getBalance).reversed())
                    .limit(5)
                    .forEach(partai -> {
                        MessageUtils.sendRaw(sender, "    " + balanceIndex[0]++ + ". " + partai.getName() + " - " +
                                MessageUtils.formatCurrency(partai.getBalance()), NamedTextColor.YELLOW);
                    });

            MessageUtils.sendRaw(sender, "");

            // Top Reputasi
            MessageUtils.sendRaw(sender, "  " + "Top 5 Partai Bereputasi", NamedTextColor.GOLD);
            int[] index = { 1 };
            partaiList.stream()
                    .sorted(Comparator.comparingDouble(PartaiData::getReputation).reversed())
                    .limit(5)
                    .forEach(partai -> {
                        MessageUtils.sendRaw(sender, "    " + index[0]++ + ". " + partai.getName() + " - " +
                                partai.getReputation() + " rep", NamedTextColor.YELLOW);
                    });
        }

        MessageUtils.sendRaw(sender, "");
        MessageUtils.sendFooter(sender);

        return true;
    }
}
