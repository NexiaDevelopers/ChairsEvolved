package net.nexia.chairsEvolved.listeners;

import net.nexia.chairsEvolved.ChairsEvolved;
import net.nexia.chairsEvolved.managers.ChairManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DismountListener implements Listener {

    final ChairManager chairManager = ChairsEvolved.getInstance().getChairManager();

    @EventHandler
    public void onPlayerDismount(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player player && chairManager.isSitting(player)) {
            chairManager.dismount(player);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (chairManager.isChair(e.getBlock())) {
            Player player = chairManager.getSittingPlayer(e.getBlock());
            chairManager.dismount(player);
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent e) {
        for (Block block : e.getBlocks()) {
            if (chairManager.isChair(block)) {
                Player player = chairManager.getSittingPlayer(block);
                chairManager.dismount(player);
                return;
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent e) {
        for (Block block : e.getBlocks()) {
            if (chairManager.isChair(block)) {
                Player player = chairManager.getSittingPlayer(block);
                chairManager.dismount(player);
                return;
            }
        }
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent e) {
        for (Block block : e.blockList()) {
            if (chairManager.isChair(block)) {
                Player player = chairManager.getSittingPlayer(block);
                chairManager.dismount(player);
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (chairManager.isSitting(e.getPlayer())) {
            chairManager.dismount(e.getPlayer());
        }
    }
}
