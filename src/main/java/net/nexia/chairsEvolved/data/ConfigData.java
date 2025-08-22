package net.nexia.chairsEvolved.data;

import net.nexia.chairsEvolved.utils.CustomSerializer;
import net.nexia.chairsEvolved.annotations.ResourceKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConfigData extends CustomSerializer {

    public ConfigData(JavaPlugin plugin, String path) {
        super(plugin, path);
    }

    @ResourceKey("require_empty_hand")
    public boolean requireEmptyHand = false;

    @ResourceKey("allow_chair_hopping")
    public boolean allowChairHopping = false;

    @ResourceKey("blacklisted_worlds")
    public List<String> blacklistedWorlds = new ArrayList<>();

    @ResourceKey("show_errors")
    public boolean showErrors = false;

    @ResourceKey("errors.already_sitting")
    public @Nullable String alreadySittingError;

    @ResourceKey("errors.occupied_chair")
    public @Nullable String occupiedChairError;

    @ResourceKey("errors.blacklisted_world")
    public @Nullable String blacklistedWorldError;

}
