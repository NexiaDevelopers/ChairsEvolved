package net.nexia.chairsevolved;

import net.nexia.chairsevolved.commands.Commands;
import net.nexia.chairsevolved.managers.DataManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ChairsEvolved extends JavaPlugin {

    public File chairs;
    public YamlConfiguration chairsYML;
    public static ChairsEvolved Instance;

    public DataManager dataManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        Instance = this;

        this.dataManager = new DataManager(this);

        this.saveDefaultConfig();
        registerConfig();

        registerCommands();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerCommands() {

        getCommand("chairs").setExecutor(new Commands(this));
        getCommand("chairs").setTabCompleter(new Commands(this));

    }

    private void registerConfig() {

        getConfig().options().copyDefaults(true);
        saveConfig();

    }
}
