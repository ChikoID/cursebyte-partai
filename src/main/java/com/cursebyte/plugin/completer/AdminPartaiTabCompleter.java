package com.cursebyte.plugin.completer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.modules.citizen.CitizenService;
import com.cursebyte.plugin.modules.partai.PartaiManager;

public class AdminPartaiTabCompleter implements TabCompleter {
    private final PartaiCore plugin;

    public AdminPartaiTabCompleter(PartaiCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();

        switch (args.length) {
            case 1 -> {
                result.add("reload");
                result.add("reset");
                result.add("bubarkan");
                result.add("balance");
                result.add("list");
                result.add("set");
            }
            case 2 -> {
                String subcommand = args[0].toLowerCase();
                switch (subcommand) {
                    case "reset", "bubarkan", "balance", "set" -> {
                        PartaiManager.getAll().forEach(partai -> result.add(partai.getName()));
                    }
                }
            }
            case 3 -> {
                String subcommand = args[0].toLowerCase();
                switch (subcommand) {
                    case "balance" -> {
                        result.add("<jumlah>");
                    }
                    case "set" -> {
                        plugin.getServer().getOnlinePlayers().forEach(player -> {
                            var profile = CitizenService.getProfile(player.getUniqueId());
                            if (profile != null) {
                                result.add(profile.getFullName());
                            }
                        });
                    }
                }
            }
            case 4 -> {
                String subcommand = args[0].toLowerCase();
                switch (subcommand) {
                    case "set" -> {
                        result.add("ketua");
                        result.add("wakil_ketua");
                        result.add("bendahara");
                        result.add("sekretaris");
                        result.add("anggota");
                    }
                }
            }
        }

        return result;
    }
}
