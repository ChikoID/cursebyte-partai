package com.cursebyte.plugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.citizen.CitizenService;

public class PlayerUtils {
    private PlayerUtils() {
        // Utility class
    }

    public static Player findOnlinePlayerByName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }

        Player exact = Bukkit.getPlayerExact(name);
        if (exact != null) {
            return exact;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            var profile = CitizenService.getProfile(player.getUniqueId());
            if (profile != null && profile.getFullName() != null
                    && profile.getFullName().equalsIgnoreCase(name)) {
                return player;
            }
        }

        return Bukkit.getPlayer(name);
    }
}
