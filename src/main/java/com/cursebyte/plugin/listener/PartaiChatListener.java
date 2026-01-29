package com.cursebyte.plugin.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.cursebyte.plugin.command.partai.ChatCommand;
import com.cursebyte.plugin.modules.member.MemberService;
import com.cursebyte.plugin.modules.partai.PartaiData;
import com.cursebyte.plugin.modules.partai.PartaiService;

/**
 * Listener untuk intercept chat dan handle party chat mode
 */
public class PartaiChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();

        // Cek apakah player dalam mode chat partai
        if (!ChatCommand.isInChatMode(playerUuid)) {
            return; // Biarkan chat global berjalan normal
        }

        // Cancel chat global
        event.setCancelled(true);

        // Get party data
        UUID partaiUuid = MemberService.getPartaiUuid(playerUuid);
        if (partaiUuid == null) {
            // Player tidak di partai, matikan mode chat
            ChatCommand.clearChatMode(playerUuid);
            return;
        }

        PartaiData partai = PartaiService.getPartai(partaiUuid);
        if (partai == null) {
            return;
        }

        // Kirim ke chat partai (harus di main thread)
        String message = event.getMessage();
        Bukkit.getScheduler().runTask(
                Bukkit.getPluginManager().getPlugin("cursebyte-partai"),
                () -> ChatCommand.sendPartaiChat(player, partai, message));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Clear chat mode saat player logout
        ChatCommand.clearChatMode(event.getPlayer().getUniqueId());
    }
}
