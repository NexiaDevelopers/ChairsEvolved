package net.nexia.chairsEvolved.data;

import net.nexia.chairsEvolved.utils.CustomSerializer;
import net.nexia.chairsEvolved.annotations.ResourceKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ConfigData extends CustomSerializer {

    public ConfigData(JavaPlugin plugin, String path) {
        super(plugin, path);
    }

    @ResourceKey("require_empty_hand")
    public boolean requireEmptyHand;

    @ResourceKey("allow_chair_hopping")
    public boolean allowChairHopping;

    @ResourceKey("blacklisted_worlds")
    public List<String> blacklistedWorlds;

    @ResourceKey("show_errors")
    public boolean showErrors;

    @ResourceKey("errors.already_sitting")
    public String alreadySittingError;

    @ResourceKey("errors.occupied_chair")
    public String occupiedChairError;

}
