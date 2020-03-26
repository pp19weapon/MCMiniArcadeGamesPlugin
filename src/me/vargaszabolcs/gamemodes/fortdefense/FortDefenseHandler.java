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

    private Location centralLocation = null;
    private int fortSize = 25;
    private static int MAX_ALLOWED_MOBS = 20;
    private boolean isGameRunning = false;
    private Plugin plugin;
    private World currentWorld;

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
        prepareFort(currentWorld);
        startCheckingMonstersInArea(currentWorld);
        waveHandler.startWaves(plugin, currentWorld, centralLocation, fortSize);

    }

    public void stopGame(){
        stopCheckingMonstersInArea();
        isGameRunning = false;
    }

    int taskID = -1;
    private void startCheckingMonstersInArea(World currentWorld){
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                checkMonstersInArea(currentWorld);
            }
        }, 40, 200);
    }
    private void stopCheckingMonstersInArea(){
        if (taskID != -1) {
            Bukkit.getScheduler().cancelTask(taskID);
        }
    }

    private int lastTimeMonstersInArea = 0;
    private void checkMonstersInArea(World currentWorld) {
        int mobsInArea = 0;
        if (centralLocation != null) {
            List<Entity> entitiesInArea = (List<Entity>) currentWorld.getNearbyEntities(centralLocation, fortSize, 7, fortSize);
            for (Entity entity : entitiesInArea) {
                if (entity instanceof Monster) {
                    mobsInArea++;
                }
            }

            if ((mobsInArea <= MAX_ALLOWED_MOBS) && (mobsInArea != lastTimeMonstersInArea)) {
                Bukkit.getServer().broadcastMessage("WARNING! Currently " + mobsInArea + " monsters in fort area out of " + MAX_ALLOWED_MOBS + "!");
                lastTimeMonstersInArea = mobsInArea;
            } else if (mobsInArea > MAX_ALLOWED_MOBS) {
                Bukkit.getServer().broadcastMessage(mobsInArea + " monsters made it into the fort, GG!");
                stopGame();
            }
        }
    }

    private void prepareFort(World currentWorld) {
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
                    currentWorld.getBlockAt(blockPlaceLoc).setType(Material.BEDROCK);
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
