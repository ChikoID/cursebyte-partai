package com.cursebyte.plugin.completer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.cursebyte.plugin.PartaiCore;

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
                result.add("edit"); // done
                result.add("info"); // done

                result.add("undang"); // done
                result.add("keluarkan"); // done
                result.add("promosi"); // done
                result.add("demosi"); // done

                result.add("keluar"); // done
                result.add("daftar"); // done

                result.add("setor"); // done
                result.add("tarik"); // done
                result.add("saldo"); // done

                result.add("pengumuman"); // done
                result.add("chat"); // done

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
                        plugin.getServer().getOnlinePlayers().forEach(player -> result.add(player.getName()));
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
