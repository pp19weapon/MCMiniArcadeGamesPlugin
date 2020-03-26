package me.vargaszabolcs.gamemodes.fortdefense;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

public class WaveHandler {
    WaveHandler(){}

    public void startWaves(Plugin plugin, World currentWorld, Location centralLocation, int fortSize){
        MonsterHandler monsterHandler = new MonsterHandler(currentWorld, centralLocation, fortSize, 2, 5);

        WaveSpawner waveSpawner = new WaveSpawner(currentWorld, monsterHandler);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, waveSpawner, 20, 400);
    }
}

class WaveSpawner implements Runnable{
    World currentWorld;
    MonsterHandler monsterHandler;

    WaveSpawner(World p_world, MonsterHandler p_monsterHandler){
        currentWorld = p_world;
        monsterHandler = p_monsterHandler;
    }

    boolean isNighttime(){
        if ((currentWorld.getTime() < 12300) || (currentWorld.getTime() > 23850)){
            return false;
        }
        return true;
    }

    Wave testWave = new Wave(new EntityType[]{EntityType.BLAZE, EntityType.SKELETON}, new int[]{20, 20});
    boolean alreadySpawned = false;

    @Override
    public void run() {
        if (isNighttime()) {
            if (alreadySpawned == false) {
                monsterHandler.spawnWave(testWave);
                alreadySpawned = true;
            }
        } else if (!isNighttime()) {
            if (alreadySpawned == true) {
                alreadySpawned = false;
            }
        }
    }
}
