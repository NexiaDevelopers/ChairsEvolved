package net.nexia.chairsEvolved.managers;

import net.nexia.chairsEvolved.ChairsEvolved;
import net.nexia.chairsEvolved.data.ChairData;
import net.nexia.chairsEvolved.data.ConfigData;
import net.nexia.nexiaapi.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ChairManager {

    final ConfigData _configData = ChairsEvolved.getInstance().getConfigData();
    final Set<ChairData> _chairs = new HashSet<>();

    public void sit(Player player, Block block) {
        ChairData chair = new ChairData(player, block.getLocation());

        if (!_configData.allowChairHopping) {
            if (isSitting(player)) {
                if (_configData.showErrors && _configData.alreadySittingError != null) {
                    player.sendMessage(Utils.color(_configData.alreadySittingError));
                }
                return;
            }
        }

        // If player has toggled chairs
        PersistentDataContainer container = player.getPersistentDataContainer();
        if (container.getOrDefault(ChairsEvolved.getInstance().toggleKey, PersistentDataType.BOOLEAN, false)) {
            return;
        }

        Player sittingPlayer = getSittingPlayer(block);
        if (sittingPlayer != null && sittingPlayer != player) {
            if (_configData.showErrors && _configData.occupiedChairError != null) {
                player.sendMessage(Utils.color(_configData.occupiedChairError));
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

        _chairs.add(chair);
    }

    public void dismount(Player player) {
        Optional<ChairData> data = _chairs.stream()
                .filter(chair -> chair.player.equals(player))
                .findFirst();

        if (data.isEmpty()) {
            return;
        }

        ChairData chair = data.get();

        _chairs.remove(chair);

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
        return _chairs.stream().anyMatch(chair -> chair.player.equals(player));
    }

    public boolean isChair(Block block) {
        return _chairs.stream().anyMatch(chair -> chair.location.equals(block.getLocation()));
    }

    public Player getSittingPlayer(Block block) {
        return _chairs.stream()
                .filter(chair -> chair.location.equals(block.getLocation()))
                .map(chair -> chair.player)
                .findFirst()
                .orElse(null);
    }
}
