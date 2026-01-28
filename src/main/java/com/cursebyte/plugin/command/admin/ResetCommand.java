package com.cursebyte.plugin.command.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.command.CommandSender;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.modules.partai.PartaiManager;
import com.cursebyte.plugin.utils.MessageUtils;

public class ResetCommand {
    private final PartaiCore plugin;
    private final Map<String, String> resetCodes = new HashMap<>();
    private final Random random = new Random();

    public ResetCommand(PartaiCore plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            MessageUtils.sendError(sender, "Gunakan /adminpartai <subcommand>");
            return;
        }

        if (args.length == 1) {
            String code = String.format("%04d", random.nextInt(10000));
            resetCodes.put(sender.getName(), code);
            MessageUtils.sendError(sender,
                    "Anda yakin ingin mereset semua data partai? Ketik ulang perintah dengan kode konfirmasi: " + code);
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                resetCodes.remove(sender.getName());
            }, 600L);
        } else if (args.length == 2) {
            String code = args[1];
            String expectedCode = resetCodes.get(sender.getName());
            if (expectedCode == null) {
                MessageUtils.sendError(sender, "Kode reset tidak valid atau sudah expired.");
            } else if (code.equals(expectedCode)) {
                PartaiManager.deleteAll();
                MessageUtils.sendSuccess(sender, "Semua data partai telah direset.");
            } else {
                MessageUtils.sendError(sender, "Kode reset tidak valid.");
            }
        }
    }
}
