package com.cursebyte.plugin.command.partai;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class HelpCommand {

        public boolean execute(CommandSender sender, String[] args) {
                if (!(sender instanceof Player)) {
                        sender.sendMessage(Component.text("Command hanya bisa digunakan oleh player!", NamedTextColor.RED));
                        return true;
                }

                Player player = (Player) sender;
                sender.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                                .color(NamedTextColor.GOLD)
                                .decorate(TextDecoration.BOLD));
                sender.sendMessage(Component.text("              BANTUAN PERINTAH PARTAI")
                                .color(NamedTextColor.YELLOW)
                                .decorate(TextDecoration.BOLD));
                sender.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                                .color(NamedTextColor.GOLD)
                                .decorate(TextDecoration.BOLD));

                // General Commands
                sender.sendMessage(Component.empty());
                sender.sendMessage(Component.text("▸ Umum:").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
                sendHelpLine(sender, "/partai help", "Menampilkan bantuan ini");
                // sendHelpLine(sender, "/partai check", "Mengecek status keanggotaan Anda");
                sendHelpLine(sender, "/partai info", "Melihat informasi partai Anda");
                sendHelpLine(sender, "/partai status", "Melihat status dan relasi partai");
                sendHelpLine(sender, "/partai reputasi", "Melihat reputasi partai Anda");

                // Management Commands
                sender.sendMessage(Component.empty());
                sender.sendMessage(Component.text("▸ Manajemen:").color(NamedTextColor.AQUA)
                                .decorate(TextDecoration.BOLD));
                sendHelpLine(sender, "/partai buat <nama>", "Membuat partai baru");
                sendHelpLine(sender, "/partai edit <field> <value>", "Mengedit informasi partai (ketua)");
                sendHelpLine(sender, "/partai hapus", "Menghapus partai Anda (ketua)");

                // Member Commands
                sender.sendMessage(Component.empty());
                sender.sendMessage(
                                Component.text("▸ Anggota:").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
                sendHelpLine(sender, "/partai undang <player>", "Mengundang pemain ke partai (ketua)");
                sendHelpLine(sender, "/partai keluarkan <player>", "Mengeluarkan anggota (ketua)");
                sendHelpLine(sender, "/partai promosi <player> <role>", "Mempromosikan anggota (ketua)");
                sendHelpLine(sender, "/partai demosi <player> <role>", "Menurunkan jabatan anggota (ketua)");
                sendHelpLine(sender, "/partai daftar", "Melihat daftar anggota partai");
                sendHelpLine(sender, "/partai keluar", "Keluar dari partai");

                // Economy Commands
                sender.sendMessage(Component.empty());
                sender.sendMessage(
                                Component.text("▸ Ekonomi:").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
                sendHelpLine(sender, "/partai setor <jumlah>", "Menyetor uang ke kas partai");
                sendHelpLine(sender, "/partai tarik <jumlah>", "Menarik uang dari kas (ketua/bendahara)");
                sendHelpLine(sender, "/partai saldo", "Mengecek saldo kas partai");

                // Communication Commands
                sender.sendMessage(Component.empty());
                sender.sendMessage(Component.text("▸ Komunikasi:").color(NamedTextColor.AQUA)
                                .decorate(TextDecoration.BOLD));
                sendHelpLine(sender, "/partai chat", "Toggle mode chat partai");
                sendHelpLine(sender, "/partai pengumuman <pesan>", "Broadcast ke semua anggota (ketua/wakil)");

                // Relations Commands
                sender.sendMessage(Component.empty());
                sender.sendMessage(
                                Component.text("▸ Relasi:").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
                sendHelpLine(sender, "/partai sekutu <partai>", "Mengatur sekutu dengan partai lain (ketua)");
                sendHelpLine(sender, "/partai musuh <partai>", "Mengatur musuh dengan partai lain (ketua)");

                // Footer
                sender.sendMessage(Component.empty());
                sender.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                                .color(NamedTextColor.GOLD)
                                .decorate(TextDecoration.BOLD));

                if (player.isOp()) {
                        sender.sendMessage(Component.text("  Tip: Gunakan /adminpartai untuk perintah admin")
                                        .color(NamedTextColor.GRAY)
                                        .decorate(TextDecoration.ITALIC));
                }

                return true;
        }

        private void sendHelpLine(CommandSender sender, String command, String description) {
                sender.sendMessage(
                                Component.text("  • ", NamedTextColor.GRAY)
                                                .append(Component.text(command, NamedTextColor.GREEN))
                                                .append(Component.text(" - ", NamedTextColor.DARK_GRAY))
                                                .append(Component.text(description, NamedTextColor.WHITE)));
        }
}
