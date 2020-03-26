package me.vargaszabolcs.gamemodes.fortdefense;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class MonsterHandler {
    //TODO make it configurable
    public static int MAX_ALLOWED_MOBS = 20;

    private Location centralLocation;
    private int fortSize;
    private int offset;
    private int maxSpawnDistance;
    private World world;

    FortDefenseHandler fortDefenseHandler;

    MonsterHandler(FortDefenseHandler p_fortDefenseHandler, int p_maxSpawnDistance, int p_offset){
        fortDefenseHandler = p_fortDefenseHandler;
        centralLocation = fortDefenseHandler.centralLocation;
        world = fortDefenseHandler.currentWorld;
        fortSize = fortDefenseHandler.fortSize;
        maxSpawnDistance = p_maxSpawnDistance;
        offset = p_offset;
    }

    public void spawnWave(Wave wave){

        for (int i = 0; i <= wave.getEntitiesToSpawn().size() - 1; i++){
            for (int count = 0; count <= wave.getNumbersToSpawn().get(i); count++) {
                double x = Math.random() * (maxSpawnDistance - (-maxSpawnDistance) + 1) + -maxSpawnDistance;
                System.out.println("Rand Spawn x " + x);
                x = x > 0 ? centralLocation.getBlockX() + x + fortSize + offset : centralLocation.getBlockX() - x - fortSize - offset;
                System.out.println("Spawn x " + x);

                double z = Math.random() * (maxSpawnDistance - (-maxSpawnDistance) + 1) + -maxSpawnDistance;
                z = z > 0 ? centralLocation.getBlockZ() + z + fortSize + offset : centralLocation.getBlockZ() - z - fortSize - offset;

                double y = FortDefenseHandler.findHighestValidBlock(world, new Location(world, x, 0, z)) + 1;

                Location spawnLocation = new Location(world, x, y, z);
                world.spawnEntity(spawnLocation, wave.getEntitiesToSpawn().get(i));

                System.out.println("Spawning " + wave.getEntitiesToSpawn().get(i).name() + " at " + spawnLocation.toString());
            }
        }
    }

    int taskID = -1;
    public void startCheckingMonstersInArea(Plugin plugin){
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                checkMonstersInArea(world);
            }
        }, 40, 200);
    }
    public void stopCheckingMonstersInArea(){
        if (taskID != -1) {
            Bukkit.getScheduler().cancelTask(taskID);
        }
    }

    int lastTimeMonstersInArea = 0;
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
                fortDefenseHandler.stopGame();
            }
        }
    }
}
