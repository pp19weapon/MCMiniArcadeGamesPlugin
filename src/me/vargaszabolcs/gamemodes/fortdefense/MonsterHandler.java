package me.vargaszabolcs.gamemodes.fortdefense;

import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
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
        centralLocation = p_fortDefenseHandler.centralLocation;
        world = p_fortDefenseHandler.currentWorld;
        fortSize = p_fortDefenseHandler.fortSize;
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
                spawnEntity(spawnLocation, wave.getEntitiesToSpawn().get(i));
            }
        }
    }

    private net.minecraft.server.v1_15_R1.Entity spawnEntity(Location spawnLoc, EntityType entType){
        //WorldServer nmsworld = ((CraftWorld) spawnLoc.getWorld()).getHandle();
        net.minecraft.server.v1_15_R1.Entity ent = ((CraftEntity) world.spawnEntity(spawnLoc, entType)).getHandle();
        ent.setCustomName(new ChatComponentText("PUSSYFUCKER"));
        ent.setCustomNameVisible(true);
        ((EntityMonster) ent).targetSelector.a(0, new PathfinderGoalNearestAttackableTarget((EntityInsentient) ent, EntityHuman.class, true));

        System.out.println("Spawning " + entType.toString() + " at " + spawnLoc.toString());

        return ent;
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
            List<Entity> entitiesInArea = (List<Entity>) currentWorld.getNearbyEntities(centralLocation, fortSize, 3, fortSize);
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
