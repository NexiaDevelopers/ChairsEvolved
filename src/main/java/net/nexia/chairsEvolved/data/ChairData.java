package net.nexia.chairsEvolved.data;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class ChairData {

    public final Player player;
    public final Location location;
    public ArmorStand armorStand;
    public BukkitTask task;

    public ChairData(Player player, Location location) {
        this.player = player;
        this.location = location;
    }

    public Location getSitLocation() {
        return location.clone().add(0.5, 0.5, 0.5);
    }

    public Location getDismountLocation() {
        return location.clone().add(0.5, 1.2, 0.5);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ChairData data && data.location.equals(location);
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }
}
