package net.nexia.chairsEvolved.managers;

import net.nexia.chairsEvolved.ChairsEvolved;
import net.nexia.chairsEvolved.data.ChairData;
import net.nexia.chairsEvolved.data.ConfigData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ChairManager {

    final ConfigData configData = ChairsEvolved.getInstance().getConfigData();
    final Set<ChairData> chairs = new HashSet<>();

    public void sit(Player player, Block block) {
        ChairData chair = new ChairData(player, block.getLocation());

        if (!configData.allowChairHopping) {
            if (isSitting(player)) {
                if (configData.showErrors && configData.alreadySittingError != null) {
                    player.sendMessage(ChatColor.RED + configData.alreadySittingError);
                }
                return;
            }
        }

        Player sittingPlayer = getSittingPlayer(block);
        if (sittingPlayer != null && sittingPlayer != player) {
            if (configData.showErrors && configData.occupiedChairError != null) {
                player.sendMessage(ChatColor.RED + configData.occupiedChairError);
            }
            return;
        }

        chair.armorStand = block.getWorld().spawn(chair.getSitLocation(), ArmorStand.class, (armorStand) -> {
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setMarker(true);
            armorStand.setSmall(true);
            armorStand.setCollidable(false);
            armorStand.setInvulnerable(true);
            armorStand.addPassenger(player);
        });

        chair.task = Bukkit.getScheduler().runTaskTimerAsynchronously(ChairsEvolved.getInstance(), () ->
                chair.armorStand.setRotation(player.getLocation().getYaw(), 0), 3, 3);

        chairs.add(chair);
    }

    public void dismount(Player player) {
        Optional<ChairData> data = chairs.stream()
                .filter(chair -> chair.player.equals(player))
                .findFirst();

        if (data.isEmpty()) {
            return;
        }

        ChairData chair = data.get();

        chairs.remove(chair);

        chair.task.cancel();
        chair.player.leaveVehicle();
        chair.armorStand.remove();

        if (player.isSneaking()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(ChairsEvolved.getInstance(), () -> {
                Location location = chair.getDismountLocation();
                location.setDirection(player.getLocation().getDirection());
                player.teleport(location);
            }, 1);
        }
    }

    public boolean isSitting(Player player) {
        return chairs.stream().anyMatch(chair -> chair.player.equals(player));
    }

    public boolean isChair(Block block) {
        return chairs.stream().anyMatch(chair -> chair.location.equals(block.getLocation()));
    }

    public Player getSittingPlayer(Block block) {
        return chairs.stream()
                .filter(chair -> chair.location.equals(block.getLocation()))
                .map(chair -> chair.player)
                .findFirst()
                .orElse(null);
    }
}
