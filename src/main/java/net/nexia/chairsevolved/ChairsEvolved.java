package net.nexia.chairsevolved;

import net.nexia.nexiaapi.Config;

import net.nexia.chairsevolved.commands.Commands;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ChairsEvolved extends JavaPlugin {

    public File chairs;
    public YamlConfiguration chairsYML;
    public static ChairsEvolved Instance;

    public Config chairsConfig;
    public Config messageConfig;
    public Config mainConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic

        Instance = this;

        this.chairsConfig = new Config(this, "chairs.yml");
        this.messageConfig = new Config(this, "messages.yml");
        this.mainConfig = new Config(this, "config.yml");

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
