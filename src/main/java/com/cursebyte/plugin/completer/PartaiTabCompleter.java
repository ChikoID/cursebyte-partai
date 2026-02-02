package com.cursebyte.plugin.completer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.cursebyte.plugin.PartaiCore;
import com.cursebyte.plugin.modules.citizen.CitizenService;

public class PartaiTabCompleter implements TabCompleter {
    private final PartaiCore plugin;

    public PartaiTabCompleter(PartaiCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        switch (args.length) {
            case 1 -> {
                result.add("help"); // done

                result.add("buat"); // done
                result.add("hapus"); // done
                result.add("edit");
                result.add("info");

                result.add("undang");
                result.add("terima");
                result.add("tolak");
                result.add("keluarkan");
                result.add("promosi");
                result.add("demosi");

                result.add("keluar");
                result.add("daftar");

                result.add("setor");
                result.add("tarik");
                result.add("saldo");

                result.add("pengumuman");
                result.add("chat");

                result.add("sekutu");
                result.add("musuh");
                result.add("status");
                result.add("reputasi");
            }
            case 2 -> {
                String subcommand = args[0].toLowerCase();
                switch (subcommand) {
                    case "buat" -> {
                        result.add("<nama_partai>");
                    }
                    case "edit" -> {
                        result.add("<nama_baru>");
                    }
                    case "info", "daftar", "saldo", "status", "reputasi", "sekutu", "musuh" -> {
                    }
                    case "setor", "tarik" -> {
                        result.add("<jumlah>");
                    }
                    case "undang", "keluarkan", "promosi", "demosi" -> {
                        plugin.getServer().getOnlinePlayers().forEach(player -> {
                            var profile = CitizenService.getProfile(player.getUniqueId());
                            if (profile != null) {
                                result.add(profile.getFullName());
                            }
                        });
                    }
                    case "pengumuman" -> {
                        result.add("<pesan>");
                    }
                }
            }
            case 3 -> {
                String subcommand = args[0].toLowerCase();
                switch (subcommand) {
                    case "buat" -> {
                        result.add("<tag_partai>");
                    }
                    case "setor", "tarik" -> {
                        result.add("[optional: alasan]");
                    }
                    case "edit" -> {
                        result.add("<tag_baru>");
                    }
                    case "promosi", "demosi" -> {
                        result.add("<jabatan_baru>");
                    }
                }
            }
        }
        return result;
    }
}
