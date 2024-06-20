package net.nexia.chairsevolved.managers;

import net.nexia.chairsevolved.ChairsEvolved;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {

    private ChairsEvolved plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    // Constructor
    public DataManager(ChairsEvolved plugin) {
        this.plugin = plugin;
        // Saves/initializes the config
        saveDefaultConfig();
    }

    public void reloadConfig() {
        // Checks to see if a config file exists
        if (this.configFile == null) {
            // No config file, creating one!
            this.configFile = new File(this.plugin.getDataFolder(), "chairs.yml");
        }
        // Reloading the config file.
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource("chairs.yml");

        // Checks to make sure Input stream is not null
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    // Get Config Method
    public FileConfiguration getConfig() {
        // Checks to see if a config file exists
        if (this.dataConfig == null) {
            reloadConfig();
        }
        return this.dataConfig;
    }

    //Save Config Method
    public void saveConfig() {
        // Checks to see if a config file exists
        if (this.dataConfig == null || this.configFile == null) {
            // If there is no file, do nothing.
            return;
        }

        // Save the config file
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
        }
    }

    public void saveDefaultConfig() {
        // Checks to see if a config file exists
        if (this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "chairs.yml");
        }

        if (!this.configFile.exists()) {
            this.plugin.saveResource("chairs.yml", false);
        }
    }
}