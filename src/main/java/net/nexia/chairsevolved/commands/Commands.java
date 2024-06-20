package net.nexia.chairsevolved.commands;

import net.nexia.chairsevolved.ChairsEvolved;
import net.nexia.chairsevolved.managers.DataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {

    private DataManager dataManager;
    private ChairsEvolved pluginConfig;

    public Commands(ChairsEvolved plugin) {
        pluginConfig = plugin;
        dataManager = plugin.dataManager;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

}
