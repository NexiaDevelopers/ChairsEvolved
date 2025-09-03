package net.nexia.chairsEvolved;

import net.nexia.chairsEvolved.data.ConfigData;
import net.nexia.chairsEvolved.listeners.BlockInteractListener;
import net.nexia.chairsEvolved.listeners.DismountListener;
import net.nexia.chairsEvolved.managers.ChairManager;
import net.nexia.chairsEvolved.utils.CustomSerializer;
import net.nexia.nexiaapi.command.CommandHandler;
import net.nexia.nexiaapi.command.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChairsEvolved extends JavaPlugin {

    private static ChairsEvolved _main;
    public static ChairsEvolved getInstance() {
        return _main;
    }

    public NamespacedKey toggleKey;

    ChairManager _chairManager;
    ConfigData _configData;

    @Override
    public void onEnable() {
        _main = this;
        
        saveDefaultConfig();

        getConfig().options().copyDefaults(true);

        _configData = CustomSerializer.deserialize(ConfigData.class, this, "config.yml");
        _configData.save();

        _chairManager = new ChairManager();

        toggleKey = new NamespacedKey(this, "chairs.toggle");
        CommandHandler handler = new CommandHandler(this, "chairs");
        handler.addCommand(new PlayerCommand("toggle", (sender, args) -> {
            Player player = (Player) sender;
            PersistentDataContainer container = player.getPersistentDataContainer();
            boolean canSit = container.getOrDefault(toggleKey, PersistentDataType.BOOLEAN, false);
            container.set(toggleKey, PersistentDataType.BOOLEAN, !canSit);
            player.sendMessage(ChatColor.GREEN + (canSit ?
                    _configData.getRandomEnabledMessage() :
                    _configData.getRandomDisabledMessage())
            );
        }));

        registerEvents();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new BlockInteractListener(), this);
        getServer().getPluginManager().registerEvents(new DismountListener(), this);
    }

    public ChairManager getChairManager() {
        return _chairManager;
    }

    public ConfigData getConfigData() {
        return _configData;
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            _chairManager.dismount(player);
        }
    }

}
