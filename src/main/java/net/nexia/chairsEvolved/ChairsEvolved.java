package net.nexia.chairsEvolved;

import net.nexia.chairsEvolved.data.ConfigData;
import net.nexia.chairsEvolved.listeners.BlockInteractListener;
import net.nexia.chairsEvolved.listeners.DismountListener;
import net.nexia.chairsEvolved.managers.ChairManager;
import net.nexia.chairsEvolved.utils.CustomSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChairsEvolved extends JavaPlugin {

    private static ChairsEvolved _main;
    public static ChairsEvolved getInstance() {
        return _main;
    }

    private ChairManager chairManager;
    private ConfigData configData;

    @Override
    public void onEnable() {
        _main = this;
        
        saveDefaultConfig();

        getConfig().options().copyDefaults(true);

        configData = CustomSerializer.deserialize(ConfigData.class, this, "config.yml");
        configData.save();

        chairManager = new ChairManager();

        registerEvents();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new BlockInteractListener(), this);
        getServer().getPluginManager().registerEvents(new DismountListener(), this);
    }

    public ChairManager getChairManager() {
        return chairManager;
    }

    public ConfigData getConfigData() {
        return configData;
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            chairManager.dismount(player);
        }
    }

}
