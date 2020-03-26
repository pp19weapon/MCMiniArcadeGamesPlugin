package me.vargaszabolcs.gamemodes.fortdefense;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class MonsterHandler {
    private Location centralLocation;
    private int fortSize;
    private int offset;
    private int maxSpawnDistance;
    private World world;

    MonsterHandler(World wrd, Location loc, int fortsize, int distOffset, int maxdistance){
        centralLocation = loc;
        world = wrd;
        fortSize = fortsize;
        maxSpawnDistance = maxdistance;
        offset = distOffset;
    }

    public void spawnWave(Wave wave){

        for (int i = 0; i <= wave.getEntitiesToSpawn().length - 1; i++){
            for (int count = 0; count <= wave.getNumbersToSpawn()[i]; count++) {
                double x = Math.random() * (maxSpawnDistance - (-maxSpawnDistance) + 1) + -maxSpawnDistance;
                System.out.println("Rand Spawn x " + x);
                x = x > 0 ? centralLocation.getBlockX() + x + fortSize + offset : centralLocation.getBlockX() - x - fortSize - offset;
                System.out.println("Spawn x " + x);

                double z = Math.random() * (maxSpawnDistance - (-maxSpawnDistance) + 1) + -maxSpawnDistance;
                z = z > 0 ? centralLocation.getBlockZ() + z + fortSize + offset : centralLocation.getBlockZ() - z - fortSize - offset;

                double y = FortDefenseHandler.findHighestValidBlock(world, new Location(world, x, 0, z)) + 1;

                Location spawnLocation = new Location(world, x, y, z);
                Entity ent = world.spawnEntity(spawnLocation, wave.getEntitiesToSpawn()[i]);



                System.out.println("Spawning " + wave.getEntitiesToSpawn()[i].name() + " at " + spawnLocation.toString());
            }
        }
    }
}
