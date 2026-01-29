package com.cursebyte.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import com.cursebyte.plugin.command.AdminPartaiCommand;
import com.cursebyte.plugin.command.PartaiCommand;
import com.cursebyte.plugin.completer.AdminPartaiTabCompleter;
import com.cursebyte.plugin.completer.PartaiTabCompleter;
import com.cursebyte.plugin.database.DatabaseManager;
import com.cursebyte.plugin.listener.PartaiChatListener;
import com.cursebyte.plugin.modules.member.MemberManager;
import com.cursebyte.plugin.modules.partai.PartaiManager;
import com.cursebyte.plugin.modules.relasi.RelasiPartaiManager;

public final class PartaiCore extends JavaPlugin {
    private final PartaiCore plug = this;
    private static PartaiCore instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        DatabaseManager.init(getDataFolder().getAbsolutePath());
        PartaiManager.initTable();
        MemberManager.initTable();
        RelasiPartaiManager.initTable();

        getLogger().info("CursebyteCore berhasil dimuat!");

        // Plugin startup logic
        registerListeners();
        registerCommands();
        registerCompleters();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("CursebyteCore dimatikan!");
    }

    /**
     * Mendaftarkan Listener
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PartaiChatListener(), this);
    }

    /**
     * Mendaftarkan Command
     */

    private void registerCommands() {
        this.getCommand("partai").setExecutor(new PartaiCommand(plug));
        this.getCommand("adminpartai").setExecutor(new AdminPartaiCommand(plug));
    }

    private void registerCompleters() {
        this.getCommand("partai").setTabCompleter(new PartaiTabCompleter(plug));
        this.getCommand("adminpartai").setTabCompleter(new AdminPartaiTabCompleter(plug));
    }

    /**
     * Mendapatkan instance PartaiCore
     * 
     * @return instance plugin
     */
    public static PartaiCore getInstance() {
        return instance;
    }
}
