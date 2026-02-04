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
        if (args.length == 1) {
            // Minta konfirmasi
            String code = String.format("%04d", random.nextInt(10000));
            resetCodes.put(sender.getName(), code);
            MessageUtils.sendError(sender,
                    "⚠ PERINGATAN: Anda akan MENGHAPUS SEMUA DATA PARTAI! Ini tidak bisa dibatalkan!");
            MessageUtils.sendError(sender,
                    "Ketik ulang perintah dengan kode konfirmasi untuk melanjutkan: " + code);
            
            // Kode expires dalam 10 menit (600 detik)
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                resetCodes.remove(sender.getName());
            }, 600L);
        } else if (args.length == 2) {
            String code = args[1];
            String expectedCode = resetCodes.get(sender.getName());
            
            if (expectedCode == null) {
                MessageUtils.sendError(sender, "Kode reset tidak valid atau sudah expired. Coba lagi dengan /adminpartai reset");
            } else if (code.equals(expectedCode)) {
                PartaiManager.deleteAll();
                MessageUtils.sendSuccess(sender, "✓ Semua data partai telah direset.");
                resetCodes.remove(sender.getName());
            } else {
                MessageUtils.sendError(sender, "Kode reset tidak valid. Coba lagi atau ketik /adminpartai reset untuk kode baru.");
            }
        } else {
            MessageUtils.sendError(sender, "Gunakan: /adminpartai reset");
        }
    }
}
