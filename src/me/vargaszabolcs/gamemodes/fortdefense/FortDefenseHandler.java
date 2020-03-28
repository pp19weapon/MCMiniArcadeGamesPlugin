package me.vargaszabolcs.gamemodes.fortdefense;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class FortDefenseHandler {

    //TODO make it configurable
    public Location centralLocation = null;
    public int fortSize = 10;
    public boolean isGameRunning = false;
    public Plugin plugin;
    public World currentWorld;

    WaveHandler waveHandler;


    public FortDefenseHandler(CommandSender sender, Plugin plg){
        plugin = plg;
        Player usingPlayer = Bukkit.getPlayer(sender.getName());
        currentWorld = usingPlayer.getWorld();
        centralLocation = usingPlayer.getLocation();
        waveHandler = new WaveHandler();

    }

    public void startGame(){
        isGameRunning = true;
        prepareFort();
        waveHandler.startWaves(this);

    }

    public void stopGame(){
        waveHandler.stopWaves();
        isGameRunning = false;
    }



    private void prepareFort() {
        centralLocation.getBlock().setType(Material.GLOWSTONE);

        Location blockPlaceLoc = null;
        int minXCord = centralLocation.getBlockX() - fortSize;
        int maxXCord = centralLocation.getBlockX() + fortSize;
        int minZCord = centralLocation.getBlockZ() - fortSize;
        int maxZCord = centralLocation.getBlockZ() + fortSize;

        System.out.println("Fort bound: max -> " + maxXCord + ":" + maxZCord);
        System.out.println("Fort bound: min -> " + minXCord + ":" + minZCord);

        for (int x = minXCord; x <= maxXCord; x++){
            for (int z = minZCord; z <= maxZCord; z++){
                //Get edges of square
                if ((x == minXCord) ||
                        (z == minZCord) ||
                        (x == maxXCord) ||
                        (z == maxZCord))
                {
                    int y = findHighestValidBlock(currentWorld, new Location(currentWorld, x, 0, z));
                    blockPlaceLoc = new Location(currentWorld, x, y, z);
                    currentWorld.getBlockAt(blockPlaceLoc).setType(Material.OAK_WOOD);
                }
            }
        }
    }

    //Finds the highest block which isn't empty and returns it
    public static int findHighestValidBlock(World world, Location loc){
        for (int y = 255; y >= 1; y--){
            Block currentBlock = world.getBlockAt(loc.getBlockX(), y, loc.getBlockZ());
            if ((!currentBlock.isPassable()) &&
                    (currentBlock.getType().isOccluding())){
                return y;
            }
        }
        return 0;
    }
}
