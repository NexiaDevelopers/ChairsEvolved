package net.nexia.chairsEvolved.listeners;

import net.nexia.chairsEvolved.ChairsEvolved;
import net.nexia.chairsEvolved.data.ConfigData;
import net.nexia.chairsEvolved.managers.ChairManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class BlockInteractListener implements Listener {

    final ChairManager _chairManager = ChairsEvolved.getInstance().getChairManager();
    final ConfigData _configData = ChairsEvolved.getInstance().getConfigData();

    @EventHandler
    public void onBlockRightClick(PlayerInteractEvent e) {

        Player player = e.getPlayer();
        Block clickedBlock = e.getClickedBlock();

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (clickedBlock == null) {
            return;
        }

        if (clickedBlock.getLocation().add(0, 1, 0).getBlock().getType() != Material.AIR) {
            return;
        }

        if (_configData.requireEmptyHand && player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            return;
        }

        if (_configData.blacklistedWorlds.contains(player.getWorld().getName())) {
            return;
        }

        if (clickedBlock.getBlockData() instanceof Stairs stairs) {
            if (stairs.getHalf() != Stairs.Half.BOTTOM) {
                return;
            }
        } else if (clickedBlock.getBlockData() instanceof Slab slab) {
            if (slab.getType() != Slab.Type.BOTTOM) {
                return;
            }
        } else {
            return;
        }

        _chairManager.sit(player, clickedBlock);

    }
}
