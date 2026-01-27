package com.cursebyte.plugin.utils;

import org.bukkit.command.CommandSender;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class MessageUtils {
    
    // Prefix untuk semua pesan plugin
    private static final Component PREFIX = Component.text("[", NamedTextColor.DARK_GRAY)
            .append(Component.text("Partai Plugin", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text("]", NamedTextColor.DARK_GRAY))
            .append(Component.space());

    /**
     * Mengirim pesan sukses ke CommandSender
     */
    public static void sendSuccess(CommandSender sender, String message) {
        sender.sendMessage(PREFIX.append(
                Component.text(message, NamedTextColor.GREEN)
        ));
    }

    /**
     * Mengirim pesan error ke CommandSender
     */
    public static void sendError(CommandSender sender, String message) {
        sender.sendMessage(PREFIX.append(
                Component.text(message, NamedTextColor.RED)
        ));
    }

    /**
     * Mengirim pesan info ke CommandSender
     */
    public static void sendInfo(CommandSender sender, String message) {
        sender.sendMessage(PREFIX.append(
                Component.text(message, NamedTextColor.YELLOW)
        ));
    }

    /**
     * Mengirim pesan warning ke CommandSender
     */
    public static void sendWarning(CommandSender sender, String message) {
        sender.sendMessage(PREFIX.append(
                Component.text(message, NamedTextColor.GOLD)
        ));
    }

    /**
     * Mengirim pesan custom dengan warna tertentu
     */
    public static void sendMessage(CommandSender sender, String message, NamedTextColor color) {
        sender.sendMessage(PREFIX.append(
                Component.text(message, color)
        ));
    }

    /**
     * Mengirim pesan tanpa prefix
     */
    public static void sendRaw(CommandSender sender, String message) {
        sender.sendMessage(Component.text(message, NamedTextColor.GRAY));
    }

    /**
     * Mengirim pesan tanpa prefix dengan warna custom
     */
    public static void sendRaw(CommandSender sender, String message, NamedTextColor color) {
        sender.sendMessage(Component.text(message, color));
    }

    /**
     * Membuat header untuk menu atau list
     */
    public static Component createHeader(String title) {
        return Component.text("═══════ ", NamedTextColor.DARK_GRAY)
                .append(Component.text(title, NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" ═══════", NamedTextColor.DARK_GRAY));
    }

    /**
     * Membuat footer untuk menu atau list
     */
    public static Component createFooter() {
        return Component.text("═════════════════════", NamedTextColor.DARK_GRAY);
    }

    /**
     * Mengirim header ke CommandSender
     */
    public static void sendHeader(CommandSender sender, String title) {
        sender.sendMessage(createHeader(title));
    }

    /**
     * Mengirim footer ke CommandSender
     */
    public static void sendFooter(CommandSender sender) {
        sender.sendMessage(createFooter());
    }

    /**
     * Format pesan dengan placeholder
     * Contoh: formatMessage("Saldo partai: {balance}", "{balance}", "10000")
     */
    public static String formatMessage(String template, String placeholder, String value) {
        return template.replace(placeholder, value);
    }

    /**
     * Format currency (Rupiah)
     */
    public static String formatCurrency(double amount) {
        return String.format("Rp %.2f", amount);
    }

    /**
     * Format number dengan pemisah ribuan
     */
    public static String formatNumber(int number) {
        return String.format("%,d", number);
    }
}
